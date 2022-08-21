package com.flab.delivery.service;

import com.flab.delivery.dao.CartDao;
import com.flab.delivery.dto.cart.CartResponseDto;
import com.flab.delivery.dto.cart.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartDao cartDao;

    public void insertItem(String userId, String storeId, ItemDto itemDto) {
        cartDao.insertItem(userId, storeId, itemDto);
    }

    public void deleteItem(String userId, String cartKey) {
        cartDao.deleteItem(userId, cartKey);
    }

    public CartResponseDto findAllItem(String userId) {
        return cartDao.findAllItem(userId);
    }


    public void increaseQuantity(String userId, String cartKey) {
        cartDao.increaseQuantity(userId, cartKey);
    }

    public void decreaseQuantity(String userId, String cartKey) {
        cartDao.decreaseQuantity(userId, cartKey);
    }

}
