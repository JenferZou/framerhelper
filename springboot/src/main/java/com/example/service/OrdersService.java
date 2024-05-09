package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Goods;
import com.example.entity.Orders;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.GoodsMapper;
import com.example.mapper.OrdersMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 订单信息表业务处理
 * 这是一个名为OrdersService的服务类，主要用于处理与订单相关的业务逻辑。该类主要依赖三个Mapper：OrdersMapper、UserMapper和GoodsMapper。
 **/
/*@Service是Spring Framework中的一种注解，用于标记一个类为业务逻辑层的Service Bean。
 *当Spring应用启动时，带有@Service注解的Bean会自动创建并加入到Spring应用上下文中。*/
@Service
public class OrdersService {

    /*MyBatis的Mapper对象，用于与数据库进行交互。*/
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GoodsMapper goodsMapper;

    /*DateUtil, ObjectUtil, 和 CustomException是工具类和自定义异常类，其中可能有日期格式化、对象有效性检查和异常抛出的功能。*/
    /**
     * 【新增】
     * 添加一个订单。首先检查用户的账户余额是否足够支付订单。
     * 如果不足，抛出一个自定义异常。然后为订单生成一个唯一ID并设置订单时间为当前时间。
     * 接着将订单插入到数据库中，并从用户账户中扣除相应的金额。最后，减少商品库存。
     */
    public void add(Orders orders) {
        // 判断一下用户余额够不够
        User user = userMapper.selectById(orders.getUserId()); /*从数据库中获取与订单关联的用户。 这里的userId是从前端传过来的*/
        if (user.getAccount() < orders.getPrice()) {  /*判断用户的账户余额是否足够支付订单。如果不足，抛出一个自定义异常CustomException， 创建数据库时account默认为0，所以一定是有值的*/
            throw new CustomException(ResultCodeEnum.ACCOUNT_LOWER_ERROR); /*并传递一个枚举值ResultCodeEnum.ACCOUNT_LOWER_ERROR作为异常参数。*/
        }
        orders.setOrderId(DateUtil.format(new Date(), "yyyyMMddHHmmss")); /*orders.setOrderId - DateUtil.format(new Date(), "yyyyMMddHHmmss")为订单生成一个唯一ID。*/
        orders.setTime(DateUtil.now()); /*DateUtil.now()设置订单的时间为当前时间。*/
        ordersMapper.insert(orders); /*将订单信息插入数据库。*/

        // 扣除用户余额
        if (ObjectUtil.isNotEmpty(user)) { /*使用ObjectUtil.isNotEmpty(user)检查用户对象是否为空。如果不为空，则从用户账户中扣除订单的金额。*/
            user.setAccount(user.getAccount() - orders.getPrice());  /*余额-订单价格*/
            userMapper.updateById(user); /*数据库 更新用户账户余额。*/
        }

        // 减少商品库存
        Goods goods = goodsMapper.selectById(orders.getGoodsId()); /*goodsMapper.selectById(orders.getGoodsId())从数据库中获取与订单关联的商品。*/
        if (ObjectUtil.isNotEmpty(goods)) {  /*ObjectUtil.isNotEmpty(goods)检查商品对象是否为空。如果不为空，则减少该商品的库存数量。*/
            goods.setNum(goods.getNum() - 1);
            goodsMapper.updateById(goods);  /*更新商品库存信息。*/
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    /**
     * 批量删除
     * 它接收一个ID列表，然后使用ordersMapper的deleteById方法批量删除这些ID对应的订单。
     */
    public void deleteBatch(List<Integer> ids) { /*这是一个公共方法，名为deleteBatch，它接受一个Integer类型的列表参数ids。*/
        for (Integer id : ids) { /* 这是一个增强的for循环，用于遍历传入的ids列表。*/
            ordersMapper.deleteById(id); /*对于列表中的每一个id，这一行代码调用了ordersMapper对象的deleteById方法，并传递了该id作为参数。这意味着对于每一个ID，它都会从数据库中删除对应的订单。*/
        }
    }

    /**
     * 修改
     */
    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    /**
     * 根据ID查询
     */
    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    /**
     * 分页查询
     * 这是一个公共方法，返回类型为PageInfo<Orders>，名为selectPage。它接受三个参数：一个Orders对象、一个表示页码的Integer类型pageNum和一个表示每页大小的Integer类型pageSize。
     */
    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);  /*调用了PageHelper的startPage方法，用于设置分页参数。它告诉MyBatis从哪一页开始查询以及每页的大小。*/
        List<Orders> list = ordersMapper.selectAll(orders); /*调用了ordersMapper的selectAll方法，并传递了orders作为参数，用于查询所有满足条件的订单。查询结果存储在名为list的列表中。*/
        /*【后续】：为后台数据的显示添加一个关联查询（也可以加在xml文件里面）
         * 这个代码主要处理订单列表（list）中的每个订单（dbOrders）*/
        for (Orders dbOrders : list) { /*这是一个增强型的for循环，用于遍历订单列表（list）中的每一个订单（dbOrders）。*/
            Goods goods = goodsMapper.selectById(dbOrders.getGoodsId()); /*通过调用goodsMapper的selectById方法，根据订单中的商品ID（dbOrders.getGoodsId()）查询商品信息，并将查询结果存储在goods变量中。*/
            /*orders用来存放的字段名称要提前在实体类中定义出来*/
            if (ObjectUtil.isNotEmpty(goods)) { /*检查上一步查询到的商品信息是否为空。如果商品信息不为空，则执行以下代码块。*/
                dbOrders.setGoodsName(goods.getName()); /*将商品名称设置到当前订单对象（dbOrders）中。*/
                dbOrders.setImg(goods.getImg()); /*将商品图片链接设置到当前订单对象（dbOrders）中。*/
            }
            User user = userMapper.selectById(dbOrders.getUserId()); /*通过调用userMapper的selectById方法，根据订单中的用户ID（dbOrders.getUserId()）查询用户信息，并将查询结果存储在user变量中。*/
            if (ObjectUtil.isNotEmpty(user)) { /*检查上一步查询到的用户信息是否为空。如果用户信息不为空，则执行以下代码块。*/
                dbOrders.setUserName(user.getName()); /*将用户名称设置到当前订单对象（dbOrders）中。*/
            }
        }
        return PageInfo.of(list); /*将查询结果列表包装在一个PageInfo对象中，并返回该对象。PageInfo是一个用于分页信息的类，它包含了当前页的订单列表、总页数、总记录数等信息。*/
    }

}