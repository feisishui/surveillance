package com.casic.patrol.party.web;

import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.query.PropertyFilter;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.party.persistence.domain.PartyStructRule;
import com.casic.patrol.party.persistence.domain.PartyStructType;
import com.casic.patrol.party.persistence.domain.PartyType;
import com.casic.patrol.party.persistence.manager.PartyStructRuleManager;
import com.casic.patrol.party.persistence.manager.PartyStructTypeManager;
import com.casic.patrol.party.persistence.manager.PartyTypeManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("party")
public class PartyStructRuleController {
    private PartyStructRuleManager partyStructRuleManager;
    private PartyStructTypeManager partyStructTypeManager;
    private PartyTypeManager partyTypeManager;
    private MessageHelper messageHelper;
    private TenantHolder tenantHolder;

    @RequestMapping("party-struct-rule-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        propertyFilters.add(new PropertyFilter("EQS_tenantId", tenantId));
        page = partyStructRuleManager.pagedQuery(page, propertyFilters);

        List<PartyStructType> partyStructTypes = partyStructTypeManager.findBy(
                "tenantId", tenantId);
        model.addAttribute("page", page);
        model.addAttribute("partyStructTypes", partyStructTypes);

        return "party/party-struct-rule-list";
    }

    @RequestMapping("party-struct-rule-input")
    public String input(@RequestParam(value = "id", required = false) Long id,
            Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PartyStructType> partyStructTypes = partyStructTypeManager.findBy(
                "tenantId", tenantId);
        List<PartyType> partyTypes = partyTypeManager.findBy("tenantId",
                tenantId);
        model.addAttribute("partyStructTypes", partyStructTypes);
        model.addAttribute("partyTypes", partyTypes);

        if (id != null) {
            PartyStructRule partyStructRule = partyStructRuleManager.get(id);
            model.addAttribute("model", partyStructRule);
        }

        return "party/party-struct-rule-input";
    }

    @RequestMapping("party-struct-rule-save")
    public String save(@RequestParam(value = "id", required = false) Long id,
            @RequestParam("partyStructTypeId") Long partyStructTypeId,
            @RequestParam("parentTypeId") Long parentTypeId,
            @RequestParam("childTypeId") Long childTypeId,
            RedirectAttributes redirectAttributes) {
        String tenantId = tenantHolder.getTenantId();

        if (id != null) {
            PartyStructRule partyStructRule = partyStructRuleManager.get(id);
            partyStructRuleManager.remove(partyStructRule);
        }

        PartyStructRule partyStructRule = new PartyStructRule();
        partyStructRule.setTenantId(tenantId);
        partyStructRule.setPartyStructType(partyStructTypeManager
                .get(partyStructTypeId));
        partyStructRule.setParentType(partyTypeManager.get(parentTypeId));
        partyStructRule.setChildType(partyTypeManager.get(childTypeId));

        partyStructRuleManager.save(partyStructRule);
        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/party/party-struct-rule-list.do";
    }

    @RequestMapping("party-struct-rule-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        partyStructRuleManager.removeAll(partyStructRuleManager
                .findByIds(selectedItem));
        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/party/party-struct-rule-list.do";
    }

    // ~ ======================================================================
    @Resource
    public void setPartyStructRuleManager(
            PartyStructRuleManager partyStructRuleManager) {
        this.partyStructRuleManager = partyStructRuleManager;
    }

    @Resource
    public void setPartyStructTypeManager(
            PartyStructTypeManager partyStructTypeManager) {
        this.partyStructTypeManager = partyStructTypeManager;
    }

    @Resource
    public void setPartyTypeManager(PartyTypeManager partyTypeManager) {
        this.partyTypeManager = partyTypeManager;
    }

    @Resource
    public void setMessageHelper(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }
}
