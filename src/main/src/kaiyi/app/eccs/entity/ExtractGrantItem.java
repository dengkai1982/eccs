package kaiyi.app.eccs.entity;

import kaiyi.puer.commons.data.ICurrency;
import kaiyi.puer.db.entity.TimeStampEntity;

import javax.persistence.*;

/**
 * 提成发放表
 */
@Entity(name=ExtractGrantItem.TABLE_NAME)
public class ExtractGrantItem extends TimeStampEntity {
    public static final String TABLE_NAME="extract_grant_item";
    private static final long serialVersionUID = -7430687321945901968L;

    private ProjectExtractGrant extractGrant;

    private Employee employee;
    /**提成比例*/
    private float rate;
    /**实际到账金额*/
    @ICurrency
    private int amount;

    @Override
    protected String[] filterFiledArray() {
        return new String[0];
    }

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name="extractGrant")
    public ProjectExtractGrant getExtractGrant() {
        return extractGrant;
    }

    public void setExtractGrant(ProjectExtractGrant extractGrant) {
        this.extractGrant = extractGrant;
    }
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name="employee")
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
