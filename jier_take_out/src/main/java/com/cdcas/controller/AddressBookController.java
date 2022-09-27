package com.cdcas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cdcas.common.BaseContext;
import com.cdcas.common.R;
import com.cdcas.pojo.AddressBook;
import com.cdcas.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增用户地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<String> addressBook(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrent());
        addressBookService.save(addressBook);
        return R.success("保存成功");
    }

    /**
     * 查询所有的默认地址
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrent());
        List<AddressBook> addressBookList = addressBookService.list(queryWrapper);
        return R.success(addressBookList);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("default")
    public R<String> fault(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrent());
        queryWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(queryWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }

    /**
     * 根据id修改地址信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable String id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            return R.error("没有该信息");
        }
        return R.success(addressBook);
    }

    /**
     * 更新地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> updateAddressBook(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success("更新成功");

    }

    /**
     * 删除地址
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteAddressBook(@RequestParam List<Long> ids) {
        addressBookService.removeByIds(ids);
        return R.success("删除成功");
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrent());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

}
