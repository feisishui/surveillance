package com.casic.patrol.workcal.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.workcal.persistence.domain.WorkcalRule;
import org.springframework.stereotype.Service;

@Service
public class WorkcalRuleManager extends HibernateEntityDao<WorkcalRule> {
}
