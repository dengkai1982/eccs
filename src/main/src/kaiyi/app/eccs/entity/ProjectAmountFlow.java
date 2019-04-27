package kaiyi.app.eccs.entity;
import kaiyi.puer.commons.bean.BeanSyntacticSugar;
import kaiyi.puer.commons.data.Currency;
import kaiyi.puer.commons.data.ICurrency;
import kaiyi.puer.commons.time.DateTimeUtil;
import kaiyi.puer.db.entity.LogicDeleteEntity;
import kaiyi.puer.json.JsonCreator;
import kaiyi.puer.json.JsonValuePolicy;
import kaiyi.puer.json.creator.ObjectJsonCreator;
import kaiyi.puer.json.creator.StringJsonCreator;

import javax.persistence.*;
import java.util.Date;

/**
 * 项目资金入账流水
 */
@Entity(name=ProjectAmountFlow.TABLE_NAME)
public class ProjectAmountFlow extends LogicDeleteEntity {
    public static final String TABLE_NAME="project_amount_flow";
    private static final long serialVersionUID = -2138462281459166289L;
    //入账金额
    private int amount;
    //操作员
    private VisitorUser operMan;
    //备注信息
    private String remark;
    //工程合同
    private ProjectManagement projectManagement;

    private Date receivablesDate;
    //计提比例
    private int proportion;
    //计提金额
    @ICurrency
    private int commissionAmount;
    @Override
    protected JsonValuePolicy convertJsonValuePolicy() {
        return new JsonValuePolicy<ProjectAmountFlow>() {
            @Override
            public JsonCreator getCreator(ProjectAmountFlow entity, String field, Object fieldValue) {
                if(field.equals("operMan")){
                    VisitorUser operMan=entity.getOperMan();
                    return new ObjectJsonCreator<VisitorUser>(operMan,
                            BeanSyntacticSugar.getFieldString(VisitorUser.class,operMan.fieldFilter()),
                            operMan.jsonFieldReplacePolicy(),operMan.jsonValuePolicy());
                }else if(field.equals("projectManagement")){
                    ProjectManagement projectManagement=entity.getProjectManagement();
                    return new ObjectJsonCreator<ProjectManagement>(projectManagement,
                            BeanSyntacticSugar.getFieldString(ProjectManagement.class,projectManagement.fieldFilter()),
                            projectManagement.jsonFieldReplacePolicy(),projectManagement.jsonValuePolicy());
                }else if(field.equals("amount")){
                    Long currency=Long.parseLong(fieldValue.toString());
                    return new StringJsonCreator(Currency.parseForNoDecimalPoint(currency).toString());
                }else if(field.equals("receivablesDate")){
                    return new StringJsonCreator(DateTimeUtil.yyyyMMdd.format(entity.getReceivablesDate()));
                }
                return null;
            }
        };
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name="operMan")
    public VisitorUser getOperMan() {
        return operMan;
    }

    public void setOperMan(VisitorUser operMan) {
        this.operMan = operMan;
    }
    @Lob
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name="projectManagement")
    public ProjectManagement getProjectManagement() {
        return projectManagement;
    }

    public void setProjectManagement(ProjectManagement projectManagement) {
        this.projectManagement = projectManagement;
    }
    @Temporal(TemporalType.DATE)
    public Date getReceivablesDate() {
        return receivablesDate;
    }

    public void setReceivablesDate(Date receivablesDate) {
        this.receivablesDate = receivablesDate;
    }

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
    }

    public int getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(int commissionAmount) {
        this.commissionAmount = commissionAmount;
    }
}

