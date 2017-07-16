package com.casic.patrol.bpm.persistence.manager;

import com.casic.patrol.bpm.persistence.domain.BpmTaskDef;
import com.casic.patrol.core.hibernate.HibernateEntityDao;
import org.springframework.stereotype.Service;

@Service
public class BpmTaskDefManager extends HibernateEntityDao<BpmTaskDef> {
}
