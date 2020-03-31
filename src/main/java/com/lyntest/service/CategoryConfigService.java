package com.lyntest.service;

import com.lyntest.bean.Category;
import com.lyntest.bean.ResponseVo;

/**
 * @author 简单随风
 * @date 2019/10/22
 */
public interface CategoryConfigService {

    ResponseVo getCategoryList();

    /** 新增变量 */
    ResponseVo createNewCategory(Category category);

    /** 修改变量 */
    ResponseVo updateCategory(Category category);

    /** 删除变量 */
    ResponseVo deleteCategory(int id);
}
