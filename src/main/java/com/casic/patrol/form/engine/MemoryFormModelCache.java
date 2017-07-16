package com.casic.patrol.form.engine;


import com.casic.patrol.form.engine.model.FormModel;

import java.util.HashMap;
import java.util.Map;

public class MemoryFormModelCache implements FormModelCache {
    private Map<String, FormModel> formModelMap = new HashMap<String, FormModel>();

    public FormModel getFormModel(String id) {
        return formModelMap.get(id);
    }

    public void setFormModel(FormModel formModel) {
        String id = formModel.getId();
        formModelMap.put(id, formModel);
    }
}
