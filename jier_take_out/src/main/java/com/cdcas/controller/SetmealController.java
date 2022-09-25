package com.cdcas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdcas.common.R;
import com.cdcas.dto.SetmealDto;
import com.cdcas.pojo.Category;
import com.cdcas.pojo.Setmeal;
import com.cdcas.pojo.SetmealDish;
import com.cdcas.service.CategoryService;
import com.cdcas.service.SetmealDishService;
import com.cdcas.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(Integer page, Integer pageSize, String name) {
//        分页对象
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");

        //        条件查询
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        setmealService.page(setmealPage, queryWrapper);

//        对setmealPage中的内容进行更改
        setmealDtoPage.setRecords(setmealPage.getRecords().stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(setmealDto.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList()));
        return R.success(setmealDtoPage);
    }

    /**
     * 添加套餐方法
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.saveDishAndSetmeal(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 删除套餐方法
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam List<Long> ids) {
        setmealService.deleteDishAndSetmeal(ids);
        return R.success("删除成功");
    }


    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealDto(@PathVariable Long id) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, SetmealDish::getId, id);
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
        BeanUtils.copyProperties(setmeal, setmealDto);
        BeanUtils.copyProperties(setmealDishList, setmealDto);
        return R.success(setmealDto);
    }


    @PostMapping("/status/0")
    public R<String> updateStatus0(@RequestParam List<Long> ids) {
        List<Setmeal> setmealList = setmealService.getBaseMapper().selectBatchIds(ids);
        for (Setmeal setmeal : setmealList) {
            setmeal.setStatus(0);
        }
        setmealService.updateBatchById(setmealList);
        return R.success("修改成功");
    }

    @PostMapping("/status/1")
    public R<String> updateStatus1(@RequestParam List<Long> ids) {
        List<Setmeal> setmealList = setmealService.getBaseMapper().selectBatchIds(ids);
        for (Setmeal setmeal : setmealList) {
            setmeal.setStatus(1);
        }
        setmealService.updateBatchById(setmealList);

        return R.success("修改成功");
    }
}
