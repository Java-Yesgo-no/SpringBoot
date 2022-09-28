package com.cdcas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdcas.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
