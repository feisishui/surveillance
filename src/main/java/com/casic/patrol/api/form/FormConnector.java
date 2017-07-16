package com.casic.patrol.api.form;

import java.util.List;

public interface FormConnector {
    List<FormDTO> getAll(String tenantId);

    FormDTO findForm(String code, String tenantId);
}
