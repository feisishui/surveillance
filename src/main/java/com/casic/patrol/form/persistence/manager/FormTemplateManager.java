package com.casic.patrol.form.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.form.persistence.domain.FormTemplate;
import org.springframework.stereotype.Service;

@Service
public class FormTemplateManager extends HibernateEntityDao<FormTemplate> {
}
