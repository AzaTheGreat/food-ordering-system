package com.neusoft.bsp.business.seller.service.impl;

import com.neusoft.bsp.business.po.Company;
import com.neusoft.bsp.business.seller.mapper.CompanyMapper;
import com.neusoft.bsp.business.seller.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyMapper companyMapper;

    @Override
    public int insert(Company company) {
        return companyMapper.insert(company);
    }

    @Override
    public int update(Company company) {
        return companyMapper.update(company);
    }

    @Override
    public int delete(int man_id) {
        return companyMapper.delete(man_id);
    }

    @Override
    public Company getById(int man_id) {
        return companyMapper.getById(man_id);
    }

    @Override
    public int updateUserWithLastManu(int user_id) {
        return companyMapper.updateUserWithLastManu(user_id);
    }

    @Override
    public int updateUserWithManuID(int man_id) { return companyMapper.updateUserWithManuID(man_id); }
}
