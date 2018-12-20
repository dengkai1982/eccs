package kaiyi.app.eccs.entity;

import kaiyi.puer.commons.data.Currency;
import kaiyi.puer.commons.data.ICurrency;
import kaiyi.puer.commons.time.DateTimeUtil;
import kaiyi.puer.commons.validate.DataValidate;
import kaiyi.puer.db.entity.LogicDeleteEntity;
import kaiyi.puer.json.JsonCreator;
import kaiyi.puer.json.JsonValuePolicy;
import kaiyi.puer.json.creator.StringJsonCreator;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 在职员工
 */
@Entity(name=Employee.TABLE_NAME)
public class Employee extends LogicDeleteEntity {
    public static final String TABLE_NAME="employee";
    private static final long serialVersionUID = 7586412469062785190L;
    //员工姓名
    private String name;
    //入职时间
    private Date jobDate;
    //离职时间
    private Date exitDate;
    //是否在职，true，在职
    private boolean onTheJob;
    //专业技术职务工资
    @ICurrency
    private int profressWages;
    //复核人员内部经营责任制考核
    @ICurrency
    private int reviewCheckWages;
    //经营协调等人员内部经营责任制考核
    @ICurrency
    private int operateCheckWages;
    /*
    @Override
    protected JsonValuePolicy convertJsonValuePolicy() {
        return new JsonValuePolicy<Employee>() {
            @Override
            public JsonCreator getCreator(Employee entity, String field, Object fieldValue) {
                if(field.equals("profressWages")||field.equals("reviewCheckWages")
                        ||field.equals("operateCheckWages")){
                    Long amount=Long.parseLong(fieldValue.toString());
                    return new StringJsonCreator(Currency.parseForNoDecimalPoint(amount).toString());
                }else if(field.equals("jobDate")||field.equals(exitDate)){
                    Date date=(Date)fieldValue;
                    return new StringJsonCreator(DateTimeUtil.yyyyMMdd.format(date));
                }
                return null;
            }
        };
    }
    */
    @DataValidate(required = true,emptyHint = "员工姓名必须填写")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Temporal(TemporalType.DATE)
    public Date getJobDate() {
        return jobDate;
    }

    public void setJobDate(Date jobDate) {
        this.jobDate = jobDate;
    }
    @Temporal(TemporalType.DATE)
    public Date getExitDate() {
        return exitDate;
    }

    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }

    public boolean isOnTheJob() {
        return onTheJob;
    }

    public void setOnTheJob(boolean onTheJob) {
        this.onTheJob = onTheJob;
    }

    public int getProfressWages() {
        return profressWages;
    }

    public void setProfressWages(int profressWages) {
        this.profressWages = profressWages;
    }

    public int getReviewCheckWages() {
        return reviewCheckWages;
    }

    public void setReviewCheckWages(int reviewCheckWages) {
        this.reviewCheckWages = reviewCheckWages;
    }

    public int getOperateCheckWages() {
        return operateCheckWages;
    }

    public void setOperateCheckWages(int operateCheckWages) {
        this.operateCheckWages = operateCheckWages;
    }

    @Override
    public SimpleDateFormat getFormatter(String fieldName) {
        if(fieldName.equals("jobDate")||fieldName.equals("exitDate")){
            return DateTimeUtil.yyyyMMdd;
        }
        return super.getFormatter(fieldName);
    }
}
