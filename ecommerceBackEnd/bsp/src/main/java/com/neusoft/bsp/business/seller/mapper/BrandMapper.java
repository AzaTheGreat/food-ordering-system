package com.neusoft.bsp.business.seller.mapper;

import com.neusoft.bsp.business.po.Brand;
import com.neusoft.bsp.common.base.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//@CacheConfig(cacheNames="brandCache")
@Repository
public interface BrandMapper extends BaseMapper<Integer, Brand> {
//    @Cacheable()
    List<Brand> getAllById(Integer man_id);

}
