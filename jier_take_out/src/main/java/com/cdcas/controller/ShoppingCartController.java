package com.cdcas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdcas.common.BaseContext;
import com.cdcas.common.R;
import com.cdcas.pojo.ShoppingCart;
import com.cdcas.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();
        return R.success(shoppingCartList);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
//        获取当前用户的Id
        Long aLong = BaseContext.getCurrent();
        shoppingCart.setUserId(aLong);
//        获取到setmealId
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrent());
//        判断该setmealId是否存在
        if (setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
//       如果不存在，则该条信息是菜品
        else {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }
        ShoppingCart shoppingCartResult = shoppingCartService.getOne(queryWrapper);
        if (shoppingCartResult != null) {
            Integer number = shoppingCartResult.getNumber();
            shoppingCartResult.setNumber(number + 1);
            shoppingCartService.updateById(shoppingCartResult);
        } else {
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
        }
        ShoppingCart shoppingCartResultLast = shoppingCartService.getOne(queryWrapper);
        return R.success(shoppingCartResultLast);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> reduce(@RequestBody ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrent());
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
        int number = shoppingCart1.getNumber() - 1;
        if (number == 0) {
            shoppingCart1.setNumber(0);
            shoppingCartService.removeById(shoppingCart1);
        } else {
            shoppingCart1.setNumber(number);
            shoppingCartService.updateById(shoppingCart1);
        }
        return R.success(shoppingCart1);
    }

    @DeleteMapping("/clean")
    public R<String> reduceAll() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        shoppingCartService.remove(queryWrapper);
        return R.success("清除成功");
    }
}
