package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.entity.Employee;
import kaiyi.app.eccs.entity.ParticipateEmployee;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.service.EmployeeService;
import kaiyi.app.eccs.service.ParticipateEmployeeService;
import kaiyi.puer.db.query.CompareQueryExpress;
import kaiyi.puer.db.query.CompareQueryExpress.Compare;
import kaiyi.puer.db.query.LinkQueryExpress;
import kaiyi.puer.db.query.LinkQueryExpress.LINK;
import kaiyi.puer.db.query.QueryExpress;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service("participateEmployeeService")
public class ParticipateEmployeeServiceImpl extends InjectDao<ParticipateEmployee> implements ParticipateEmployeeService {

    @Resource
    private EmployeeService employeeService;
    @Override
    public void employeeBind(ProjectManagement projectManagement, String employeeId){
        Employee employee=employeeService.findForPrimary(employeeId);
        QueryExpress query=new CompareQueryExpress("projectManagement",Compare.EQUAL,projectManagement);
        query=new LinkQueryExpress(query, LINK.AND,new CompareQueryExpress("employee",Compare.EQUAL,employee));
        if(exist(query)){
            em.createQuery("update "+getEntityName(entityClass)+" o set o.bindStatus=:bindStatus " +
                    ",o.updateTime=:updateTime where o.projectManagement=:projectManagement and o.employee=:employee")
                    .setParameter("bindStatus",Boolean.TRUE).setParameter("updateTime",new Date())
                    .setParameter("projectManagement",projectManagement).setParameter("employee",employee)
                    .executeUpdate();
        }else{
            ParticipateEmployee pe=new ParticipateEmployee();
            pe.setBindStatus(true);
            pe.setEmployee(employee);
            pe.setProjectManagement(projectManagement);
            saveObject(pe);
        }

    }

    @Override
    public void employeeUnbind(ProjectManagement projectManagement, Employee employee) {
        em.createQuery("update "+getEntityName(entityClass)+" o set o.bindStatus=:bindStatus , " +
                "o.updateTime=:updateTime where o.projectManagement=:projectManagement and o.employee=:employee")
                .setParameter("bindStatus",Boolean.FALSE).setParameter("updateTime",new Date())
        .setParameter("projectManagement",projectManagement).setParameter("employee",employee)
        .executeUpdate();
    }

    @Override
    public void unbindById(String id) {
        em.createQuery("update "+getEntityName(entityClass)+" o set o.bindStatus=:bindStatus " +
                "where o.id=:id").setParameter("bindStatus",Boolean.FALSE).setParameter("id",id)
                .executeUpdate();
    }

}
