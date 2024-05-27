package com.joy.controller.admin;

import com.joy.dto.CategoryDTO;
import com.joy.dto.CategoryPageQueryDTO;
import com.joy.entity.Category;
import com.joy.result.PageResult;
import com.joy.result.Result;
import com.joy.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 */
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "API for category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * query categories by page
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "get categories")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("菜品分类查询，参数为: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * enable or disable one category
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "enable or disable one category")
    public Result enableOrDisable(@PathVariable int status, long id) {
        categoryService.enableOrDisable(status, id);
        return Result.success();
    }

    @PutMapping()
    @ApiOperation(value = "update category info")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @PostMapping()
    @ApiOperation(value = "add new category")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @DeleteMapping()
    @ApiOperation(value = "delete one category")
    public Result deleteById(long id) {
        categoryService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "get the category list")
    public Result<Category[]> getList(int type) {
        Category[] categories = categoryService.getList(type);
        return Result.success(categories);
    }
}
