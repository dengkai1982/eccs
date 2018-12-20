package kaiyi.app.eccs.service;

import kaiyi.app.eccs.DatabaseFastOper;
import kaiyi.app.eccs.entity.Employee;
import kaiyi.puer.db.orm.DatabaseQuery;

public interface EmployeeService extends DatabaseQuery<Employee> ,DatabaseFastOper<Employee> {
}
