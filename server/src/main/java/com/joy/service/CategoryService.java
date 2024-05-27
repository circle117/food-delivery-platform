package com.joy.service;

import com.joy.dto.CategoryDTO;
import com.joy.dto.CategoryPageQueryDTO;
import com.joy.entity.Category;
import com.joy.result.PageResult;

public interface CategoryService {

    /**
     * category page query
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * enable or disable one category
     * @param status
     * @param id
     */
    void enableOrDisable(int status, long id);

    void update(CategoryDTO categoryDTO);

    void save(CategoryDTO categoryDTO);

    void deleteById(long id);

    Category[] getList(Integer type);
}
