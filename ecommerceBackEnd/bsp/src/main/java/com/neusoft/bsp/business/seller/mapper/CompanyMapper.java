package com.neusoft.bsp.business.seller.mapper;

import com.neusoft.bsp.business.po.Company;
import com.neusoft.bsp.common.base.BaseMapper;
import org.springframework.stereotype.Repository;

//@CacheConfig(cacheNames="companyCache")
@Repository
public interface CompanyMapper extends BaseMapper<Integer, Company> {
//    @CachePut()
    int updateUserWithLastManu(int user_id);

//    @CachePut
    int updateUserWithManuID(int man_id);
}
