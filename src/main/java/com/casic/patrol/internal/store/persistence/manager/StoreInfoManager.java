package com.casic.patrol.internal.store.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.internal.store.persistence.domain.StoreInfo;
import org.springframework.stereotype.Service;

@Service
public class StoreInfoManager extends HibernateEntityDao<StoreInfo> {
}
