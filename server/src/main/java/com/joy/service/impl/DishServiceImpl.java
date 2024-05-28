package com.joy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.joy.constant.MessageConstant;
import com.joy.constant.StatusConstant;
import com.joy.dto.DishDTO;
import com.joy.dto.DishPageQueryDTO;
import com.joy.entity.Dish;
import com.joy.entity.DishFlavor;
import com.joy.exception.DeletionNotAllowedException;
import com.joy.mapper.DishFlavorMapper;
import com.joy.mapper.DishMapper;
import com.joy.mapper.SetmealDishMapper;
import com.joy.result.PageResult;
import com.joy.service.DishService;
import com.joy.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // insert one entry to the dish table
        dishMapper.insert(dish);

        long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });

            // insert entries to the flavor table (batch)
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        // if it is deletable: status is enable
        for (long id: ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE)
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        // if it is deletable: connected with a setmeal
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty())
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);

        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    @Transactional
    public DishVO getByIdWithFlavor(long id) {
        Dish dish = dishMapper.getById(id);

        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // update the dish table
        dishMapper.update(dish);

        // delete the dish_flavor table
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // update the dish_flavor table
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    public void enableOrDisable(int status, long id) {
        Dish dish = Dish.builder()
                        .status(status)
                        .id(id)
                        .build();
        dishMapper.update(dish);
    }

    public List<DishVO> getListWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.getList(dish);

        List<DishVO> dishVOList = new LinkedList<>();

        for (Dish d: dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    public List<Dish> getListByCategoryId(long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);

        return dishMapper.getList(dish);
    }

    public Dish getById(long id) {
        return dishMapper.getById(id);
    }
}
