package kaiyi.app.eccs;

import kaiyi.app.eccs.entity.VisitorMenu;
import kaiyi.app.eccs.service.VisitorMenuService;
import kaiyi.puer.commons.access.AccessControl;
import kaiyi.puer.commons.access.AccessControlReader;
import kaiyi.puer.commons.access.AccessControlScanHandler;
import kaiyi.puer.commons.collection.Cascadeable;
import kaiyi.puer.commons.collection.StreamArray;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.commons.data.StringEditor;
import kaiyi.puer.commons.exec.ExecuterQueue;
import kaiyi.puer.commons.exec.ThreadExecuterQueue;
import kaiyi.puer.commons.validate.ValidateAnnotationScanner;
import kaiyi.puer.commons.validate.ValidateReference;
import kaiyi.puer.crypt.cipher.CipherOperator;
import kaiyi.puer.db.orm.DatabaseQuery;
import kaiyi.puer.web.springmvc.SpringContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * 系统运行类库
 */
public class ApplicationRuntimeService {
    private boolean inited;

    private SpringContextHolder springContextHolder;

    private ValidateAnnotationScanner validateAnnotationScanner;

    private AccessControlReader accessControlReader;

    private ExecuterQueue executerQueue;

    private ThreadExecuterQueue threadExecuterQueue;

    private CipherOperator cipherOperator;

    private ValidateReference validateReference;

    private boolean updateMenus;

    public void setUpdateMenus(boolean updateMenus) {
        this.updateMenus = updateMenus;
    }
    //率先初始化需要通知的服务类
    private void initNotifyService(){
        Map<String, Object> obj = springContextHolder.getBeansWithAnnotation(Service.class);
        obj.forEach((k,v)->{
            if(v instanceof DatabaseQuery<?>) {
                ((DatabaseQuery<?>)v).count();
            }
        });
    }
    /**对应用进行初始化*/
    public void initApplication()throws ClassNotFoundException{
        initNotifyService();
        //初始化验证框架
        validateReference=validateAnnotationScanner.scanAnnotation();
        if(!inited&&updateMenus){
            final VisitorMenuService visitorMenuService =springContextHolder.getBean(VisitorMenuService.class);
            visitorMenuService.clearMenus();
            StreamCollection<VisitorMenu> menuList=new StreamCollection<>(new ArrayList<>());
            accessControlReader.readAccessControl(new AccessControlScanHandler() {
                @Override
                public void readClass(Class<?> aClass,String code, String name, String detail,
                                      AccessControl.Action action, float weight, String path,
                                      String parent,boolean defaultAuthor) {
                    VisitorMenu menu=new VisitorMenu(name,detail,action,path,weight, Cascadeable.START_LEVEL);
                    menu.setId(code);
                    menu.setParentId(parent);
                    menu.setDefaultAuthor(defaultAuthor);
                    menuList.add(menu);
                }
                @Override
                public void readMethod(Class<?> clz, Method method, String code, String name,
                                       String detail, AccessControl.Action action, float weight, String path, String parent,boolean defaultAuthor) {
                    if(StringEditor.isEmpty(path)){
                        //没有指定路径，尝试读取该方法上的@RequestMapping接口
                        RequestMapping clzMapping=clz.getAnnotation(RequestMapping.class);
                        String[] clzPath=clzMapping.value();
                        RequestMapping methodMapping=method.getAnnotation(RequestMapping.class);
                        String[] paths=methodMapping.value();
                        if(StreamArray.assertNotEmpty(paths)){
                            if(StreamArray.assertNotEmpty(clzPath)){
                                path=clzPath[0];
                            }
                            path+=paths[0];
                        }
                    }
                    VisitorMenu menu=new VisitorMenu(name,detail,action,path,weight, Cascadeable.START_LEVEL);
                    menu.setId(code);
                    menu.setParentId(parent);
                    menu.setDefaultAuthor(defaultAuthor);
                    menu.setActionFlag(code);
                    menuList.add(menu);
                }
            });
            menuList.sort();
            for(VisitorMenu menu:menuList){
                visitorMenuService.newMenu(menu.getId(),menu.getName(),menu.getDetail(),
                        menu.getMenuAction(),menu.getActionFlag(),menu.getWeight(),menu.getParentId(),menu.isDefaultAuthor());
            }
            inited=true;
        }
    }

    public ExecuterQueue getExecuterQueue() {
        return executerQueue;
    }

    public ThreadExecuterQueue getThreadExecuterQueue() {
        return threadExecuterQueue;
    }

    /***
     * 执行阻塞的服务层访问代码
     * @param executer
     */
    public void executeBlockCode(ServiceAccessExecuter executer){
        executer.setSpringContextHolder(springContextHolder);
        executerQueue.putCommond(executer);
    }

    /**
     * 执行异步的服务层访问代码
     * @param executer
     */
    public void executerAsyncCode(ServiceAccessExecuter executer){
        executer.setSpringContextHolder(springContextHolder);
        threadExecuterQueue.putCommand(executer);
    }
    /**
     * 获取验证器
     * @return
     */
    public ValidateReference getValidateReference() {
        return validateReference;
    }
    /**
     * 获取SpringContextHolder
     * @return
     */
    public SpringContextHolder getSpringContextHolder() {
        return springContextHolder;
    }
    /**
     * 获取Cipher
     * @return
     */
    public CipherOperator getCipherOperator() {
        return cipherOperator;
    }


    public void setCipherOperator(CipherOperator cipherOperator) {
        this.cipherOperator = cipherOperator;
    }

    public void setSpringContextHolder(SpringContextHolder springContextHolder) {
        this.springContextHolder = springContextHolder;
    }

    public void setValidateAnnotationScanner(ValidateAnnotationScanner validateAnnotationScanner) {
        this.validateAnnotationScanner = validateAnnotationScanner;
    }

    public void setAccessControlReader(AccessControlReader accessControlReader) {
        this.accessControlReader = accessControlReader;
    }

    public void setExecuterQueue(ExecuterQueue executerQueue) {
        this.executerQueue = executerQueue;
    }

    public void setThreadExecuterQueue(ThreadExecuterQueue threadExecuterQueue) {
        this.threadExecuterQueue = threadExecuterQueue;
    }
}
