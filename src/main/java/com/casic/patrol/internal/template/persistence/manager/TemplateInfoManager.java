package com.casic.patrol.internal.template.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.internal.template.persistence.domain.TemplateInfo;
import org.springframework.stereotype.Service;

@Service
public class TemplateInfoManager extends HibernateEntityDao<TemplateInfo> {
}
