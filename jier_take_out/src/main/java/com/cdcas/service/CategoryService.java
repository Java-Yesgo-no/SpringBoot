package com.cdcas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdcas.pojo.Category;

public interface CategoryService extends IService<Category>  {
    void remove(Long id);
}
