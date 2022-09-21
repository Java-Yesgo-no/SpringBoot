package com.cdcas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdcas.common.CustomException;
import com.cdcas.mapper.CategoryMapper;
import com.cdcas.pojo.Category;
import com.cdcas.pojo.Dish;
import com.cdcas.pojo.Setmeal;
import com.cdcas.service.CategoryService;
import com.cdcas.service.DishService;
import com.cdcas.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
//        构建条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);

//        查询当前分类是否关联了菜品,如果已经关联，抛出一个业务异常
        if (dishCount > 0) {
//              已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }
//        查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if (setmealCount > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
//        正常删除分类
        super.removeById(id);
    }
}
