package com.joy.service;

import com.joy.vo.BusinessDataVO;
import com.joy.vo.DishOverViewVO;
import com.joy.vo.OrderOverViewVO;
import com.joy.vo.SetmealOverViewVO;
import java.time.LocalDateTime;

public interface WorkspaceService {

    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    OrderOverViewVO getOrderOverView();

    DishOverViewVO getDishOverView();

    SetmealOverViewVO getSetmealOverView();

}
