package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    //    新增员工
    @PostMapping
    public Result<String> addEmployee(@RequestBody Employee employee) {
        int affectedRows = employeeService.addEmployee(employee);
        log.info("<UNK>{}", employee);
        if (affectedRows > 0) {

            return Result.success();
        }
        return Result.error("添加出错");


    }

    //分页查询
    @GetMapping("/page")
    public Result<PageResult> QueryEmployee(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("<UNK>{}", employeePageQueryDTO);
        // 返回值是总数和10条员工数据，封装在PageResult，ResturnPageResult
        PageResult pageResult = employeeService.QueryEmployee(employeePageQueryDTO);

        return Result.success(pageResult);

    }

    //启用禁用员工账号
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result employeePower(@PathVariable int status, Long id) {
        employeeService.employeePower(status, id);
        return Result.success("您的修改成功了");
    }

    //编辑员工信息,两个请求
    @GetMapping("/{id}")
    @ApiOperation("显示要修改的原始信息")
    public Result<Employee> selectEmployee(@PathVariable Long id) {
        log.info("<UNK>{传进来的Id是}", id);
        Employee employee = employeeService.selectEmployeeById(id);

        return Result.success(employee);
    }


    @PutMapping
    @ApiOperation("修改员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(employeeDTO);

        return Result.success();
    }


    //修改密码
    @PutMapping("/editPassword")
    @ApiOperation("修改密码")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        boolean success = employeeService.changePassword(passwordEditDTO);
        if (!success) {
            return Result.error("原密码不正确");
        }
        return Result.success("密码修改成功");
    }
}





