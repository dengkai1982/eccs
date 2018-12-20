package kaiyi.app.eccs.service;
import kaiyi.app.eccs.DatabaseFastOper;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.puer.db.orm.DatabaseQuery;
import kaiyi.puer.db.orm.ServiceException;

public interface VisitorUserService extends DatabaseQuery<VisitorUser>,DatabaseFastOper<VisitorUser> {
    /**
     * 用户登录
     * @param loginName
     * @param password
     */
    VisitorUser login(String loginName, String password)throws ServiceException;
    /**
     * 修改密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @throws ServiceException
     */
    void changePassword(String userId, String oldPassword, String newPassword)throws ServiceException;

}
