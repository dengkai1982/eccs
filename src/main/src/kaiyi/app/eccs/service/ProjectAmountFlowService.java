package kaiyi.app.eccs.service;

import kaiyi.app.eccs.entity.ProjectAmountFlow;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.puer.db.orm.DatabaseQuery;

import java.util.Date;

public interface ProjectAmountFlowService extends DatabaseQuery<ProjectAmountFlow> {

    void addFlow(ProjectManagement projectManagement,int amount,
                 VisitorUser operMan, String remark, Date receivablesTime);

    void modify(String entityId,int amount,VisitorUser operMan, String remark, Date receivablesTime);

    void deleteForId(String flowId);
}
