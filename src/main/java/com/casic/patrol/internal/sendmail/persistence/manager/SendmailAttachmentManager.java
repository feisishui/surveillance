package com.casic.patrol.internal.sendmail.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.internal.sendmail.persistence.domain.SendmailAttachment;
import org.springframework.stereotype.Service;

@Service
public class SendmailAttachmentManager extends
        HibernateEntityDao<SendmailAttachment> {
}
