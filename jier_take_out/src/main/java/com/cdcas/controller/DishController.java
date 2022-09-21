package com.cdcas.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdcas.common.R;
import com.cdcas.pojo.Dish;
import com.cdcas.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
     private DishService dishService;

    /**
     * 菜品的分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<Dish>> page(Integer page,Integer pageSize){
        Page<Dish> pageInfo=new Page<>(page,pageSize);

        dishService.page(pageInfo);
        return R.success(pageInfo);
    }

    /**
     * 删除单个菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids){
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }

    /**
     * 菜品启售
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> updateStatus0(@RequestParam List<Long> ids){
        List<Dish> dish=dishService.getBaseMapper().selectBatchIds(ids);
        for (Dish dish1 : dish) {
            dish1.setStatus(1);
        }
        dishService.updateBatchById(dish);

        return R.success("状态修改成功");

    }


    /**
     * 菜品停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> updateStatus1(@RequestParam List<Long> ids){
        List<Dish> dish=dishService.getBaseMapper().selectBatchIds(ids);
        for (Dish dish1 : dish) {
            dish1.setStatus(0);
        }
        dishService.updateBatchById(dish);
        return R.success("状态修改成功");
    }


}
