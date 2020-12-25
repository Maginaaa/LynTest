package com.lyn.dto.autotest;

import com.lyn.bo.ExpectedBO;
import com.lyn.bo.HeadersBO;
import com.lyn.bo.VariableSaveBO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author 简单随风
 * @date 2020/9/8
 */
@Data
public class HttpCaseDTO {

    private Integer id;

    /**
     * 创建人
     */
    private String creatorName;

    /**
     * 分组
     */
    private String category;

    /**
     * 接口名称
     */
    @Length(min = 1,max = 32,message = "{autotest.http-case.case-name.max}")
    @NotBlank(message = "{autotest.http-case.case-name.not-blank}")
    private String caseName;

    /**
     * 接口url
     */
    @NotBlank(message = "{autotest.http-case.url.not-blank}")
    private String apiUrl;

    /**
     * 请求方式
     */
    @NotBlank
    private String apiMethod;

    /**
     * 备注
     */
    private String description;

    /**
     * 请求头
     */
    private List<HeadersBO> headerForm;

    /**
     * body类型：1.json 2.urlform
     */
    private Integer bodyType;

    /**
     * body值
     */
    private String bodyValue;

    /**
     * 需要保存的变量
     */
    private List<VariableSaveBO> variableList;

    /**
     * 需要校验的参数
     */
    private List<ExpectedBO> expectedList;
}
