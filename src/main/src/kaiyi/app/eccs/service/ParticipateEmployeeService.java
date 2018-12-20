package kaiyi.app.eccs.service;

import kaiyi.app.eccs.entity.Employee;
import kaiyi.app.eccs.entity.ParticipateEmployee;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.puer.db.orm.DatabaseQuery;

public interface ParticipateEmployeeService extends DatabaseQuery<ParticipateEmployee> {
    /**绑定工程参与人员*/
    void employeeBind(ProjectManagement projectManagement, String employeeId);
    /**解除工程人员绑定*/
    void employeeUnbind(ProjectManagement projectManagement, Employee employee);

    void unbindById(String id);
}
