package kaiyi.app.eccs.entity;

import kaiyi.puer.commons.bean.BeanSyntacticSugar;
import kaiyi.puer.db.entity.TimeStampEntity;
import kaiyi.puer.json.JsonCreator;
import kaiyi.puer.json.JsonValuePolicy;
import kaiyi.puer.json.creator.ObjectJsonCreator;

import javax.persistence.*;

@Entity(name=ParticipateEmployee.TABLE_NAME)
public class ParticipateEmployee extends TimeStampEntity {
    public static final String TABLE_NAME="participate_employee";
    private static final long serialVersionUID = 5201321949605944538L;
    //参与员工
    private Employee employee;
    //工程项目
    private ProjectManagement projectManagement;
    //绑定状态
    private boolean bindStatus;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name="employee")
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name="projectManagement")
    public ProjectManagement getProjectManagement() {
        return projectManagement;
    }

    public void setProjectManagement(ProjectManagement projectManagement) {
        this.projectManagement = projectManagement;
    }

    @Override
    protected JsonValuePolicy convertJsonValuePolicy() {
        return new JsonValuePolicy<ParticipateEmployee>() {
            @Override
            public JsonCreator getCreator(ParticipateEmployee entity, String field, Object fieldValue) {
                if(field.equals("employee")){
                    Employee employee=entity.getEmployee();
                    return new ObjectJsonCreator<Employee>(employee,
                            BeanSyntacticSugar.getFieldString(Employee.class,employee.fieldFilter()),
                            employee.jsonFieldReplacePolicy(),employee.jsonValuePolicy());
                }else if(field.equals("projectManagement")){
                    ProjectManagement projectManagement=entity.getProjectManagement();
                    return new ObjectJsonCreator<ProjectManagement>(projectManagement,
                            BeanSyntacticSugar.getFieldString(ProjectManagement.class,projectManagement.fieldFilter()),
                            projectManagement.jsonFieldReplacePolicy(),projectManagement.jsonValuePolicy());
                }
                return null;
            }
        };
    }

    public boolean isBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(boolean bindStatus) {
        this.bindStatus = bindStatus;
    }
}
