package kaiyi.app.eccs;

import kaiyi.puer.db.orm.ServiceException;

/**
 * 服务异常定义
 */
public class ServiceExceptionDefine {
    public static final ServiceException userNotExist=new ServiceException(
            ServiceException.CODE_FAIL,"账户不存在");
    public static final ServiceException passwordError=new ServiceException(
            ServiceException.CODE_FAIL,"密码错误");

    public static final ServiceException oldPasswordError=new ServiceException(
            ServiceException.CODE_FAIL,"旧密码错误");

    public static final ServiceException password2Error=new ServiceException(
            ServiceException.CODE_FAIL,"两次密码错误");
    public static final ServiceException loginError=new ServiceException(
            ServiceException.CODE_FAIL,"登录账号不存在或密码错误");

    public static final ServiceException contractNotExist=new ServiceException(
            ServiceException.CODE_FAIL,"合同不存在");

    public static final ServiceException employeeNotExist=new ServiceException(
            ServiceException.CODE_FAIL,"员工不存在");

    public static final ServiceException admissionGTContract=new ServiceException(
            ServiceException.CODE_FAIL,"入账金额超过合同金额");

    public static final ServiceException admissionZeor=new ServiceException(
            ServiceException.CODE_FAIL,"入账金额不能为0或负数");

    public static final ServiceException employeeIsBindToContract=new ServiceException(
            ServiceException.CODE_FAIL,"员工已参与该合同");

    public static final ServiceException extractGrantExist=new ServiceException(
            ServiceException.CODE_FAIL,"已存在有提成记录");

    public static final ServiceException emptyAmountFlow=new ServiceException(
            ServiceException.CODE_FAIL,"收款流水不存在或已删除");
}

