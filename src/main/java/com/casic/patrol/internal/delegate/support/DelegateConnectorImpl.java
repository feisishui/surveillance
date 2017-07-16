package com.casic.patrol.internal.delegate.support;


import com.casic.patrol.api.delegate.DelegateConnector;
import com.casic.patrol.internal.delegate.persistence.domain.DelegateInfo;
import com.casic.patrol.internal.delegate.service.DelegateService;

import javax.annotation.Resource;

public class DelegateConnectorImpl implements DelegateConnector {
    private DelegateService delegateService;

    public String findAttorney(String userId, String processDefinitionId,
            String taskDefinitionKey, String tenantId) {
        DelegateInfo delegateInfo = delegateService.getDelegateInfo(userId,
                processDefinitionId, taskDefinitionKey, tenantId);

        if (delegateInfo == null) {
            return null;
        }

        return delegateInfo.getAttorney();
    }

    public void recordDelegate(String userId, String attorney, String taskId,
            String tenantId) {
        delegateService.saveRecord(userId, attorney, taskId, tenantId);
    }

    public void cancel(String taskId, String userId, String tenantId) {
    }

    public void complete(String taskId, String complete, String tenantId) {
    }

    @Resource
    public void setDelegateService(DelegateService delegateService) {
        this.delegateService = delegateService;
    }
}
