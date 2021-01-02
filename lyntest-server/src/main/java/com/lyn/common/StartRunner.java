package com.lyn.common;

import com.alibaba.fastjson.JSON;
import com.lyn.async.CollectionExecute;
import com.lyn.model.autotest.CollectionDO;
import com.lyn.service.autotest.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author 简单随风
 * @date 2020/12/16
 */
@Component
@Slf4j
public class StartRunner implements CommandLineRunner {

    @Autowired
    private CollectionExecute collectionExecute;

    @Autowired
    private CollectionService collectionService;

    /**
     * 集合定时执行的初始化
     */
    @Override
    public void run(String... args) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        log.info("当前时间为： "+ new Date());
        // 查询需要开启定时任务的list
        List<CollectionDO> collectionList = collectionService.timingSwitchOpenCollectionIdList();
        collectionList.forEach(e-> collectionExecute.start(e));
    }
}
