package com.neusoft.bsp.business.user.mapper;

import com.neusoft.bsp.business.po.Cart;
import com.neusoft.bsp.common.base.BaseMapper;
import org.springframework.stereotype.Repository;


import java.util.List;

//@CacheConfig(cacheNames="cartCache")
@Repository
public interface CartMapper extends BaseMapper<Integer, Cart> {
//    @Cacheable()
    List<Cart> getAllById(Integer dsr_id);
}
