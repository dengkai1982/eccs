package kaiyi.app.eccs.service;

import kaiyi.app.eccs.entity.ExtractGrantItem;
import kaiyi.app.eccs.entity.ProjectExtractGrant;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.puer.db.orm.DatabaseQuery;
import kaiyi.puer.db.orm.ServiceException;

import java.util.List;
import java.util.Set;

public interface ProjectExtractGrantService extends DatabaseQuery<ProjectExtractGrant> {
    /**
     * 新增提成发放记录
     */
    ProjectManagement newProjectExtractGrant(String projectAmountFlowId, Set<ExtractGrantItem> items)throws ServiceException;

    /**
     * 修改提成发放金额
     * @param id
     * @param items
     */
    ProjectManagement modifyExtractGranItem(String id,Set<ExtractGrantItem> items)throws ServiceException;
    /**
     * 根据收款项进行查找,杜绝一个收款项出现多个明细的情况
     * @param flowId
     * @return
     */
    ProjectExtractGrant findByProjectAmountFlow(String flowId);

    String deleteExtractGrantItem(String grantId);
}
