package com.casic.patrol.portal.persistence.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.portal.persistence.domain.PortalInfo;
import org.springframework.stereotype.Service;

@Service
public class PortalInfoManager extends HibernateEntityDao<PortalInfo> {
}
