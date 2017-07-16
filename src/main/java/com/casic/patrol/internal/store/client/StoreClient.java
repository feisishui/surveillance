package com.casic.patrol.internal.store.client;



import com.casic.patrol.api.StoreDTO;

import java.io.InputStream;

public interface StoreClient {
    StoreDTO saveStore(InputStream inputStream, String fileName,
                       String contentType, String tenantId) throws Exception;

    StoreDTO getStore(String key, String tenantId) throws Exception;
}
