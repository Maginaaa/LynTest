package com.lyn.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  @author 简单随风
 * @since 2020-09-17
 */
@Data
@Accessors(chain = true)
@TableName("mock")
public class MockDO {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * mock接口路径
     */
    private String path;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 描述
     */
    private String description;

    /**
     * 响应头
     */
//    private List<HeadersBO> responseHeader;
    private String responseHeader;

    /**
     * 响应体
     */
    private String responseBody;

    /**
     * 响应代码
     */
    private Integer responseCode;


}
