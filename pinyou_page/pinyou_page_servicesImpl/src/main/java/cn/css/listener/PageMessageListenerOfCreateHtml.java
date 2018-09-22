package cn.css.listener;

import cn.css.pinyou_page_services.PageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * 监听，批量生成静态页面
 */
public class PageMessageListenerOfCreateHtml implements MessageListener {
    @Autowired
    private PageService pageService;

    @Override
    public void onMessage(Message message) {
        //操作，获取消息
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();

            //根据商品的ID查询SKU（item）列表
            //导入到索引库
            for (Long goodsId : ids) {
                pageService.creataHtml(goodsId);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
