package com.cdcas.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdcas.common.R;
import com.cdcas.dto.DishDto;
import com.cdcas.pojo.Category;
import com.cdcas.pojo.Dish;
import com.cdcas.pojo.DishFlavor;
import com.cdcas.service.CategoryService;
import com.cdcas.service.DishFlavorService;
import com.cdcas.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品的分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(Integer page, Integer pageSize, String name) {
        //构造分页构造器对象
        Page<DishDto> dishDtoPage = new Page<>();
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getSort);

        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        //获取到所有的菜品对象
        List<Dish> dishList = pageInfo.getRecords();
        //对菜品对象进行遍历，进行更改然后赋值给list对象
        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
//            由于 1.dishDto对象是新new出来的，2.遍历的是dish对象所以需要将
//            每一个dish对象进行拷贝到dishDto对象中。
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //            根据id查询 菜品分类 对象
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
//            将所有的dishDto对象进行收集，然后转化为对象;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /**
     * 删除单个菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids) {
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }

    /**
     * 菜品启售
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> updateStatus0(@RequestParam List<Long> ids) {
        List<Dish> dish = dishService.getBaseMapper().selectBatchIds(ids);
        for (Dish dish1 : dish) {
            dish1.setStatus(1);
        }
        dishService.updateBatchById(dish);

        return R.success("状态修改成功");

    }

    /**
     * 菜品停售
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> updateStatus1(@RequestParam List<Long> ids) {
        List<Dish> dish = dishService.getBaseMapper().selectBatchIds(ids);
        for (Dish dish1 : dish) {
            dish1.setStatus(0);
        }
        dishService.updateBatchById(dish);
        return R.success("状态修改成功");
    }

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> addDishAndFlavor(@RequestBody DishDto dishDto) {
        dishService.addDishAndFlavor(dishDto);
        return R.success("新增成功");
    }


    @GetMapping("/{id}")
    public R<DishDto> getDishAndFlavor(@PathVariable Long id) {
//            获取菜品
        Dish dish= dishService.getDish(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
//            获取菜品的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, DishFlavor::getDishId, id);
        List<DishFlavor> flavor = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavor);
        log.info(String.valueOf(flavor));
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> updateDishAndFlavor(@RequestBody DishDto dishDto){
//        将dishDto对象中的数据克隆出来
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        DishFlavor dishFlavor=new DishFlavor();
        BeanUtils.copyProperties(dishDto,dishFlavor);
//        更新口味和菜品
        dishService.updateById(dish);
        dishFlavorService.updateById(dishFlavor);
        return R.success("更新成功");
    }
}
