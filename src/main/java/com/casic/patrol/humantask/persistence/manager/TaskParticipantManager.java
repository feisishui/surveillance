package com.casic.patrol.humantask.persistence.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.humantask.persistence.domain.TaskParticipant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskParticipantManager extends HibernateEntityDao<TaskParticipant> {
    public boolean exist(String userid, Long taskinfoId) {
        TaskParticipant temp = findUnique("from TaskParticipant where ref=? and taskInfo.id=?", userid, taskinfoId);
        return temp != null;
    }

    public List<TaskParticipant> getByTaskInfoId(Long taskinfoId, String category) {
        return find("from TaskParticipant where taskInfo.id=? and category=?", taskinfoId, category);
    }
}
