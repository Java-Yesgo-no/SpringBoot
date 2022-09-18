package com.cdcas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdcas.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
