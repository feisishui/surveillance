package com.casic.patrol.msg.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.msg.persistence.domain.MsgInfo;
import org.springframework.stereotype.Service;

@Service
public class MsgInfoManager extends HibernateEntityDao<MsgInfo> {
}
