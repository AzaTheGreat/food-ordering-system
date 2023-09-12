package com.neusoft.bsp.business.seller.mapper;

import com.neusoft.bsp.common.base.BaseMapper;
import com.neusoft.bsp.business.po.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

//@CacheConfig(cacheNames="productCache")
@Repository
public interface ProductMapper extends BaseMapper<Integer, Product> {
//    @CachePut()
    int updateSts(Product product);

//    @Cacheable()
    List<Product> getProductOnShelf();
}
