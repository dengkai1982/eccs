package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.entity.VisitorMenu;
import kaiyi.app.eccs.service.VisitorMenuService;
import kaiyi.puer.commons.access.AccessControl;
import kaiyi.puer.commons.collection.Cascadeable;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.commons.data.StringEditor;
import kaiyi.puer.db.query.OrderBy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("visitorMenuService")
public class VisitorMenuServiceImpl extends InjectDao<VisitorMenu> implements VisitorMenuService {

    private static final long serialVersionUID = -5924317086688496778L;

    @Override
    public void newMenu(String code, String name, String detail, AccessControl.Action action,
                        String actionFlag, float weight, String parentCode,boolean defaultAuthors){
        VisitorMenu parent=null;
        if(StringEditor.notEmpty(parentCode)){
            parent=findForPrimary(parentCode);
        }
        VisitorMenu menu=new VisitorMenu(name,detail,action,actionFlag,weight, Cascadeable.START_LEVEL);
        menu.setId(code);
        menu.setDefaultAuthor(defaultAuthors);
        if(parent!=null){
            menu.setLevel(parent.getLevel()+1);
            menu.setParent(parent);
        }
        saveObject(menu);
    }

    @Override
    public void clearMenus() {
        int result=em.createQuery("update "+getEntityName(entityClass)+" o set o.parent=:parent")
                .setParameter("parent",null).executeUpdate();
        assert result>=0;
        em.createQuery("delete from "+getEntityName(entityClass)).executeUpdate();
        assert result>=0;
    }

    @Override
    public StreamCollection<VisitorMenu> getRootMenus() {
        OrderBy orderby=new OrderBy("o","level",OrderBy.TYPE.ASC);
        orderby.add("o","weight",OrderBy.TYPE.DESC);
        StreamCollection<VisitorMenu> menuStrem=getEntitys(orderby);
        List<VisitorMenu> result=new ArrayList<>();
        Cascadeable.cascade(menuStrem.toList(),result);
        Collections.sort(result);
        return new StreamCollection<>(result);
    }

}
