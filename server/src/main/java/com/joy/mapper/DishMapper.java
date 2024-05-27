package com.joy.mapper;

import com.github.pagehelper.Page;
import com.joy.annotation.AutoFill;
import com.joy.dto.DishPageQueryDTO;
import com.joy.entity.Dish;
import com.joy.enumeration.OperationType;
import com.joy.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById(long id);

    @Delete("delete from dish where id = #{id}")
    void deleteById(long id);

    void deleteByIds(List<Long> ids);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> getList(Dish dish);
}
