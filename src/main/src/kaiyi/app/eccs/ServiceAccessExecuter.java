package kaiyi.app.eccs;

import kaiyi.puer.commons.exec.CodeExecuter;
import kaiyi.puer.web.springmvc.SpringContextHolder;
/**
 * 需要访问服务层的代码执行器
 */
public abstract class ServiceAccessExecuter implements CodeExecuter {
    protected SpringContextHolder springContextHolder;

    protected void setSpringContextHolder(SpringContextHolder springContextHolder) {
        this.springContextHolder = springContextHolder;
    }
}
