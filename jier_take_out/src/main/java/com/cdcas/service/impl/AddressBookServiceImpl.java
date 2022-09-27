package com.cdcas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdcas.mapper.AddressBookMapper;
import com.cdcas.pojo.AddressBook;
import com.cdcas.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
