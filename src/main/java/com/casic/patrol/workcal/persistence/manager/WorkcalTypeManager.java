package com.casic.patrol.workcal.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.workcal.persistence.domain.WorkcalType;
import org.springframework.stereotype.Service;

@Service
public class WorkcalTypeManager extends HibernateEntityDao<WorkcalType> {
}
