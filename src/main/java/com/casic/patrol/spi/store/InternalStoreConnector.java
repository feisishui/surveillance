package com.casic.patrol.spi.store;



import com.casic.patrol.api.StoreDTO;

import javax.activation.DataSource;

public interface InternalStoreConnector {
    StoreDTO saveStore(String model, DataSource dataSource, String tenantId)
            throws Exception;

    StoreDTO saveStore(String model, String key, DataSource dataSource,
                       String tenantId) throws Exception;

    StoreDTO getStore(String model, String key, String tenantId)
            throws Exception;

    void removeStore(String model, String key, String tenantId)
            throws Exception;
}
