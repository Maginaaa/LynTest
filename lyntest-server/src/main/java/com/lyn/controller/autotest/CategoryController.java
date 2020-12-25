package com.lyn.controller.autotest;


import com.lyn.model.autotest.CategoryDO;
import com.lyn.service.autotest.CategoryService;
import com.lyn.vo.CreatedVO;
import com.lyn.vo.DeletedVO;
import com.lyn.vo.UpdatedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
*  @author 简单随风
* @since 2020-09-10
*/
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public CreatedVO create(@RequestBody CategoryDO categoryDO) {
        categoryService.createCategory(categoryDO);
        return new CreatedVO();
    }

    @PostMapping("/update")
    public UpdatedVO update(@RequestBody CategoryDO categoryDO) {
        categoryService.editCategory(categoryDO);
        return new UpdatedVO();
    }

    @DeleteMapping("/{id}")
    public DeletedVO delete(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        categoryService.deleteCategory(id);
        return new DeletedVO();
    }

    @GetMapping("/list")
    public List<CategoryDO> list() {
        return categoryService.getCategoryList();
    }

}
