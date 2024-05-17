package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

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

    Category[] getList(int type);
}
