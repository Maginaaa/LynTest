package com.lyntest.service.impl;

import com.lyntest.bean.Category;
import com.lyntest.bean.ResponseVo;
import com.lyntest.mapper.ApiTestConfigMapper;
import com.lyntest.service.CategoryConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 简单随风
 * @date 2019/10/22
 */
@Service(value = "CategoryConfigService")
public class CategoryConfigServiceImpl implements CategoryConfigService {

    @Autowired
    private ApiTestConfigMapper apiTestConfigMapper;

    @Override
    public ResponseVo getCategoryList() {

        ResponseVo responseVo = new ResponseVo();

        List<Category> categoryList = apiTestConfigMapper.selectAllCategory();

        responseVo.setIsSuccess(Boolean.TRUE);
        responseVo.setResult(categoryList);

        return responseVo;
    }

    @Override
    public ResponseVo createNewCategory(Category category) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestConfigMapper.createCategory(category);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("创建成功！");
        }

        return responseVo;
    }

    @Override
    public ResponseVo updateCategory(Category category) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestConfigMapper.updateCategory(category);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("更新成功！");
        }
        return responseVo;
    }

    @Override
    public ResponseVo deleteCategory(int id) {

        ResponseVo responseVo = new ResponseVo();

        int i = apiTestConfigMapper.deleteCategory(id);

        if (i == 1){
            responseVo.setIsSuccess(Boolean.TRUE);
            responseVo.setResult("删除成功！");
        }
        return responseVo;
    }
}
