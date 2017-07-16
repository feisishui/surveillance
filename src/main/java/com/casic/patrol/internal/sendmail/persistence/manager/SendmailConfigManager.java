package com.casic.patrol.internal.sendmail.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.internal.sendmail.persistence.domain.SendmailConfig;
import org.springframework.stereotype.Service;

@Service
public class SendmailConfigManager extends HibernateEntityDao<SendmailConfig> {
}
