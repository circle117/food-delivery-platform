package com.joy.mapper;

import com.github.pagehelper.Page;
import com.joy.annotation.AutoFill;
import com.joy.dto.SetmealPageQueryDTO;
import com.joy.entity.Setmeal;
import com.joy.enumeration.OperationType;
import com.joy.vo.DishItemVO;
import com.joy.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(long categoryId);

    @AutoFill(OperationType.INSERT)
    void save(Setmeal setmeal);

    Page<SetmealVO> queryPage(SetmealPageQueryDTO setmealPageQueryDTO);

    @Delete("delete from setmeal where id = #{id}")
    void deleteById(long id);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> getByCategoryId(long categoryId);

    List<DishItemVO> getDishItemBySetmealId(long setmealId);

    Integer countByMap(Map map);
}
