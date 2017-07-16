package com.casic.patrol.internal.sendmail.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.internal.sendmail.persistence.domain.SendmailQueue;
import org.springframework.stereotype.Service;

@Service
public class SendmailQueueManager extends HibernateEntityDao<SendmailQueue> {
}
