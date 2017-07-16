package com.casic.patrol.form.engine;


import com.casic.patrol.form.engine.model.FormModel;

public interface FormModelCache {
    FormModel getFormModel(String id);

    void setFormModel(FormModel formModel);
}
