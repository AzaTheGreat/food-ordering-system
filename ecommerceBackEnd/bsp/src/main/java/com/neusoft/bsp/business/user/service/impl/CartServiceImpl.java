package com.neusoft.bsp.business.user.service.impl;

import com.neusoft.bsp.business.po.Cart;
import com.neusoft.bsp.business.user.mapper.CartMapper;
import com.neusoft.bsp.business.user.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("WithlistService")
public class CartServiceImpl implements CartService {

    @Autowired
    CartMapper cartMapper;

    @Override
    public int delete(Integer wit_id) {
        return cartMapper.delete(wit_id);
    }

    @Override
    public int insert(Cart cart) {
        return cartMapper.insert(cart);
    }

    @Override
    public List<Cart> getAllById(Integer dsr_id) {
        return cartMapper.getAllById(dsr_id);
    }

    @Override
    public int update(Cart cart) {
        return cartMapper.update(cart);
    }

    @Override
    public Cart getById(Integer wit_id) {
        return cartMapper.getById(wit_id);
    }
}
