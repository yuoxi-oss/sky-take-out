package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    int addEmployee(Employee employee);


    PageResult QueryEmployee(EmployeePageQueryDTO employeePageQueryDTO);


    void employeePower(int status, Long id);

    void updateEmployee(EmployeeDTO employeeDTO);

    Employee selectEmployeeById(Long id);


    boolean changePassword(PasswordEditDTO passwordEditDTO);
}
