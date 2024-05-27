package com.joy.service;

import com.joy.dto.SetmealDTO;
import com.joy.dto.SetmealPageQueryDTO;
import com.joy.entity.Setmeal;
import com.joy.entity.SetmealDish;
import com.joy.result.PageResult;
import com.joy.vo.DishItemVO;
import com.joy.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    void save(SetmealDTO setmealDTO);

    PageResult queryPage(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDishes(long id);

    void update(SetmealDTO setmealDTO);

    void enableOrDisable(int status, long id);

    List<Setmeal> getByCategoryId(long categoryId);

    List<DishItemVO> getDishItemBySetmealId(long setmealId);
}
