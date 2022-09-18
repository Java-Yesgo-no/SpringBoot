package com.cdcas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdcas.mapper.EmployeeMapper;
import com.cdcas.pojo.Employee;
import com.cdcas.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
