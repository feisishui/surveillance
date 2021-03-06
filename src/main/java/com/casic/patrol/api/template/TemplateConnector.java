package com.casic.patrol.api.template;

import java.util.List;

public interface TemplateConnector {
    TemplateDTO findByCode(String code, String tenantId);

    List<TemplateDTO> findAll(String tenantId);
}
