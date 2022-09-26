package com.cdcas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdcas.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
