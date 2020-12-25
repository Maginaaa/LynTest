package com.lyn.async;

import com.alibaba.fastjson.JSON;
import com.lyn.dto.autotest.HttpCaseDTO;
import com.lyn.dto.autotest.PressureReportDTO;
import com.lyn.model.autotest.PressureReportDO;
import com.lyn.module.message.WsHandler;
import com.lyn.service.autotest.HttpCaseService;
import com.lyn.service.autotest.PressureReportService;
import com.lyn.vo.AutoTestResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author 简单随风
 * @date 2020/12/17
 */
@Component
public class PressureExecute {

    @Autowired
    private WsHandler wsHandler;

    @Autowired
    private HttpCaseService httpCaseService;

    @Autowired
    private PressureReportService pressureReportService;


    @Async
    public void execute(PressureReportDTO pressureReportDTO, HttpCaseDTO httpCaseDTO) {

        int threads = pressureReportDTO.getThreads();
        int times = pressureReportDTO.getTimes();

        String msg = "pressure_" + httpCaseDTO.getId();

        // 创建缓存线程池
        ExecutorService es = Executors.newCachedThreadPool();
        CompletionService<List<AutoTestResponseVO>> completionService = new ExecutorCompletionService<>(es);
        // 保存回调结果
        List<Future<List<AutoTestResponseVO>>> respList = new ArrayList<>();
        for (int i=0;i<threads;i++){
            respList.add(completionService.submit(()->{
                List<AutoTestResponseVO> resList = new ArrayList<>(times);
                for (int j=0;j<times;j++) {
                    AutoTestResponseVO executeResponse = httpCaseService.caseDoExecute(httpCaseDTO);
                    resList.add(executeResponse);
                }
                return resList;
            }));
        }

        PressureReportDO pressureReportDO = new PressureReportDO();
        BeanUtils.copyProperties(pressureReportDTO, pressureReportDO);
        List<Long> responseTimeList = new ArrayList<>();
        List<AutoTestResponseVO> result = new ArrayList<>();
        int passCount = 0;
        int errorCount = 0;
        long totalTime = 0;

        for (Future<List<AutoTestResponseVO>> o : respList) {
            try {
                List<AutoTestResponseVO> l = o.get();
                for (AutoTestResponseVO res:l){

                    if (res.getExpectedIsPass()){
                        passCount += 1;
                    } else {
                        errorCount += 1;
                    }
                    result.add(res);
                    responseTimeList.add(res.getResponseTime());
                    totalTime += res.getResponseTime();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        responseTimeList.sort(Comparator.comparing(Long::intValue));
        int size = responseTimeList.size();
        pressureReportDO.setMin(Math.toIntExact(responseTimeList.get(0)));
        pressureReportDO.setMax(Math.toIntExact(responseTimeList.get(size-1)));
        pressureReportDO.setAverage(Math.toIntExact(totalTime/(threads * times)));
        pressureReportDO.setMedian(Math.toIntExact(responseTimeList.get(size/2)));
        pressureReportDO.setNinety(Math.toIntExact(responseTimeList.get((int)(size*0.9))));
        pressureReportDO.setNinetyFive(Math.toIntExact(responseTimeList.get((int)(size*0.95))));
        pressureReportDO.setNinetyNine(Math.toIntExact(responseTimeList.get((int)(size*0.99))));
        pressureReportDO.setPassCount(passCount);
        pressureReportDO.setErrorCount(errorCount);
        pressureReportDO.setResult(JSON.toJSONString(result));
        pressureReportService.saveReport(pressureReportDO);


        try {
            wsHandler.broadCast(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        es.shutdown();

    }



}
