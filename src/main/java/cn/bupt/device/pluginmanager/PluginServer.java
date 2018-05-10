package cn.bupt.device.pluginmanager;

import cn.bupt.device.sendEmailMethod.SendMail;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by tangjialiang on 2018/5/4.
 */
@Component
public class PluginServer implements ApplicationContextAware, InitializingBean {

    @Autowired
    SendMail sendMail;

    private String pluginInfo ;

    private PluginRegistry pluginRegistry ;

    private String states;
    @Override
    public void afterPropertiesSet() throws Exception {
        if (pluginRegistry != null) {
            pluginRegistry.register(pluginInfo,states); // 注册服务地址
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 得到所有RpcService注解的SpringBean
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Plugin.class);
        if (MapUtils.isNotEmpty(beansWithAnnotation)) {
            for (Object serviceBean :
                    beansWithAnnotation.values()) {
                Class<?> aClass = serviceBean.getClass();
                String pluginInfo = aClass.getAnnotation(Plugin.class).pluginInfo();
                String registerAddr = aClass.getAnnotation(Plugin.class).registerAddr() ;
                this.pluginInfo = pluginInfo ;
                this.states = sendMail.getState();

                pluginRegistry = new PluginRegistry(registerAddr) ;
            }
        }
    }
}
