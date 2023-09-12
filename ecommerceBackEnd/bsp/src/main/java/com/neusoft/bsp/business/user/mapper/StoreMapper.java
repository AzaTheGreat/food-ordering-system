package com.neusoft.bsp.business.user.mapper;

import com.neusoft.bsp.business.po.Store;
import com.neusoft.bsp.common.base.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//@CacheConfig(cacheNames="storeCache")
@Repository
public interface StoreMapper extends BaseMapper<Integer, Store> {
//    @Cacheable()
    List<Store> getAllByDsrId(int dsr_id);
}
