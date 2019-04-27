package kaiyi.app.eccs.service;

import kaiyi.app.eccs.entity.ExtractGrantItem;
import kaiyi.puer.db.orm.DatabaseQuery;

public interface ExtractGrantItemService extends DatabaseQuery<ExtractGrantItem> {
    String deleteById(String id);

    String totalCommission(String employeeId,String  projectManagementId);
}
