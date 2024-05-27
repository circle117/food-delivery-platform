package com.joy.service;

import com.joy.dto.EmployeeDTO;
import com.joy.dto.EmployeeLoginDTO;
import com.joy.dto.EmployeePageQueryDTO;
import com.joy.entity.Employee;
import com.joy.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * add new employee
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * query employees by page
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * enable or disable an employee account
     * @param status
     * @param id
     */
    void enableOrDisable(int status, long id);

    /**
     * get employee info by id
     * @param id
     * @return
     */
    Employee getById(long id);

    /**
     * edit employee info
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
