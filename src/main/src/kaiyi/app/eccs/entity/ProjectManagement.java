package kaiyi.app.eccs.entity;

import kaiyi.puer.commons.data.Currency;
import kaiyi.puer.commons.data.ICurrency;
import kaiyi.puer.commons.validate.DataValidate;
import kaiyi.puer.commons.validate.Validate;
import kaiyi.puer.commons.validate.ValidateType;
import kaiyi.puer.db.entity.LogicDeleteEntity;
import kaiyi.puer.json.JsonCreator;
import kaiyi.puer.json.JsonValuePolicy;
import kaiyi.puer.json.creator.StringJsonCreator;

import javax.persistence.Entity;

/**
 * 项目工程
 */
@Entity(name=ProjectManagement.TABLE_NAME)
public class ProjectManagement extends LogicDeleteEntity {
    public static final String TABLE_NAME="project_management";
    private static final long serialVersionUID = -7671447401032208050L;
    //合同编号
    private String contractNumber;
    //项目名称
    private String projectName;
    //项目类别
    private String projectType;
    //合同金额
    @ICurrency
    private int contractAmount;
    //实际到账金额
    @ICurrency
    private int transferredAmount;
    //初始提成比例
    private float rate;
    @Override
    protected JsonValuePolicy convertJsonValuePolicy() {
        return new JsonValuePolicy<ProjectManagement>() {
            @Override
            public JsonCreator getCreator(ProjectManagement entity, String field, Object fieldValue) {
                if(field.equals("contractAmount")||field.equals("transferredAmount")){
                    Long amount=Long.parseLong(fieldValue.toString());
                    return new StringJsonCreator(Currency.parseForNoDecimalPoint(amount).toString());
                }
                return null;
            }
        };
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public int getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(int contractAmount) {
        this.contractAmount = contractAmount;
    }

    public int getTransferredAmount() {
        return transferredAmount;
    }

    public void setTransferredAmount(int transferredAmount) {
        this.transferredAmount = transferredAmount;
    }
    @DataValidate(required = true,emptyHint = "初始提成必须必须填写",
            numberArea = true,startArea = 0d,endArea = 100d,
            validates = {
        @Validate(type = ValidateType.NUMBER,hint="不是有效的数值")
    })
    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
