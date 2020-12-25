package com.lyn.service.autotest.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.mapper.autotest.CategoryMapper;
import com.lyn.model.autotest.CategoryDO;
import com.lyn.service.autotest.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 *  @author 简单随风
 * @since 2020-09-10
 */
@Service
@Transactional
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryDO> getCategoryList() {
        return categoryMapper.selectList(null);
    }

    @Override
    public boolean createCategory(CategoryDO categoryDO) {

        return categoryMapper.insert(categoryDO) > 0;
    }

    @Override
    public boolean editCategory(CategoryDO categoryDO) {
        return categoryMapper.updateById(categoryDO) > 0;
    }

    @Override
    public boolean deleteCategory(Integer id) {

        return categoryMapper.deleteById(id) > 0;
    }
}
