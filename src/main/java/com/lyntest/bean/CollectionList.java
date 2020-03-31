package com.lyntest.bean;

import lombok.Data;

/**
 * @author 简单随风
 * @date 2019/10/24
 */
@Data
public class CollectionList {

    private int page;

    private int pageSize;

    private CollectionDetail collectionDetail;
}
