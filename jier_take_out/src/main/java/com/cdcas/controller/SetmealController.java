package com.cdcas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdcas.common.R;
import com.cdcas.dto.SetmealDto;
import com.cdcas.pojo.Category;
import com.cdcas.pojo.Dish;
import com.cdcas.pojo.Setmeal;
import com.cdcas.service.CategoryService;
import com.cdcas.service.DishService;
import com.cdcas.service.SetmealService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(Integer page,Integer pageSize,String name){
//        分页对象
        Page<Setmeal> setmealPage=new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage=new Page<>(page,pageSize);
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        //        条件查询
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        setmealService.page(setmealPage,queryWrapper);

//        对setmealPage中的内容进行更改
        setmealDtoPage.setRecords(setmealPage.getRecords().stream().map((item)->{
                    SetmealDto setmealDto = new SetmealDto();
                    BeanUtils.copyProperties(item, setmealDto);
                    Category category = categoryService.getById(setmealDto.getCategoryId());
                    setmealDto.setCategoryName(category.getName());
                    return setmealDto;
        }).collect(Collectors.toList()));
        return R.success(setmealDtoPage);
    }

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        List<Dish> dishList=new ArrayList<>();
        BeanUtils.copyProperties(setmealDto,dishList);
        setmealService.save(setmeal);
        dishService.saveBatch(dishList);
        return R.success("添加成功");
    }


}
