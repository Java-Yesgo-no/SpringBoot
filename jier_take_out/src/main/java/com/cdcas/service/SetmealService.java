package com.cdcas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdcas.dto.SetmealDto;
import com.cdcas.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void saveDishAndSetmeal(SetmealDto setmealDto);

    void deleteDishAndSetmeal(List<Long> ids);
}
