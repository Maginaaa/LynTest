package com.lyn.service.autotest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.model.autotest.CategoryDO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-10
 */
public interface CategoryService extends IService<CategoryDO> {

    /**
     * 获取分类列表
     * @return
     */
    List<CategoryDO> getCategoryList();

    /**
     * 创建分类
     */
    boolean createCategory(CategoryDO categoryDO);

    /**
     * 更新分类信息
     */
    boolean editCategory(CategoryDO categoryDO);


    /**
     * 删除分类
     */
    boolean deleteCategory(Integer id);
}
