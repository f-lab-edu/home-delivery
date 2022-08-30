package com.flab.delivery.dao;

import com.flab.delivery.dto.cart.ItemDto;
import com.flab.delivery.dto.cart.CartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class CartDao {

    private final RedisTemplate<String, Object> redisCartTemplate;
    private final String STORE = "STORE_ID";

    public void insertItem(String userId, String storeId, ItemDto itemDto) {

        String hashKey = itemDto.getCartKey();
        String existingStoreId = (String) redisCartTemplate.opsForHash().get(STORE, userId);

        if (existingStoreId == null) {
            redisCartTemplate.opsForHash().put(STORE, userId, storeId);
            redisCartTemplate.opsForHash().put(userId, hashKey, itemDto);
        } else {
            if (existingStoreId.equals(storeId)) {
                ItemDto cartItem = (ItemDto) redisCartTemplate.opsForHash().get(userId, hashKey);
                if (cartItem == null) {
                    redisCartTemplate.opsForHash().put(userId, hashKey, itemDto);
                } else {
                    cartItem.increaseQuantity();
                    redisCartTemplate.opsForHash().put(userId, hashKey, cartItem);
                }
            } else {
                redisCartTemplate.delete(userId);
                redisCartTemplate.opsForHash().delete(STORE, userId);
                redisCartTemplate.opsForHash().put(STORE, userId, storeId);
                redisCartTemplate.opsForHash().put(userId, hashKey, itemDto);
            }
        }
    }

    public void deleteItem(String userId, String cartKey) {
        redisCartTemplate.opsForHash().delete(userId, cartKey);
        if (redisCartTemplate.opsForHash().entries(userId).size() == 0) {
            RedisSerializer keySerializer = redisCartTemplate.getStringSerializer();
            RedisSerializer hashKeySerializer = redisCartTemplate.getHashKeySerializer();

            redisCartTemplate.executePipelined(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.del(keySerializer.serialize(userId));
                    connection.hDel(keySerializer.serialize(STORE), hashKeySerializer.serialize(userId));
                    return null;
                }
            });

        }
    }

    public CartResponseDto findAllItem(String userId) {
        Object getStoreId = redisCartTemplate.opsForHash().get(STORE, userId);
        List<ItemDto> cartList = new ArrayList<>();

        if (getStoreId == null) {
            return new CartResponseDto(null, cartList);
        }
        String storeIdToString = (String) getStoreId;
        Collection<Object> values = redisCartTemplate.opsForHash().entries(userId).values();
        for (Object val : values) {
            ItemDto cart = (ItemDto) val;
            cartList.add(cart);
        }

        Long responseStoreId = new Long(storeIdToString);
        return new CartResponseDto(responseStoreId, cartList);
    }


    public void increaseQuantity(String userId, String cartKey) {
        Object getItem = redisCartTemplate.opsForHash().get(userId, cartKey);
        if (getItem == null) {
            return;
        }
        ItemDto item = (ItemDto) getItem;
        item.increaseQuantity();
        redisCartTemplate.opsForHash().put(userId, cartKey, item);
    }

    public void decreaseQuantity(String userId, String cartKey) {
        Object getItem = redisCartTemplate.opsForHash().get(userId, cartKey);
        if (getItem == null) {
            return;
        }
        ItemDto item = (ItemDto) getItem;
        item.decreaseQuantity();
        redisCartTemplate.opsForHash().put(userId, cartKey, item);
    }


}
