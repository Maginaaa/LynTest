package com.lyntest.controller;

import com.lyntest.bean.Category;
import com.lyntest.bean.ResponseVo;
import com.lyntest.service.CategoryConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 简单随风
 * @date 2019/10/24
 */
@RestController
@RequestMapping(value="/category")
public class CategoryController {

    @Autowired
    private CategoryConfigService categoryConfigService;

    /**
     * 获取分类列表
     */
    @GetMapping(value = "/list")
    public ResponseVo getCategoryList(){

        return categoryConfigService.getCategoryList();
    }


    /**
     * 新增分类
     */
    @PostMapping(value = "/create")
    public ResponseVo createCategory(@RequestBody Category category){

        return categoryConfigService.createNewCategory(category);
    }

    /**
     * 更新分类
     */
    @PostMapping(value = "/update")
    public ResponseVo updateCategory(@RequestBody Category category){

        return categoryConfigService.updateCategory(category);
    }

    /**
     * 删除分类
     */
    @PostMapping(value = "/delete")
    public ResponseVo deleteCategory(@RequestBody Integer id){

        return categoryConfigService.deleteCategory(id);
    }
}
