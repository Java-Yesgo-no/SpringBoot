package com.cdcas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdcas.common.CustomException;
import com.cdcas.dto.SetmealDto;
import com.cdcas.mapper.SetmealMapper;
import com.cdcas.pojo.Setmeal;
import com.cdcas.pojo.SetmealDish;
import com.cdcas.service.SetmealDishService;
import com.cdcas.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService{

    @Autowired
    private SetmealDishService setmealDishService;



    @Transactional
    @Override
    public void saveDishAndSetmeal(SetmealDto setmealDto) {
        //        保存套餐的基本信息，操作setmeal,执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDish=setmealDto.getSetmealDishes();
//        保存套餐和菜品的关联信息，操作Setmeal——dish，执行insert操作
        for (SetmealDish dish : setmealDish) {
            dish.setSetmealId(setmealDto.getId());
        }
//        保存套餐和菜品的关联信息，操作Setmeal——dish执行insert
        setmealDishService.saveBatch(setmealDish);
    }

    @Transactional
    @Override
    public void deleteDishAndSetmeal(List<Long> ids) {
//        在删除套餐前查看套餐是否处于在销售状态
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper=new LambdaQueryWrapper<>();
        setmealQueryWrapper.in(ids!=null,Setmeal::getId,ids);
        setmealQueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(setmealQueryWrapper);
        if (count > 0) {
            throw new CustomException("无法删除在售套餐");
        }
//      没有在售的套餐，删除
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishQueryWrapper.in(ids != null, SetmealDish::getSetmealId, ids);
        setmealDishService.remove(setmealDishQueryWrapper);
    }

    @Override
    public List<SetmealDto> getListSetmealDto(String categoryId, String status) {

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
        queryWrapper.eq(Setmeal::getStatus, status);
        queryWrapper.orderByDesc(Setmeal::getPrice).orderByDesc(Setmeal::getCreateTime);

        List<Setmeal> setmealList = this.list(queryWrapper);

        List<SetmealDto> setmealDtoList = setmealList.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            queryWrapper1.eq(SetmealDish::getSetmealId, item.getId());
            setmealDto.setSetmealDishes(setmealDishService.list(queryWrapper1));
            return setmealDto;
        }).collect(Collectors.toList());

        return setmealDtoList;

    }


}
