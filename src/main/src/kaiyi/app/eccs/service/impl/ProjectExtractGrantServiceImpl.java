package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.ServiceExceptionDefine;
import kaiyi.app.eccs.entity.ExtractGrantItem;
import kaiyi.app.eccs.entity.ProjectAmountFlow;
import kaiyi.app.eccs.entity.ProjectExtractGrant;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.service.ExtractGrantItemService;
import kaiyi.app.eccs.service.ProjectAmountFlowService;
import kaiyi.app.eccs.service.ProjectExtractGrantService;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.db.orm.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service("projectExtractGrantService")
public class ProjectExtractGrantServiceImpl extends InjectDao<ProjectExtractGrant> implements ProjectExtractGrantService {
    private static final long serialVersionUID = 1520709186006839565L;
    @Resource
    private ProjectAmountFlowService projectAmountFlowService;
    @Resource
    private ExtractGrantItemService extractGrantItemService;
    @Override
    public ProjectManagement newProjectExtractGrant(String projectAmountFlowId, Set<ExtractGrantItem> items) throws ServiceException {
        if(findByProjectAmountFlow(projectAmountFlowId)!=null){
            throw ServiceExceptionDefine.extractGrantExist;
        }
        ProjectAmountFlow amountFlow=projectAmountFlowService.findForPrimary(projectAmountFlowId);
        if(amountFlow==null){
            throw ServiceExceptionDefine.emptyAmountFlow;
        }else if(amountFlow.isDeleted()){
            throw ServiceExceptionDefine.emptyAmountFlow;
        }
        ProjectExtractGrant grant=new ProjectExtractGrant();
        grant.setProjectManagement(amountFlow.getProjectManagement());
        grant.setProjectAmountFlow(amountFlow);
        int totalGrantAmount=0;
        Date date=new Date();
        for(ExtractGrantItem item:items){
            item.setExtractGrant(grant);
            item.setId(randomIdentifier());
            item.setCreateTime(date);
            item.setUpdateTime(date);
            totalGrantAmount+=item.getAmount();
        }
        grant.setTotalGrantAmount(totalGrantAmount);
        grant.setItems(items);
        saveObject(grant);
        return amountFlow.getProjectManagement();
    }

    @Override
    public ProjectManagement modifyExtractGranItem(String id, Set<ExtractGrantItem> items)throws ServiceException {
        ProjectExtractGrant grant=findForPrimary(id);
        if(grant==null){
            throw ServiceExceptionDefine.emptyAmountFlow;
        }
        Set<ExtractGrantItem> grantItems=grant.getItems();
        int totalGrantAmount=0;
        StreamCollection<ExtractGrantItem> streamItems=new StreamCollection<>(grantItems);
        for(ExtractGrantItem grantItem:items){
            ExtractGrantItem item=streamItems.find(i->{
               return i.getEmployee().getId().equals(grantItem.getEmployee().getId());
            });
            Date date=new Date();
            if(item==null){
                grantItem.setId(randomIdentifier());
                grantItem.setExtractGrant(grant);
                grantItem.setId(randomIdentifier());
                grantItem.setCreateTime(date);
                grantItem.setUpdateTime(date);
                grantItems.add(grantItem);
            }else{
                item.setAmount(grantItem.getAmount());
                item.setUpdateTime(date);
            }
            totalGrantAmount+=grantItem.getAmount();
        }
        grant.setTotalGrantAmount(totalGrantAmount);
        grant.setItems(grantItems);
        return grant.getProjectManagement();
    }

    @Override
    public ProjectExtractGrant findByProjectAmountFlow(String flowId) {
        ProjectAmountFlow flow=new ProjectAmountFlow();
        flow.setId(flowId);
        return signleQuery("projectAmountFlow",flow);
    }

    @Override
    public String deleteExtractGrantItem(String grantId) {
        ProjectExtractGrant grant=findForPrimary(grantId);
        String projectManagementId=grant.getProjectManagement().getId();
        if(grant!=null){
            grant.getProjectManagement();
            Set<ExtractGrantItem> items=grant.getItems();
            int amount=0;
            for(ExtractGrantItem ite:items){
                amount+=ite.getAmount();
            }
            if(amount>0){
                grant.setTotalGrantAmount(amount);
            }else{
                deleteForPrimary(grantId);
            }
        }
        return projectManagementId;
    }
}
