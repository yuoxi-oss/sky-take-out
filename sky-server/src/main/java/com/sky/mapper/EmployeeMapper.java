package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user, status) " +
            "values (#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{status})")
    int addEmployee(Employee employee);


    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);


    void update(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee selectEmployeeById(Long id);

    @Update("UPDATE employee\n" +
            "SET password = #{password} " +
            "WHERE username = #{username};")
    void editPassword(PasswordEditDTO passwordEditDTO);


    //查询密码，根据ID
    @Select("SELECT password FROM employee WHERE id = #{empId}")
    String getPasswordById(Long empId);

    //更新密码
    @Update("UPDATE employee SET password = #{encodedNewPass} WHERE id = #{empId}")
    void updatePassword(Long empId, String encodedNewPass);
}
