package com.cdcas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdcas.common.BaseContext;
import com.cdcas.common.R;
import com.cdcas.pojo.Orders;
import com.cdcas.pojo.User;
import com.cdcas.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;

@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单操作
     *
     * @param order
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order) {
        ordersService.submit(order);
        return R.success("下单成功");
    }


    /**
     * 查询所有的订单
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping({"/userPage", "/page"})
    public R<Page<Orders>> userPage(Long page, Long pageSize, String number, String beginTime, String endTime) {
        Page<Orders> userPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//        小于等于
        queryWrapper.lt(beginTime != null, Orders::getOrderTime, endTime);
//        大于等于
        queryWrapper.ge(endTime != null, Orders::getOrderTime, beginTime);
        queryWrapper.eq(number != null, Orders::getId, number);
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrent());
        ordersService.page(userPage, queryWrapper);
        return R.success(userPage);
    }

    /**
     * 更改订单的状态
     *
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders) {
        ordersService.updateById(orders);
        return R.success("更新成功");
    }


}
