package com.lyntest.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author 简单随风
 * @date 2019/10/24
 */
@Data
public class CollectionFocus {

    private int id;

    private Boolean focusType;

    /** 关注集合的集合id */
    private int collectionId;

    /** 关注人的code */
    private String userCode;

    /** 关注日期 */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date focusDate;
}
