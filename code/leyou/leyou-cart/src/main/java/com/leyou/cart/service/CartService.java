package com.leyou.cart.service;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "leyou:cart:";

//    static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public void addCart(Cart cart) {
        // 获取登录用户
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        // 查询是否存在
//        Long skuId = cart.getSkuId();

        String key = cart.getSkuId().toString();

        Integer num = cart.getNum();
//        Boolean boo = hashOperations.hasKey(skuId.toString());

        if (hashOperations.hasKey(key)) {
            // 存在，获取购物车数据
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            // 修改购物车数量
            cart.setNum(cart.getNum() + num);

        } else {
            // 不存在，新增购物车数据
            cart.setUserId(userInfo.getId());
            // 其它商品信息，需要查询商品服务
            Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        // 将购物车数据写入redis
        hashOperations.put(key, JsonUtils.serialize(cart));
    }

    public List<Cart> queryCarts() {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getUserInfo();

        // 判断是否存在购物车
//        String key = KEY_PREFIX + user.getId();
        if(!this.redisTemplate.hasKey(KEY_PREFIX + user.getId())){
            // 不存在，直接返回
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + user.getId());
        List<Object> cartsJson = hashOperations.values();
        // 判断是否有数据
        if(CollectionUtils.isEmpty(cartsJson)){
            return null;
        }
        // 查询购物车数据
        return cartsJson.stream().map(cartJson -> JsonUtils.parse(cartJson.toString(), Cart.class)).collect(Collectors.toList());
    }

    public void updateNum(Cart cart) {
        // 获取登陆信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
//        String key = KEY_PREFIX + userInfo.getId();
        if(!this.redisTemplate.hasKey(KEY_PREFIX + userInfo.getId())){
            // 不存在，直接返回
            return ;
        }

        Integer num = cart.getNum();

        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        // 获取购物车信息
        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
        cart = JsonUtils.parse(cartJson, Cart.class);
        // 更新数量
        cart.setNum(num);
        // 写入购物车
        hashOperations.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }
}
