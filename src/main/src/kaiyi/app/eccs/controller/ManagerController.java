package kaiyi.app.eccs.controller;

import kaiyi.app.eccs.ApplicationRuntimeService;
import kaiyi.app.eccs.DatabaseFastOper;
import kaiyi.app.eccs.entity.VisitorMenu;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.app.eccs.service.VisitorMenuService;
import kaiyi.puer.commons.collection.Cascadeable;
import kaiyi.puer.commons.collection.ProcessCascadeEachHandler;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.commons.data.JavaDataTyper;
import kaiyi.puer.commons.data.StringEditor;
import kaiyi.puer.commons.time.DateTimeUtil;
import kaiyi.puer.db.Pagination;
import kaiyi.puer.db.orm.DatabaseQuery;
import kaiyi.puer.db.orm.ServiceException;
import kaiyi.puer.db.query.CompareQueryExpress;
import kaiyi.puer.db.query.NullQueryExpress;
import kaiyi.puer.db.query.OrderBy;
import kaiyi.puer.db.query.QueryExpress;
import kaiyi.puer.json.creator.JsonMessageCreator;
import kaiyi.puer.web.AbstractController;
import kaiyi.puer.web.servlet.PageNavigation;
import kaiyi.puer.web.servlet.ServletUtils;
import kaiyi.puer.web.servlet.WebInteractive;
import kaiyi.puer.web.servlet.WebPage;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.BiConsumer;

public abstract class ManagerController extends AbstractController {

    @Resource
    protected VisitorMenuService visitorMenuService;
    @Resource
    protected ApplicationRuntimeService applicationRuntimeService;
    public static final String NEW_OR_EDIT_ENTITY_NAME="entity";

    public static final String NAME_SESSION_USER="_name_session_user";
    public static final String APPLICATION_MENU="_application_menu";
    public static final String PAGINATION="_pagination";
    private static final StreamCollection<String> clearParameterName;
    static{
        clearParameterName=new StreamCollection<>();
        clearParameterName.add(WebInteractive.PAGINATION_PARAMETER_CURRENT_PAGE);
        clearParameterName.add(WebInteractive.PAGINATION_PARAMETER_MAX_RESULT);
        clearParameterName.add(WebInteractive.PAGINATION_PARAMETER_FIRST);
    }
    protected void setQueryBackPage(WebInteractive interactive){
        interactive.setRequestAttribute("fieldName",interactive.getStringParameter("fieldName",""));
        interactive.setRequestAttribute("fieldId",interactive.getStringParameter("fieldId",""));
    }
    @Override
    protected OrderBy getDefaultOrderBy(String prefix,String  orderByFlag) {
        return null;
    }



    /********************* 消息处理相关  ***************************/
    /**
     * 获取默认正常返回消息
     * @return
     */
    protected JsonMessageCreator getSuccessMessage(){
        return new JsonMessageCreator(JsonMessageCreator.SUCCESS,JsonMessageCreator.SUCCESS,"");
    }

    /**
     * 捕获到ServiceException异常是调用
     * @param msg
     * @param e
     */
    protected void catchServiceException(JsonMessageCreator msg,ServiceException e){
        msg.setMsg(e.getErrorMsg());
        msg.setCode(e.getErrorCode());
    }
    /********************* 用户登录相关  ***************************/
    //将用户加入到session
    protected void addUserToSession(WebInteractive interactive,VisitorUser user){
        HttpServletRequest request=interactive.getHttpServletRequest();
        HttpSession session=request.getSession();
        if(session.isNew()) {
            session.setMaxInactiveInterval(Long.valueOf(DateTimeUtil.getHourMillisecond()*12).intValue());
        }
        session.setAttribute(NAME_SESSION_USER,user);
    }
    /**
     * 获取已经登录的用户
     * @param request
     * @return
     */
    public static final VisitorUser getLoginedUser(HttpServletRequest request){
        return (VisitorUser) request.getSession().getAttribute(NAME_SESSION_USER);
    }
    /**
     * 获取登陆用户信息
     * @param interactive
     * @return
     */
    protected VisitorUser getLoginedUser(WebInteractive interactive) {
        return getLoginedUser(interactive.getHttpServletRequest());
    }
    //移除用户登录
    protected void removeUserBySession(WebInteractive interactive){
        HttpServletRequest request=interactive.getHttpServletRequest();
        HttpSession session=request.getSession();
        session.removeAttribute(NAME_SESSION_USER);
    }
    /********************* 页面菜单相关  ***************************/
    /**
     * 添加系统菜单
     */
    protected VisitorMenu addMenuToApplication(HttpServletRequest request){
        StreamCollection<VisitorMenu> menus = visitorMenuService.getRootMenus();
        Cascadeable.forEachCascade(menus.toList(), new ProcessCascadeEachHandler<Cascadeable>() {
            @Override
            public void beforeEach(Cascadeable cascadeable) {

            }

            @Override
            public void beforeEachChild(Cascadeable cascadeable, Collection<? extends Cascadeable> collection) {

            }

            @Override
            public void afterEachChild(Cascadeable cascadeable, Collection<? extends Cascadeable> collection) {

            }

            @Override
            public void atferEach(Cascadeable cascadeable) {

            }

            @Override
            public boolean each(int i, Cascadeable cascadeable) {
                VisitorMenu menu=(VisitorMenu)cascadeable;
                menu.setShowable(true);
                return true;
            }
        });
        VisitorMenu firstMenu=null;
        if(!menus.isEmpty()){
            VisitorMenu first=menus.get(0);
            first.setActive(true);
            if(!first.getChildrenList().isEmpty()){
                firstMenu=((VisitorMenu)first.getChildrenList().get(0));
                firstMenu.setActive(true);
            }
        }
        request.getServletContext().setAttribute(APPLICATION_MENU,menus);
        return firstMenu;
    }
    /**
     * 获取系统菜单
     * @return
     */
    protected StreamCollection<VisitorMenu> getMenuForApplication(WebInteractive interactive){
        return getMenuForApplication(interactive.getHttpServletRequest());
    }

    public static StreamCollection<VisitorMenu> getMenuForApplication(HttpServletRequest request){
        StreamCollection<VisitorMenu> visitorMenus=(StreamCollection<VisitorMenu>)request.getServletContext().getAttribute(APPLICATION_MENU);
        if(visitorMenus==null){
            visitorMenus=new StreamCollection<>();
        }
        return visitorMenus;
    }
    //设置菜单为激活
    private void setParentMenuActive(VisitorMenu menu) {
        VisitorMenu parent = menu.getParent();
        if (parent != null) {
            parent.setActive(true);
            setParentMenuActive(parent);
        }
    }
    //设置导航栏
    private void setNavigation(WebPage webPage, VisitorMenu menu, boolean active) {
        PageNavigation nav = new PageNavigation();
        nav.setAccessId(menu.getId());
        nav.setActive(active);
        nav.setName(menu.getName());
        nav.setPath(menu.getActionFlag());
        nav.setShow(true);
        webPage.addNavigation(nav);
        if (menu.getParent() != null) {
            setNavigation(webPage, menu.getParent(), false);
        }
    }

    /**
     * 构建新增,修改的基本页面信息
     * @param interactive
     * @param query
     */
    protected <T> void newOrEditPage(WebInteractive interactive, DatabaseQuery<T> query, BiConsumer<WebInteractive,T> c){
        setLimitPage(interactive);
        String id=interactive.getStringParameter("id","");
        if(StringEditor.notEmpty(id)){
            T t=query.findForPrimary(id);
            interactive.setRequestAttribute(NEW_OR_EDIT_ENTITY_NAME,t);
            if(c!=null)
            c.accept(interactive,t);
        }else{
            if(c!=null)
            c.accept(interactive,null);
        }
    }

    //设置分页参数
    protected void setLimitPage(WebInteractive interactive) {
        int currentPage=interactive.getPaginationCurrentPage();
        interactive.setRequestAttribute("currentPage",currentPage);
    }
    /**
     * 完善页面
     */
    protected void perfectPage(WebInteractive interactive){
        WebPage webPage=interactive.getWebPage();
        String requestPrefix=ServletUtils.getRequestPrefix(interactive.getHttpServletRequest(),false);
        StreamCollection<VisitorMenu> sessionMenus=getMenuForApplication(interactive.getHttpServletRequest());
        if(sessionMenus==null)return;
        VisitorMenu menu= (VisitorMenu)Cascadeable.findById(sessionMenus,requestPrefix);
        if(menu!=null){
            webPage.setPageTitle(menu.getName());
            setNavigation(webPage,menu,true);
            List<PageNavigation> navs=webPage.getNavigations();
            Collections.reverse(navs);
            webPage.setNavigations(navs);
            final String currentMenuId=menu.getId();
            Cascadeable.forEachCascade(sessionMenus.toList(), new ProcessCascadeEachHandler<Cascadeable>() {
                @Override
                public void beforeEach(Cascadeable cascadeable) {

                }

                @Override
                public void beforeEachChild(Cascadeable cascadeable, Collection<? extends Cascadeable> collection) {

                }

                @Override
                public void afterEachChild(Cascadeable cascadeable, Collection<? extends Cascadeable> collection) {

                }

                @Override
                public void atferEach(Cascadeable cascadeable) {

                }

                @Override
                public boolean each(int i, Cascadeable cascadeable) {
                    VisitorMenu menu=(VisitorMenu)cascadeable;
                    if(menu.getId().equals(currentMenuId)){
                        menu.setActive(true);
                    }else{
                        menu.setActive(false);
                    }
                    return true;
                }
            });
            String url=webPage.getRequestUrlAndParameter(clearParameterName);
            webPage.setPaginationQueryUrl(url);
            webPage.setConditionQueryUrl(webPage.getFullRequestUrl());
            setParentMenuActive(menu);
        }
    }
    protected <T> T exuterNewOrUpdate(WebInteractive interactive, DatabaseFastOper<T> oper, JsonMessageCreator jmc,
                                     Map<String,JavaDataTyper> otherParams){
        Map<String,JavaDataTyper> params=interactive.getRequestParameterMap();
        if(Objects.nonNull(otherParams)&&!otherParams.isEmpty()){
            otherParams.forEach((key,value)->{
                params.put(key,value);
            });
        }
        StringEditor id=interactive.getStringEditorParameter("id");
        try{
            if(id.assertIsEmpty()){
                return oper.newObject(params);
            }else{
                return oper.updateObject(id.getValue(),params);
            }
        }catch(ServiceException e){
            catchServiceException(jmc,e);
        }
        return null;
    }

    protected <T> T exuterNewOrUpdate(WebInteractive interactive, DatabaseFastOper<T> oper, JsonMessageCreator jmc) {
        return exuterNewOrUpdate(interactive,oper,jmc,null);
    }
    /**
     * 自定义查询
     * @param interactive
     * @param queryOperator
     * @param sql
     * @param valueMap
     * @param useNative
     */
    protected void generatorDataListPage(WebInteractive interactive, DatabaseQuery<?> queryOperator,String sql,
                                         Map<String,Object> valueMap,boolean useNative) {
        perfectPage(interactive);
        int currentPage=interactive.getPaginationCurrentPage();
        int maxResult=interactive.getPaginationMaxResult();
        interactive.setRequestAttribute("currentPage",currentPage);
        interactive.setRequestAttribute("maxResult", maxResult);
        int count=queryOperator.customCount(sql,valueMap,useNative);
        Pagination<?> pagination=new Pagination<>(maxResult, count);
        pagination.switchPage(currentPage);
        interactive.setRequestAttribute(PAGINATION,pagination);
    }

    protected void generatorDataListPage(WebInteractive interactive, DatabaseQuery<?> queryOperator, QueryExpress queryExpress) {
        int currentPage=interactive.getPaginationCurrentPage();
        int maxResult=interactive.getPaginationMaxResult();
        interactive.setRequestAttribute("currentPage",currentPage);
        interactive.setRequestAttribute("maxResult", maxResult);
        int count=queryOperator.count(queryExpress);
        Pagination<?> pagination=new Pagination<>(maxResult, count);
        pagination.switchPage(currentPage);
        interactive.setRequestAttribute(PAGINATION,pagination);
    }

    protected QueryExpress getEnableQueryExpress(){
        return new CompareQueryExpress("enable",CompareQueryExpress.Compare.EQUAL,Boolean.TRUE);
    }

    protected QueryExpress getIdentifyNotNullQueryExpress(){
        return new NullQueryExpress("id",NullQueryExpress.NullCondition.IS_NOT_NULL);
    }
}
