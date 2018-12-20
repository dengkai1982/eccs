package kaiyi.app.eccs.service;
import kaiyi.app.eccs.entity.VisitorMenu;
import kaiyi.puer.commons.access.AccessControl;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.db.DataChangeNotify;
import kaiyi.puer.db.orm.DatabaseQuery;

public interface VisitorMenuService extends DatabaseQuery<VisitorMenu> {
    /**
     * 新增菜单
     * @param code 菜单编码，作为主键使用
     * @param name 菜单名称
     * @param detail 菜单明细
     * @param action 菜单动作
     * @param actionFlag 菜单动作Action
     * @param weight 排序权重
     * @param parentCode 父级菜单编码
     */
    public void newMenu(String code, String name, String detail, AccessControl.Action action,
                        String actionFlag, float weight, String parentCode, boolean defaultAuthors);
    /**
     * 清理所有菜单项
     */
    public void clearMenus();
    /**
     * 获取根菜单
     * @return
     */
    public StreamCollection<VisitorMenu> getRootMenus();
    /**
     * 注册菜单编号通知
     * @param notifyer
     */
    public void registDatasourceStatusNotify(DataChangeNotify notifyer);


}
