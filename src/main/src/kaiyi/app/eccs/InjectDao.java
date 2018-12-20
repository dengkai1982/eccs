package kaiyi.app.eccs;

import kaiyi.puer.commons.bean.BeanSyntacticSugar;
import kaiyi.puer.commons.data.JavaDataTyper;
import kaiyi.puer.db.DataChangeNotify;
import kaiyi.puer.db.orm.JpaDataOperImpl;
import kaiyi.puer.db.orm.ORMException;
import kaiyi.puer.db.orm.ServiceException;
import kaiyi.puer.db.query.QueryExpress;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;

@Transactional(propagation=Propagation.REQUIRED)
public abstract class InjectDao<T> extends JpaDataOperImpl<T> implements DatabaseFastOper<T> {
	private static final long serialVersionUID = -7804767008062125327L;
	@Resource
	protected ApplicationRuntimeService applicationRuntimeService;

	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly = true)
	@Override
	public T findForPrimary(Serializable entityid) {
		return super.findForPrimary(entityid);
	}

	@PersistenceContext(unitName="eccs_unit")
	@Override
	public void setEntityManager(EntityManager em) {
		this.em=em;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void saveObject(T entity) throws ORMException {
		super.saveObject(entity);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void updateObject(T entity) throws ORMException {
		super.updateObject(entity);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void deleteForPrimary(Serializable entityid) throws ORMException {
		super.deleteForPrimary(entityid);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public int deleteByQuery(QueryExpress express) throws ORMException {
		return super.deleteByQuery(express);
	}

	/***
	 * 新增对象
	 * @param data
	 * @throws ORMException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public T  newObject(Map<String, JavaDataTyper> data)throws ServiceException{
		T t=(T)BeanSyntacticSugar.objectBind(entityClass,data,getNewOrUpdateConvert());
		objectBeforePersistHandler(t);
		saveObject(t);
		objectAfterPersistHandler(t);
		return t;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public T updateObject(Serializable primaryId, Map<String, JavaDataTyper> data)throws ServiceException{
		T t=findForPrimary(primaryId);
		data.remove(primaryId);
		objectBeforeUpdateHandler(t);
		BeanSyntacticSugar.updateObject(t,data,getNewOrUpdateConvert());
		objectAfterUpdateHandler(t);
		return t;
	}

	/**
	 * 调用newObject持久化之前的回调方法
	 * @param t
	 */
	protected void objectBeforePersistHandler(T t)throws ServiceException{

	}
	/**
	 * 调用newObject持久化之后的回调方法
	 * @param t
	 */
	protected void objectAfterPersistHandler(T t)throws ServiceException{

	}
	/**
	 * 调用objectUpdate更新之前的回调方法
	 * @param t
	 */
	protected void objectBeforeUpdateHandler(T t)throws ServiceException{

	}
	/**
	 * 调用objectUpdate更新之后的回调方法
	 * @param t
	 */
	protected void objectAfterUpdateHandler(T t)throws ServiceException{

	}
	/**
	 * 自动新增、更新时，当传入参数是非基本对象时，需要提供转换方法
	 * @return
	 */
	protected  BiFunction<String, JavaDataTyper, Object> getNewOrUpdateConvert(){
		return null;
	}

	@Override
	public void registDatasourceStatusNotify(DataChangeNotify notifyer) {
		super.registDatasourceStatusNotify(notifyer);
	}



}
