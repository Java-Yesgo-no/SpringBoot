package com.cdcas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdcas.dto.DishDto;
import com.cdcas.mapper.SetmealMapper;
import com.cdcas.pojo.Dish;

public interface DishService extends IService<Dish> {

//    新增菜品，同时插入菜品对应的口味数据，需要操作两张表:dish，dish_flavor
        void addDishAndFlavor(DishDto dishDto);

//    获得菜品信息，和口味信息
        Dish getDish(Long id);
}
