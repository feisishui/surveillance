package com.casic.patrol.api.tenant;

public interface TenantHolder {
    String getTenantId();

    String getTenantCode();

    String getUserRepoRef();

    TenantDTO getTenantDto();
}
