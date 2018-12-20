package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.entity.ExtractGrantItem;
import kaiyi.app.eccs.service.ExtractGrantItemService;
import org.springframework.stereotype.Service;

@Service("extractGrantItemService")
public class ExtractGrantItemServiceImpl extends InjectDao<ExtractGrantItem> implements ExtractGrantItemService {
    private static final long serialVersionUID = 1859883909851693458L;

    @Override
    public void deleteById(String id) {
        em.createQuery("delete from "+getEntityName(entityClass)+" o where o.id=:id")
                .setParameter("id",id).executeUpdate();
    }
}
