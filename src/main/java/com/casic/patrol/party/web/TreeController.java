package com.casic.patrol.party.web;

import java.util.List;

import javax.annotation.Resource;

import com.casic.patrol.api.tenant.TenantHolder;

import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.domain.PartyStructType;
import com.casic.patrol.party.persistence.manager.PartyEntityManager;
import com.casic.patrol.party.persistence.manager.PartyStructTypeManager;
import com.casic.patrol.party.service.PartyService;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("party")
public class TreeController {
    private PartyEntityManager partyEntityManager;
    private PartyStructTypeManager partyStructTypeManager;
    private PartyService partyService;
    private TenantHolder tenantHolder;

    @RequestMapping("tree-list")
    public String list(
            @RequestParam(value = "partyStructTypeId", required = false) Long partyStructTypeId,
            Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PartyStructType> partyStructTypes = partyStructTypeManager.findBy(
                "tenantId", tenantId);

        List<PartyEntity> partyEntities = partyService
                .getTopPartyEntities(partyStructTypeId);
        model.addAttribute("partyStructTypes", partyStructTypes);
        model.addAttribute("partyEntities", partyEntities);

        return "party/tree-list";
    }

    // ~ ======================================================================
    @Resource
    public void setPartyEntityManager(PartyEntityManager partyEntityManager) {
        this.partyEntityManager = partyEntityManager;
    }

    @Resource
    public void setPartyStructTypeManager(
            PartyStructTypeManager partyStructTypeManager) {
        this.partyStructTypeManager = partyStructTypeManager;
    }

    @Resource
    public void setPartyService(PartyService partyService) {
        this.partyService = partyService;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }
}
