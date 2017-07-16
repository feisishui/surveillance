package com.casic.patrol.model.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.model.persistence.domain.ModelInfo;
import org.springframework.stereotype.Service;

@Service
public class ModelInfoManager extends HibernateEntityDao<ModelInfo> {
}
