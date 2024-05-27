package com.joy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.joy.constant.MessageConstant;
import com.joy.constant.StatusConstant;
import com.joy.dto.CategoryDTO;
import com.joy.dto.CategoryPageQueryDTO;
import com.joy.entity.Category;
import com.joy.exception.DeletionNotAllowedException;
import com.joy.mapper.CategoryMapper;
import com.joy.mapper.DishMapper;
import com.joy.mapper.SetmealMapper;
import com.joy.result.PageResult;
import com.joy.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * category page query
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * enable or disable one category
     * @param status
     * @param id
     */
    public void enableOrDisable(int status, long id) {
        Category category = Category.builder()
                .status(status)
                .id(id)
                .build();
        categoryMapper.update(category);
    }

    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        categoryMapper.update(category);
    }

    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        category.setStatus(StatusConstant.DISABLE);

        categoryMapper.insert(category);
    }

    public void deleteById(long id) {
        int count = dishMapper.countByCategoryId(id);
        if (count > 0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);

        count = setmealMapper.countByCategoryId(id);
        if (count > 0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        categoryMapper.deleteById(id);
    }

    public Category[] getList(Integer type) {
        return categoryMapper.getAll(type);
    }
}
