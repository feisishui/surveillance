package com.casic.patrol.internal.delegate.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.internal.delegate.persistence.domain.DelegateHistory;
import org.springframework.stereotype.Service;

@Service
public class DelegateHistoryManager extends HibernateEntityDao<DelegateHistory> {
}
