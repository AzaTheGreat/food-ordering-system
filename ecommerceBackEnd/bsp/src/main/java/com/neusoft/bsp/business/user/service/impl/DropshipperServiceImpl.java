package com.neusoft.bsp.business.user.service.impl;

import com.neusoft.bsp.admin.user.po.User;
import com.neusoft.bsp.business.user.mapper.DropshipperMapper;
import com.neusoft.bsp.business.user.service.DropshipperService;
import com.neusoft.bsp.business.po.Dropshipper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service("DropshipperService")
public class DropshipperServiceImpl implements DropshipperService {
    @Autowired
    DropshipperMapper dropshipperMapper;

    @Override
    public int insert(Dropshipper store) {
        return dropshipperMapper.insert(store);
    }

    @Override
    public int update(Dropshipper store) {
        return dropshipperMapper.update(store);
    }

    @Override
    public int delete(int str_id) {
        return dropshipperMapper.delete(str_id);
    }

    @Override
    public Dropshipper getById(int str_id) {
        return dropshipperMapper.getById(str_id);
    }

    @Override
    public int addDropshipper(User user) {
        Dropshipper dropshipper = new Dropshipper();
        dropshipper.setName(user.getName());
        long time = System.currentTimeMillis();
        Date datetime = new java.sql.Date(time);
        dropshipper.setRegister_date(datetime);
        dropshipperMapper.insert(dropshipper);
        return dropshipper.getDsr_id();
    }
}
