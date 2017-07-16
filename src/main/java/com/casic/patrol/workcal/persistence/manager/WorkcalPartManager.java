package com.casic.patrol.workcal.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.workcal.persistence.domain.WorkcalPart;
import org.springframework.stereotype.Service;

@Service
public class WorkcalPartManager extends HibernateEntityDao<WorkcalPart> {
}
