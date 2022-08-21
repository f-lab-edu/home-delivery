package com.flab.delivery.dao;

import com.flab.delivery.dto.cart.ItemDto;
import com.flab.delivery.dto.cart.CartResponseDto;
import com.flab.delivery.enums.CartType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class CartDao {

    private final RedisTemplate<String, Object> redisCartTemplate;
    private final String STORE = "STORE_ID";

    public void insertItem(String userId, String storeId, ItemDto itemDto) {
        CartType cartType = checkCartType(userId, storeId);
        String hashKey = itemDto.getCartKey();

        switch (cartType) {
            case DIFF:
                redisCartTemplate.delete(userId);
                redisCartTemplate.opsForHash().delete(STORE, userId);
            case FIRST:
                redisCartTemplate.opsForHash().put(STORE, userId, storeId);
                redisCartTemplate.opsForHash().put(userId, hashKey, itemDto);
                break;
            case EQUAL:
                Object getItemDto = redisCartTemplate.opsForHash().get(userId, hashKey);
                if (getItemDto == null) {
                    redisCartTemplate.opsForHash().put(userId, hashKey, itemDto);
                } else {
                    ItemDto getItemDtoToDto = (ItemDto) getItemDto;
                    getItemDtoToDto.increaseQuantity();
                    redisCartTemplate.opsForHash().put(userId, hashKey, getItemDtoToDto);
                }
                break;
        }
    }


    private CartType checkCartType(String userId, String storeId) {
        Object findStoreId = redisCartTemplate.opsForHash().get(STORE, userId);
        if (findStoreId != null) {
            String findStoreIdToString = (String) findStoreId;
            if (!findStoreIdToString.equals(storeId)) {
                return CartType.DIFF;
            }
            return CartType.EQUAL;
        }
        return CartType.FIRST;
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

    /**
     * Type을 받아서 PLUS, MINUS에 따라 처리하는게 더 괜찮은 코드가 더 괜찮을지?
     */
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
