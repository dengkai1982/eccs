package kaiyi.app.eccs.entity;

import kaiyi.puer.commons.time.DateTimeUtil;
import kaiyi.puer.commons.validate.DataValidate;
import kaiyi.puer.commons.validate.Validate;
import kaiyi.puer.commons.validate.ValidateType;
import kaiyi.puer.db.entity.LogicDeleteEntity;
import kaiyi.puer.json.JsonCreator;
import kaiyi.puer.json.JsonValuePolicy;
import kaiyi.puer.json.creator.JsonBuilder;
import kaiyi.puer.json.creator.StringJsonCreator;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name=VisitorUser.TABLE_NAME)
public class VisitorUser extends LogicDeleteEntity implements JsonBuilder {
    private static final long serialVersionUID = 1986062763273225706L;
    public static final String TABLE_NAME="visitor_user";
    /******************登录相关******************/
    //登录账号
    private String loginName;
    //真实姓名
    private String realName;
    //登录密码
    private String password;
    //最后一次登录时间
    private Date lastLoginTime;
    //访问次数
    private long accessNumber;

    @Override
    protected String[] filterFiledArray() {
        return new String[]{"password"};
    }

    @Override
    protected JsonValuePolicy convertJsonValuePolicy() {
        return new JsonValuePolicy<VisitorUser>() {
            @Override
            public JsonCreator getCreator(VisitorUser entity, String field, Object fieldValue) {
                if(field.equals("lastLoginTime")){
                    return new StringJsonCreator(DateTimeUtil.yyyyMMddHHmmss.format(entity.getLastLoginTime()));
                }
                return null;
            }
        };
    }

    @DataValidate(required = true,emptyHint = "登录名不能为空")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    @Lob
    @DataValidate(validates = {
            @Validate(type=ValidateType.MIN_LENGTH,hint="密码长度不能小于6",length=6)
    },required = true,emptyHint = "密码不能为空")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(long accessNumber) {
        this.accessNumber = accessNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}
