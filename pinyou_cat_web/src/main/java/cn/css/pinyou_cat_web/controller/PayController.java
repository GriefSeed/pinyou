package cn.css.pinyou_cat_web.controller;

import cn.css.pinyou_dto.Result;
import cn.css.pinyou_order_service.OrderService;
import cn.css.pinyou_pay_service.WeixinPayService;
import cn.css.pinyou_pojo.domain.TbPayLog;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/pay")
@RestController
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private OrderService orderService;

    /**
     * 到支付系统获取支付二维码等信息
     *
     * @return
     */
    @RequestMapping(value = "/createNative", method = RequestMethod.GET)
    public Map<String, String> createNative() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //1、获取订单号
        TbPayLog payLog = orderService.findPayLogInRedis(userId);

        //IdWorker idWorker = new IdWorker();
        //String out_trade_no = idWorker.nextId() + "";
        String out_trade_no = payLog.getOutTradeNo();

        //2、总金额；单位为分
        String total_fee = payLog.getTotalFee().toString();

        //3、调用支付系统方法获取支付二维码地址
        return weixinPayService.createNative(out_trade_no, total_fee);
    }

    /**
     * 根据订单号查询该订单的支付状态
     *
     * @param out_trade_no 订单号
     * @return 查询结果
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        Result result = new Result(false, "查询支付状态失败");

        //支付二维码超时时间；5分钟
        int count = 0;
        while (true) {

            //到支付系统查询支付状态
            Map<String, String> resultMap = weixinPayService.queryPayStatus(out_trade_no);
            if (resultMap == null) {
                break;
            }

            if ("SUCCESS".equals(resultMap.get("trade_state"))) {
                //支付成功
                result = new Result(true, "支付成功");

                //更新订单状态；参数2:微信订单号
                orderService.updateOrderStatus(out_trade_no, resultMap.get("transaction_id"));

                break;
            }

            count++;
            if (count > 100) {
                result = new Result(false, "支付二维码超时");
                break;
            }

            try {
                //每隔3秒查询
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        return result;
    }
}
