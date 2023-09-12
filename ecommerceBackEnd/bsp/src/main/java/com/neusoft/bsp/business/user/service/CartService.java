package com.neusoft.bsp.business.user.service;

import com.neusoft.bsp.business.po.Cart;

import java.util.List;

public interface CartService {
    int delete(Integer wit_id);

    int insert(Cart cart);

    List<Cart> getAllById(Integer dsr_id);

    int update(Cart cart);

    Cart getById(Integer wit_id);
}
