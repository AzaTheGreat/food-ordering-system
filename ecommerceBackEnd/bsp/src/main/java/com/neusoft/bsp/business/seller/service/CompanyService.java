package com.neusoft.bsp.business.seller.service;

import com.neusoft.bsp.business.po.Company;
import org.springframework.stereotype.Service;

@Service
public interface CompanyService {

    int insert(Company company);

    int update(Company company);

    int delete(int man_id);

    Company getById(int man_id);

    int updateUserWithLastManu(int user_id);

    int updateUserWithManuID(int man_id);
}
