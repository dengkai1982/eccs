package kaiyi.app.eccs;

import kaiyi.puer.commons.data.JavaDataTyper;
import kaiyi.puer.db.orm.ServiceException;

import java.io.Serializable;
import java.util.Map;

public interface DatabaseFastOper<T>{
    T  newObject(Map<String, JavaDataTyper> data)throws ServiceException;

    T updateObject(Serializable primaryId, Map<String, JavaDataTyper> data)throws ServiceException;
}
