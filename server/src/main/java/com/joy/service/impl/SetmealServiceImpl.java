package com.joy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.joy.constant.MessageConstant;
import com.joy.constant.StatusConstant;
import com.joy.dto.SetmealDTO;
import com.joy.dto.SetmealPageQueryDTO;
import com.joy.entity.Dish;
import com.joy.entity.DishFlavor;
import com.joy.entity.Setmeal;
import com.joy.entity.SetmealDish;
import com.joy.exception.DeletionNotAllowedException;
import com.joy.mapper.SetmealDishMapper;
import com.joy.mapper.SetmealMapper;
import com.joy.result.PageResult;
import com.joy.service.SetmealService;
import com.joy.vo.DishItemVO;
import com.joy.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Transactional
    public void save(SetmealDTO setmealDTO) {
        // save the setmeal entity
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.save(setmeal);

        long setmealId = setmeal.getId();
        // save the dishes
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });

            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    public PageResult queryPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.queryPage(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId -> {
            setmealMapper.deleteById(setmealId);
            setmealDishMapper.deleteBySetmealId(setmealId);
        });
    }

    @Transactional
    public SetmealVO getByIdWithDishes(long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();

        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishMapper.getBySetmealId(id));

        return setmealVO;
    }

    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // delete the entries in the setmeal_dish table
        setmealDishMapper.deleteBySetmealId(setmeal.getId());

        // add the new setmeal_dish entry
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        if (list != null && !list.isEmpty()) {
            list.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmeal.getId());
            });
            setmealDishMapper.insertBatch(list);
        }
    }

    public void enableOrDisable(int status, long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);

        setmealMapper.update(setmeal);
    }

    public List<Setmeal> getByCategoryId(long categoryId) {
        return setmealMapper.getByCategoryId(categoryId);
    }

    public List<DishItemVO> getDishItemBySetmealId(long setmealId) {
        List<DishItemVO> list = setmealMapper.getDishItemBySetmealId(setmealId);
        return list;
    }
}
