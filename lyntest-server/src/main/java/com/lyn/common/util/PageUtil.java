package com.lyn.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyn.vo.PageResponseVO;

import java.util.List;

/**
 *
 */
public class PageUtil {

    public static <T> PageResponseVO<T> build(IPage<T> iPage) {
        return new PageResponseVO<T>(Math.toIntExact(iPage.getTotal()), iPage.getRecords(),
                Math.toIntExact(iPage.getCurrent()), Math.toIntExact(iPage.getSize()));
    }

    public static <K, T> PageResponseVO<K> build(IPage<T> iPage, List<K> records) {
        return new PageResponseVO<K>(Math.toIntExact(iPage.getTotal()), records,
                Math.toIntExact(iPage.getCurrent()),
                Math.toIntExact(iPage.getSize()));
    }

}
