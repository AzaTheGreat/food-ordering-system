package com.neusoft.bsp.business.seller.service;

import com.neusoft.bsp.business.po.PackageInfo;

public interface PackageInfoService {
    int insert(PackageInfo packageInfo);

    int update(PackageInfo packageInfo);

    int delete(int pk);

    PackageInfo getByProduct(int product_id);
}
