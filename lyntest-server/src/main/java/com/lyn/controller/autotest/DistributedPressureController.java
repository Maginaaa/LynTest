package com.lyn.controller.autotest;

import com.lyn.vo.AutoTestResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * @author 简单随风
 * @date 2020/12/28
 * 远程调用异步任务，可参考文章 https://jiandansuifeng.blog.csdn.net/article/details/111831083
 * 也可以自行替换成WebClient
 */
@Slf4j
@RestController
@RequestMapping("/distributed")
public class DistributedPressureController {

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @GetMapping("/test")
    public String testAsyncRestTemplate() {

        // 这里使用了一个本地请求，模拟远程请求
        ListenableFuture<ResponseEntity<AutoTestResponseVO>> future = this.asyncRestTemplate.getForEntity(
                "http://localhost:8088/path",
                AutoTestResponseVO.class,
                1
        );

        // 会阻塞的回调方式
        // ResponseEntity<ResponseModel> entity = future.get();
        // ResponseModel body = entity.getBody();

        // 不会阻塞的回调方式
        future.addCallback(new ListenableFutureCallback<ResponseEntity<AutoTestResponseVO>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("请求失败", ex);
            }

            @Override
            public void onSuccess(ResponseEntity<AutoTestResponseVO> result) {
                log.info("调用成功,body = {}", result.getBody());
            }
        });
        return "success";
    }

}
