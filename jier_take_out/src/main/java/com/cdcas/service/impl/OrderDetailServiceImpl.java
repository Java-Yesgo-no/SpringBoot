package com.cdcas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdcas.mapper.OrderDetailMapper;
import com.cdcas.pojo.OrderDetail;
import com.cdcas.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
