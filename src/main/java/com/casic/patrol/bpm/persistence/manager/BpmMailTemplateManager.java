package com.casic.patrol.bpm.persistence.manager;


import com.casic.patrol.bpm.persistence.domain.BpmMailTemplate;
import com.casic.patrol.core.hibernate.HibernateEntityDao;
import org.springframework.stereotype.Service;

@Service
public class BpmMailTemplateManager extends HibernateEntityDao<BpmMailTemplate> {
}
