package com.cdcas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdcas.mapper.DishFlavorMapper;
import com.cdcas.pojo.DishFlavor;
import com.cdcas.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
