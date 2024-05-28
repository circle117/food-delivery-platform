package com.joy.service;

import com.joy.dto.DishDTO;
import com.joy.dto.DishPageQueryDTO;
import com.joy.entity.Dish;
import com.joy.result.PageResult;
import com.joy.vo.DishVO;

import java.util.List;

public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(long id);

    void updateWithFlavor(DishDTO dishDTO);

    void enableOrDisable(int status, long id);

    List<DishVO> getListWithFlavor(Dish dish);

    List<Dish> getListByCategoryId(long categoryId);

    Dish getById(long id);
}
