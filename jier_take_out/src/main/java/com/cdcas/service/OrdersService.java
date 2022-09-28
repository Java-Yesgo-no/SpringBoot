package com.cdcas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdcas.common.R;
import com.cdcas.pojo.Orders;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrdersService extends IService<Orders> {

    R<String> submit(@RequestBody Orders order);

}
