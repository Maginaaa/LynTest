package com.lyn.async;

import com.alibaba.fastjson.JSON;
import com.lyn.dto.autotest.CollectionDTO;
import com.lyn.dto.autotest.HttpCaseDTO;
import com.lyn.extension.wechat.WeChatPush;
import com.lyn.model.autotest.CollectionDO;
import com.lyn.model.autotest.CollectionReportDO;
import com.lyn.model.autotest.HttpCaseDO;
import com.lyn.module.message.WsHandler;
import com.lyn.service.autotest.CollectionReportService;
import com.lyn.service.autotest.CollectionService;
import com.lyn.service.autotest.HttpCaseService;
import com.lyn.vo.AutoTestResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * @author 简单随风
 * @date 2020/10/9
 */
@Component
@Slf4j
public class CollectionExecute {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private HttpCaseService httpCaseService;

    @Autowired
    private CollectionReportService collectionReportService;

    @Autowired
    private WsHandler wsHandler;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private WeChatPush weChatPush;

    private static Map<Integer, ScheduledFuture<?>> futureMap = new HashMap<>();

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {

        return new ThreadPoolTaskScheduler();
    }

    @Async
    public void execute(CollectionReportDO collectionReportDO) {

        // 变量初始化
        httpCaseService.variableInit();

        CollectionDTO collection =  collectionService.getCollectionDetail(collectionReportDO.getCollectionId());
        collectionReportDO.setCollectionName(collection.getCollectionName());

        List<HttpCaseDO> caseList = collectionService.getCaseList(collectionReportDO.getCollectionId());
        int totalCount = caseList.size();
        // 当前执行数量
        int executedCount = 0;
        // 断言通过的case数
        int passCount = 0;
        // 断言未通过case数
        int errorCount = 0;

        // 测试报告
        List<AutoTestResponseVO> responseList = new ArrayList<>(totalCount);

        // 遍历pocket
        for (HttpCaseDO httpCaseDO:caseList) {

            executedCount += 1;

            AutoTestResponseVO executeResponse = httpCaseService.casePreExecute(httpCaseService.caseDoToDto(httpCaseDO));
            if (executeResponse.getExpectedIsPass()) {
                passCount += 1;
            } else {
                errorCount += 1;
            }
            responseList.add(executeResponse);

            try {
                wsHandler.broadCast("collection_" + collectionReportDO.getCollectionId() + "_" + (executedCount * 100 / totalCount));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        collectionReportDO.setPassCount(passCount);
        collectionReportDO.setErrorCount(errorCount);
        collectionReportDO.setResult(JSON.toJSONString(responseList));
        collectionReportService.saveNewReport(collectionReportDO);


        // 测试报告推送策略
        Integer pushType = collection.getPushType();
        List<String> pushList =  collection.getPushList();

        String testIsPass = errorCount==0 ? "通过" : "未通过";
        if ((pushType == 2 && errorCount != 0) || pushType == 3) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 执行推送
            String content = "接口测试平台报告 \n";
            content += "【集合名称】" + collectionReportDO.getCollectionName() + "\n";
            content += "【测试时间】" + sdf.format(collectionReportDO.getExecuteTime()) + "\n";
            content += "【测试结果】" + testIsPass + "\n";
            content += "【执行情况】" + "接口总数:" + (passCount+errorCount) + ", "
                    + "成功:" + passCount + ","
                    + "失败:" + errorCount+ "\n";
            content += "【测试报告】http://ip/#/autotest/report-detail/" + collectionReportDO.getId();
            // 实际使用，这里应该 for (String userCode: pushList)来推送多人
            weChatPush.messagePush(content);


        }

    }

    public void collectionTimingTask(CollectionDTO collectionDTO){
        Integer collectionId = collectionDTO.getId();
        Boolean switchIsOpen = collectionDTO.getTimingSwitch();
        CollectionDO collectionDO = new CollectionDO();
        BeanUtils.copyProperties(collectionDTO, collectionDO);
        // 关 -> 关：不作处理
        if (!isStart(collectionId) && !switchIsOpen){
            return;
        }
        // 关 -> 开：启动
        if (!isStart(collectionId) && switchIsOpen){
            start(collectionDO);
        }
        // 开 -> 关：关闭
        if (isStart(collectionId) && !switchIsOpen){
            stop(collectionDTO.getId());
        }
        // 开 -> 开：重启
        if (isStart(collectionId) && switchIsOpen){
            stop(collectionDTO.getId());
            start(collectionDO);
        }
    }


    /**
     * 单集合的启动
     * @param collection
     * @return
     */
    public void start(CollectionDO collection) {
        Integer collectionId = collection.getId();
        // 校验集合是否已存在于map内
        if (isStart(collectionId)) {
            return;
        }
        if (futureMap.containsKey(collectionId)){
            return;
        }

        // 定时任务调度线程池
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MyRunnable(collection.getId()),
                triggerContext -> new CronTrigger(collection.getCron()).nextExecutionTime(triggerContext));
        log.info("开始执行测试集合:" + collection.getId() + "执行时间： " + collection.getCron());
        futureMap.put(collection.getId(), future);
    }

    private void stop(Integer collectionId) {
        boolean taskStartFlag = futureMap.containsKey(collectionId);
        if (taskStartFlag) {
            futureMap.get(collectionId).cancel(true);
        }
        log.info("关闭测试集合:" + collectionId);
        futureMap.remove(collectionId);
    }

    /**
     * 任务是否已经启动
     */
    private Boolean isStart(Integer taskKey) {
        //校验是否已经启动
        if (futureMap.containsKey(taskKey)) {
            return !futureMap.get(taskKey).isCancelled();
        }
        return false;
    }

    private class MyRunnable implements Runnable {
        private Integer collectionId;
        public MyRunnable(Integer collectionId){

            this.collectionId = collectionId;
        }

        @Override
        public void run() {
            CollectionReportDO collectionReportDO = new CollectionReportDO();
            collectionReportDO.setCollectionId(collectionId);

            collectionReportDO.setExecuteTime(new Date());
            collectionReportDO.setExecuteCode("system");
            collectionReportDO.setExecuteName("定时任务执行");
            String reportId = collectionId + "_" + System.currentTimeMillis() ;
            collectionReportDO.setId(reportId);
            execute(collectionReportDO);
        }
    }





}
