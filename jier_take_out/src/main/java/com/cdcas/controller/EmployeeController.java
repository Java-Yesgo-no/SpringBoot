package com.cdcas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdcas.common.R;
import com.cdcas.mapper.EmployeeMapper;
import com.cdcas.pojo.Employee;
import com.cdcas.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeservice;

    /**
     * 员工登录功能
     * @param httpServletRequest
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee){
        /**
         1.将页面提交的密码password进行md5加密处理
         2.根据页面提交的用户名username查询数据库
         3.如果没有查询到则返回登录失败的结果
         4.密码比对，如果不一致则返回登录失败结果
         5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
         6.登录成功，将员工id存入Session并返回登陆成功结果
         */
//        第一步:将页面提交的密码password进行md5加密处理
        String password=employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());

//        第二步:根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeservice.getOne(queryWrapper);

//        第三步:如果没有查询到则返回登录失败的结果queryWrapper = {LambdaQueryWrapper@7818}
        if (emp==null){
            return R.error("登录失败");
        }

//        第四步:密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }

//        第五步:查看员工状态,如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus()==0){
            return R.error("此账户已禁用");
        }
//       第六步:登陆成功,将员工id存入Session并返回登录成功结果
        httpServletRequest.getSession().setAttribute("employee",emp);
        return R.success(emp);
    }


    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
//        清除Session保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工,员工信息:{}",employee.toString());
//        设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
//        获取当前登录用户的id
        Employee employeeManager= (Employee) request.getSession().getAttribute("employee");
        Long empManagerId=employeeManager.getId();
        employee.setCreateUser(empManagerId);
        employee.setUpdateUser(empManagerId);


        employeeservice.save(employee);
        return R.success("新增员工成功");
    }
}
