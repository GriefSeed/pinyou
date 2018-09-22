package cn.css.listener;

import cn.css.pinyou_page_services.PageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * 商品下架，批量移除静态页面
 */
public class PageMessageListenerOfDelHtml implements MessageListener {

    @Autowired
    private PageService pageService;

    @Override
    public void onMessage(Message message) {
        // 将接收到的消息进行强制转换
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            for (Long goodsId : ids) {
                pageService.delHtml(goodsId);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


}
