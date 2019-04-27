package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.entity.Employee;
import kaiyi.app.eccs.entity.ExtractGrantItem;
import kaiyi.app.eccs.entity.ProjectExtractGrant;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.service.ExtractGrantItemService;
import kaiyi.app.eccs.service.ProjectExtractGrantService;
import kaiyi.puer.commons.data.Currency;
import kaiyi.puer.commons.data.StringEditor;
import kaiyi.puer.db.query.CompareQueryExpress;
import kaiyi.puer.db.query.LinkQueryExpress;
import kaiyi.puer.db.query.NullQueryExpress;
import kaiyi.puer.db.query.QueryExpress;
import kaiyi.puer.web.QueryTransfor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.util.Objects;

@Service("extractGrantItemService")
public class ExtractGrantItemServiceImpl extends InjectDao<ExtractGrantItem> implements ExtractGrantItemService {
    private static final long serialVersionUID = 1859883909851693458L;
    @Resource
    private ProjectExtractGrantService projectExtractGrantService;
    @Override
    public String deleteById(String id) {
        ExtractGrantItem item=findForPrimary(id);
        if(Objects.nonNull(item)){
            String grantId=item.getExtractGrant().getId();
            em.createQuery("delete from "+getEntityName(entityClass)+" o where o.id=:id")
                    .setParameter("id",id).executeUpdate();
            return grantId;
        }
        return "";
    }

    @Override
    public String totalCommission(String employeeId, String projectManagementId) {
        QueryExpress query=new NullQueryExpress("id",NullQueryExpress.NullCondition.IS_NOT_NULL);
        if(StringEditor.notEmpty(projectManagementId)){
            ProjectManagement projectManagement=new ProjectManagement();
            projectManagement.setId(projectManagementId);
            ProjectExtractGrant grant=projectExtractGrantService.signleQuery("projectManagement",projectManagement);
            if(grant!=null){
                query=new LinkQueryExpress(query, LinkQueryExpress.LINK.AND,new CompareQueryExpress("extractGrant",
                        CompareQueryExpress.Compare.EQUAL,grant));
            }
        }
        if(StringEditor.notEmpty(employeeId)){
            Employee employee=new Employee();
            employee.setId(employeeId);
            query=new LinkQueryExpress(query, LinkQueryExpress.LINK.AND,new CompareQueryExpress("employee",
                    CompareQueryExpress.Compare.EQUAL,employee));
        }
        Query q=em.createQuery("select COALESCE(SUM(o.amount),0) from "+getEntityName(entityClass)+" o " +
                "where "+query.build());
        query.setParameter(q);
        Long amount=(Long)q.getSingleResult();
        return Currency.parseForNoDecimalPoint(amount).toString();
    }
}
