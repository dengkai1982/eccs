package kaiyi.app.eccs.service;

import kaiyi.app.eccs.DatabaseFastOper;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.puer.db.orm.DatabaseQuery;
import kaiyi.puer.db.orm.ServiceException;

public interface ProjectManagementService extends DatabaseQuery<ProjectManagement> ,DatabaseFastOper<ProjectManagement> {
    /**
     * 合同资金入账
     * @param id
     * @param amount
     * @throws ServiceException
     */
    void contractAdmission(String id, int amount, VisitorUser operMan,String remark)throws ServiceException;
    /**
     * 移除资金入账记录
     * @param flowId
     */
    void deleteAdmission(String flowId,VisitorUser operMan)throws ServiceException;
    /**
     * 加入工程参与人员
     * @param projectId
     * @param employeeId
     * @param  isJoin true 加入
     */
    void employeeJoin(String projectId,String employeeId,boolean isJoin)throws ServiceException;

}
