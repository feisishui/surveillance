package com.casic.patrol.bpm.proxy;

import org.activiti.engine.*;

public class ProxyProcessEngine implements ProcessEngine {
    private ProcessEngine processEngine;

    public void close() {
        if (processEngine == null) {
            return;
        }
    }

    public String getName() {
        if (processEngine == null) {
            return null;
        }

        return processEngine.getName();
    }

    public FormService getFormService() {
        if (processEngine == null) {
            return null;
        }

        return processEngine.getFormService();
    }

    public HistoryService getHistoryService() {
        if (processEngine == null) {
            return null;
        }

        return processEngine.getHistoryService();
    }

    public IdentityService getIdentityService() {
        if (processEngine == null) {
            return null;
        }

        return processEngine.getIdentityService();
    }

    public ManagementService getManagementService() {
        if (processEngine == null) {
            return null;
        }

        return processEngine.getManagementService();
    }

    public ProcessEngineConfiguration getProcessEngineConfiguration() {
        if (processEngine == null) {
            return null;
        }

        return processEngine.getProcessEngineConfiguration();
    }

    public RepositoryService getRepositoryService() {
        if (processEngine == null) {
            return null;
        }

        return processEngine.getRepositoryService();
    }

    public RuntimeService getRuntimeService() {
        if (processEngine == null) {
            return null;
        }

        return processEngine.getRuntimeService();
    }

    public TaskService getTaskService() {
        if (processEngine == null) {
            return null;
        }

        return processEngine.getTaskService();
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }
}