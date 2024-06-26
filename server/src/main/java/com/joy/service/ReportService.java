package com.joy.service;

import com.aliyuncs.http.HttpResponse;
import com.joy.vo.OrderReportVO;
import com.joy.vo.SalesTop10ReportVO;
import com.joy.vo.TurnoverReportVO;
import com.joy.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

public interface ReportService {

    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getTopStatistics(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response) throws IOException;
}
