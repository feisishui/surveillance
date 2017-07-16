package com.casic.patrol.internal.sendmail.scheduler;


import com.casic.patrol.api.tenant.TenantConnector;
import com.casic.patrol.api.tenant.TenantDTO;
import com.casic.patrol.core.mail.MailHelper;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.internal.sendmail.persistence.domain.SendmailQueue;
import com.casic.patrol.internal.sendmail.service.SendmailDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Transactional
public class SendMailJob {
    private static Logger logger = LoggerFactory.getLogger(SendMailJob.class);
    private SendmailDataService sendmailDataService;
    private int threshold = 20;
    private BeanMapper beanMapper = new BeanMapper();
    private MailHelper mailHelper;
    private boolean running;
    private boolean enabled = true;
    private TenantConnector tenantConnector;

    // every 10 seconds
    @Scheduled(cron = "0/10 * * * * ?")
    public void execute() {
        if (!enabled) {
            return;
        }

        try {
            for (TenantDTO tenantDto : tenantConnector.findAll()) {
                this.doExecute(tenantDto.getId());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public synchronized void doExecute(String tenantId) throws Exception {
        if (running) {
            return;
        }

        running = true;
        logger.debug("send mail job start");

        List<SendmailQueue> sendmailQueues = sendmailDataService
                .findTopSendmailQueues(threshold, tenantId);
        logger.debug("sendmailQueues : {}", sendmailQueues.size());

        for (SendmailQueue sendmailQueue : sendmailQueues) {
            sendmailDataService.processSendmailQueue(sendmailQueue);
        }

        logger.debug("send mail job end");
        running = false;
    }

    @Resource
    public void setSendmailDataService(SendmailDataService sendmailDataService) {
        this.sendmailDataService = sendmailDataService;
    }

    @Resource
    public void setMailHelper(MailHelper mailHelper) {
        this.mailHelper = mailHelper;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    @Value("${mail.enabled}")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Resource
    public void setTenantConnector(TenantConnector tenantConnector) {
        this.tenantConnector = tenantConnector;
    }
}
