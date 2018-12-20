package kaiyi.app.eccs.entity;

import kaiyi.puer.commons.data.ICurrency;
import kaiyi.puer.db.entity.TimeStampEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * 项目提成发放
 */
@Entity(name=ProjectExtractGrant.TABLE_NAME)
public class ProjectExtractGrant extends TimeStampEntity {
    public static final String TABLE_NAME="poject_extract_grant";
    private static final long serialVersionUID = -2852536021190722361L;
    /**
     * 管理的项目
     */
    private ProjectManagement projectManagement;
    /**
     * 关联的流水项
     */
    private ProjectAmountFlow projectAmountFlow;
    /**
     * 已发放提成总额
     */
    @ICurrency
    private int totalGrantAmount;
    /**
     * 发放员工记录
     */
    private Set<ExtractGrantItem> items;

    @Override
    protected String[] filterFiledArray() {
        return new String[]{"projectManagement","items"};
    }

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="projectManagement")
    public ProjectManagement getProjectManagement() {
        return projectManagement;
    }

    public void setProjectManagement(ProjectManagement projectManagement) {
        this.projectManagement = projectManagement;
    }
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name="projectAmountFlow")
    public ProjectAmountFlow getProjectAmountFlow() {
        return projectAmountFlow;
    }

    public void setProjectAmountFlow(ProjectAmountFlow projectAmountFlow) {
        this.projectAmountFlow = projectAmountFlow;
    }

    public int getTotalGrantAmount() {
        return totalGrantAmount;
    }

    public void setTotalGrantAmount(int totalGrantAmount) {
        this.totalGrantAmount = totalGrantAmount;
    }
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "extractGrant" )
    public Set<ExtractGrantItem> getItems() {
        return items;
    }

    public void setItems(Set<ExtractGrantItem> items) {
        this.items = items;
    }
}
