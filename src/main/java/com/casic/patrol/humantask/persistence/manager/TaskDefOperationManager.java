package com.casic.patrol.humantask.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.humantask.persistence.domain.TaskDefOperation;
import org.springframework.stereotype.Service;

@Service
public class TaskDefOperationManager extends
        HibernateEntityDao<TaskDefOperation> {
}
