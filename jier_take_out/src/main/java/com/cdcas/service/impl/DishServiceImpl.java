package com.cdcas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdcas.dto.DishDto;
import com.cdcas.mapper.DishMapper;
import com.cdcas.pojo.Dish;
import com.cdcas.pojo.DishFlavor;
import com.cdcas.service.DishFlavorService;
import com.cdcas.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    /**
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dishDto
     */
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
//    开启事务
    @Transactional
    public void addDishAndFlavor(DishDto dishDto) {
//        保存菜品的基本信息到菜品表dish
        this.save(dishDto);

//            菜品id
        Long dishId = dishDto.getId();

//            将菜品和口味进行对应
        List<DishFlavor> flavorList = dishDto.getFlavors();
        flavorList = flavorList.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
//        保存菜品口味数据到菜品口味dish_flavor
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    @Override
    public DishDto getDish(Long id) {
        //            获取菜品
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
//            获取菜品的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, DishFlavor::getDishId, id);
        List<DishFlavor> flavor = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavor);
        return dishDto;
    }

}
