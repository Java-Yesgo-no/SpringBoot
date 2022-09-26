package com.cdcas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdcas.mapper.UserMapper;
import com.cdcas.pojo.User;
import com.cdcas.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
