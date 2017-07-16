package com.casic.patrol.bpm.persistence.manager;


import com.casic.patrol.bpm.persistence.domain.BpmProcess;
import com.casic.patrol.core.hibernate.HibernateEntityDao;
import org.springframework.stereotype.Service;

@Service
public class BpmProcessManager extends HibernateEntityDao<BpmProcess> {
}
