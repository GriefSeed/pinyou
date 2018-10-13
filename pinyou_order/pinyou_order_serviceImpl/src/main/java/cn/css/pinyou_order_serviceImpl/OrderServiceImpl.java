package cn.css.pinyou_order_serviceImpl;

import cn.css.pinyou_common.IdWorker;
import cn.css.pinyou_dto.Cart;
import cn.css.pinyou_dto.PageResult;
import cn.css.pinyou_mapper.TbOrderItemMapper;
import cn.css.pinyou_mapper.TbOrderMapper;
import cn.css.pinyou_mapper.TbPayLogMapper;
import cn.css.pinyou_order_service.OrderService;
import cn.css.pinyou_pojo.domain.TbOrder;
import cn.css.pinyou_pojo.domain.TbOrderExample;
import cn.css.pinyou_pojo.domain.TbOrderItem;
import cn.css.pinyou_pojo.domain.TbPayLog;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbOrder> findAll() {
        return orderMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbOrder> page = (Page<TbOrder>) orderMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbPayLogMapper payLogMapper;

    /**
     * 增加
     */
    @Override
    public void add(TbOrder order) {

        //1、根据order中的userID（当前登录者名）获取Redis中的购物车集合数据
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());

        //本次支付总金额，提供给微信支付
        double totalFee = 0;
        //订单id集合字符串，提供给支付日志payLog
        String orderIds = "";

        //2、遍历集合数据：目的：创建订单
        for (Cart cart : cartList) {

            //3、创建订单Order
            TbOrder tbOrder = new TbOrder();
            //用雪花算法ID生成的64位的ID
            long orderId = idWorker.nextId();
            tbOrder.setOrderId(orderId);
            tbOrder.setStatus("1");
            tbOrder.setCreateTime(new Date());
            tbOrder.setUpdateTime(new Date());
            tbOrder.setSellerId(cart.getSellerId());
            tbOrder.setPaymentType(order.getPaymentType());
            tbOrder.setUserId(order.getUserId());
            tbOrder.setReceiverAreaName(order.getReceiverAreaName());
            tbOrder.setReceiver(order.getReceiver());
            tbOrder.setReceiverMobile(order.getReceiverMobile());
            tbOrder.setSourceType(order.getSourceType());

            //定义一个变量来合计商品明细列表中总结
            double money = 0;
            //获取购物车对象对应的商品明细列表
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            for (TbOrderItem orderItem : orderItemList) {
                //此时缺少ID
                orderItem.setId(idWorker.nextId());
                orderItem.setOrderId(orderId);
                orderItem.setSellerId(cart.getSellerId());
                //计算总价格
                money += orderItem.getTotalFee().doubleValue();

                orderItemMapper.insertSelective(orderItem);
            }
            //4、插入订单及订单商品列表
            //实际付款金额
            tbOrder.setPayment(new BigDecimal(money));

            //累计总金额
            totalFee += money;
            // 从第一次开始加上订单ID，存字符串
            if (orderIds.length() > 0) {
                orderIds += "," + orderId;
            } else {
                orderIds = orderId + "";
            }

            orderMapper.insertSelective(tbOrder);
        }

        //如果是微信支付的话；生成支付日志
        if ("1".equals(order.getPaymentType())) {
            TbPayLog payLog = new TbPayLog();
            payLog.setOutTradeNo(idWorker.nextId() + "");
            //未支付
            payLog.setTradeState("0");
            payLog.setPayType(order.getPaymentType());
            payLog.setCreateTime(new Date());
            payLog.setUserId(order.getUserId());
            //本次支付总金额
            payLog.setTotalFee((long) (totalFee * 100));
            //本次有哪些订单，使用逗号隔开
            payLog.setOrderList(orderIds);

            //保存到数据库
            payLogMapper.insertSelective(payLog);

            //存入redis
            redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);

        }

    }


    /**
     * 修改
     */
    @Override
    public void update(TbOrder order) {
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbOrder findOne(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            orderMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbOrderExample example = new TbOrderExample();
        TbOrderExample.Criteria criteria = example.createCriteria();

        if (order != null) {
            if (order.getPaymentType() != null && order.getPaymentType().length() > 0) {
                criteria.andPaymentTypeLike("%" + order.getPaymentType() + "%");
            }
            if (order.getPostFee() != null && order.getPostFee().length() > 0) {
                criteria.andPostFeeLike("%" + order.getPostFee() + "%");
            }
            if (order.getStatus() != null && order.getStatus().length() > 0) {
                criteria.andStatusLike("%" + order.getStatus() + "%");
            }
            if (order.getShippingName() != null && order.getShippingName().length() > 0) {
                criteria.andShippingNameLike("%" + order.getShippingName() + "%");
            }
            if (order.getShippingCode() != null && order.getShippingCode().length() > 0) {
                criteria.andShippingCodeLike("%" + order.getShippingCode() + "%");
            }
            if (order.getUserId() != null && order.getUserId().length() > 0) {
                criteria.andUserIdLike("%" + order.getUserId() + "%");
            }
            if (order.getBuyerMessage() != null && order.getBuyerMessage().length() > 0) {
                criteria.andBuyerMessageLike("%" + order.getBuyerMessage() + "%");
            }
            if (order.getBuyerNick() != null && order.getBuyerNick().length() > 0) {
                criteria.andBuyerNickLike("%" + order.getBuyerNick() + "%");
            }
            if (order.getBuyerRate() != null && order.getBuyerRate().length() > 0) {
                criteria.andBuyerRateLike("%" + order.getBuyerRate() + "%");
            }
            if (order.getReceiverAreaName() != null && order.getReceiverAreaName().length() > 0) {
                criteria.andReceiverAreaNameLike("%" + order.getReceiverAreaName() + "%");
            }
            if (order.getReceiverMobile() != null && order.getReceiverMobile().length() > 0) {
                criteria.andReceiverMobileLike("%" + order.getReceiverMobile() + "%");
            }
            if (order.getReceiverZipCode() != null && order.getReceiverZipCode().length() > 0) {
                criteria.andReceiverZipCodeLike("%" + order.getReceiverZipCode() + "%");
            }
            if (order.getReceiver() != null && order.getReceiver().length() > 0) {
                criteria.andReceiverLike("%" + order.getReceiver() + "%");
            }
            if (order.getInvoiceType() != null && order.getInvoiceType().length() > 0) {
                criteria.andInvoiceTypeLike("%" + order.getInvoiceType() + "%");
            }
            if (order.getSourceType() != null && order.getSourceType().length() > 0) {
                criteria.andSourceTypeLike("%" + order.getSourceType() + "%");
            }
            if (order.getSellerId() != null && order.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + order.getSellerId() + "%");
            }

        }

        Page<TbOrder> page = (Page<TbOrder>) orderMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }


    @Override
    public TbPayLog findPayLogInRedis(String userId) {
        return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
    }

    @Override
    public void updateOrderStatus(String out_trade_no, String transaction_id) {
        //1、查询支付日志
        TbPayLog payLog = payLogMapper.selectByPrimaryKey(out_trade_no);

        //2、更新支付日志
        //已支付
        payLog.setTradeState("1");
        payLog.setPayTime(new Date());
        payLog.setTransactionId(transaction_id);
        payLogMapper.updateByPrimaryKeySelective(payLog);

        //3、更新所有订单的支付状态为已支付
        //获取所有订单的订单号集合
        String[] orderIds = payLog.getOrderList().split(",");
        for (String orderId : orderIds) {
            TbOrder tbOrder = new TbOrder();
            //已支付
            tbOrder.setStatus("2");
            tbOrder.setPaymentTime(new Date());
            tbOrder.setUpdateTime(new Date());
            tbOrder.setOrderId(Long.parseLong(orderId));

            orderMapper.updateByPrimaryKeySelective(tbOrder);
        }

        //4、删除redis中的支付日志
        redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
    }
}
