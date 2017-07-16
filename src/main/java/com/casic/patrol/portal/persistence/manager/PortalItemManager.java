package com.casic.patrol.portal.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.portal.persistence.domain.PortalItem;
import org.springframework.stereotype.Service;

@Service
public class PortalItemManager extends HibernateEntityDao<PortalItem> {
}
