package com.cdcas.dto;

import com.cdcas.pojo.Setmeal;
import com.cdcas.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
