package com.lyn.vo;

import com.lyn.bo.ExpectedBO;
import com.lyn.bo.VariableSaveBO;
import lombok.Data;

import java.util.List;

/**
 * @author 简单随风
 * @date 2020/9/11
 */
@Data
public class AutoTestResponseVO {

    private Integer caseId;

    private String caseName;

    /**
     * 请求
     */
    private String url;
    private String requestText;

    /**
     * 响应文本
     */
    private String responseText;

    /**
     * 响应时间
     */
    private Long responseTime;

    /**
     * 响应代码
     */
    private Integer statusCode;

    /**
     * 响应状态: OK/
     */
    private String message;

    /**
     * 预期值结果
     */
    private Boolean expectedIsPass;
    private List<ExpectedBO> expectedRes;

    private List<VariableSaveBO> variableList;


}
