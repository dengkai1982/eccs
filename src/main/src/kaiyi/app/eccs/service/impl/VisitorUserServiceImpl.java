package kaiyi.app.eccs.service.impl;
import kaiyi.app.eccs.ApplicationRuntimeService;
import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.ServiceExceptionDefine;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.app.eccs.service.VisitorUserService;
import kaiyi.puer.commons.data.StringEditor;
import kaiyi.puer.db.orm.ServiceException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;


@Service("visitorUserService")
public class VisitorUserServiceImpl extends InjectDao<VisitorUser> implements VisitorUserService {
    private static final long serialVersionUID = 3356637804771575233L;

    @Resource
    private ApplicationRuntimeService applicationRuntimeService;

    @Override
    protected void objectBeforePersistHandler(VisitorUser visitorUser) throws ServiceException {
        String password=visitorUser.getPassword();
        if(StringEditor.notEmpty(password)){
            visitorUser.setPassword(applicationRuntimeService.getCipherOperator().encodeToBase64(password));
        }
    }

    /**
     * 用户登录
     * @param loginName
     * @param password
     */
    public VisitorUser login(String loginName, String password)throws ServiceException{
        VisitorUser user=signleQuery("loginName",loginName);
        if(Objects.isNull(user)||!user.isEnable()){
            throw ServiceExceptionDefine.loginError;
        }

        if(!applicationRuntimeService.getCipherOperator().check(password,user.getPassword())){
            throw ServiceExceptionDefine.loginError;
        }
        user.setLastLoginTime(new Date());
        user.setAccessNumber(user.getAccessNumber()+1);
        updateObject(user);
        return user;
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) throws ServiceException {
        VisitorUser user=findForPrimary(userId);
        if(Objects.isNull(user)){
            throw ServiceExceptionDefine.userNotExist;
        }
        if(!applicationRuntimeService.getCipherOperator().check(oldPassword,user.getPassword())){
            throw ServiceExceptionDefine.oldPasswordError;
        }
        user.setPassword(applicationRuntimeService.getCipherOperator().encodeToBase64(newPassword));
    }
}
