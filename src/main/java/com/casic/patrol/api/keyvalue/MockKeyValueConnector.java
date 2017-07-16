package com.casic.patrol.api.keyvalue;


import com.casic.patrol.core.page.Page;

import java.util.List;
import java.util.Map;

public class MockKeyValueConnector implements KeyValueConnector {
    public Record findByCode(String code) {
        return null;
    }

    public Record findByRef(String ref) {
        return null;
    }

    public void save(Record record) {
    }

    public void removeByCode(String code) {
    }

    public List<Record> findByStatus(int status, String userId, String tenantId) {
        return null;
    }

    public Page pagedQuery(Page page, int status, String userId, String tenantId) {
        return null;
    }

    public long findTotalCount(String category, String q, String tenantId) {
        return 0L;
    }

    public List<Map<String, Object>> findResult(Page page, String category,
            String tenantId, Map<String, String> headers, String q) {
        return null;
    }

    public Record copyRecord(Record original, List<String> fields) {
        return null;
    }
}
