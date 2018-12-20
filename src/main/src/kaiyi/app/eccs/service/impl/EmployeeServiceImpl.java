package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.entity.Employee;
import kaiyi.app.eccs.service.EmployeeService;
import kaiyi.puer.commons.data.JavaDataTyper;
import kaiyi.puer.commons.time.DateTimeUtil;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service("employeeService")
public class EmployeeServiceImpl extends InjectDao<Employee> implements EmployeeService {

    private static final long serialVersionUID = -6060756610135407049L;

    @Override
    protected BiFunction<String, JavaDataTyper, Object> getNewOrUpdateConvert() {
        return (f,d)->{
            if(f.equals("jobDate")||f.equals("exitDate")){
                return d.dateValue(DateTimeUtil.yyyyMMdd,null);
            }else if(f.equals("profressWages")||f.equals("reviewCheckWages")||f.equals("operateCheckWages")){
                return d.currencyValue().getNoDecimalPoint().intValue();
            }
            return null;
        };
    }
}
