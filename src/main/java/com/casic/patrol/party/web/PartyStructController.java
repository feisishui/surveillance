package com.casic.patrol.party.web;

import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.query.PropertyFilter;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.domain.PartyStruct;
import com.casic.patrol.party.persistence.domain.PartyStructType;
import com.casic.patrol.party.persistence.manager.PartyEntityManager;
import com.casic.patrol.party.persistence.manager.PartyStructManager;
import com.casic.patrol.party.persistence.manager.PartyStructTypeManager;
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
public class PartyStructController {
    private PartyEntityManager partyEntityManager;
    private PartyStructManager partyStructManager;
    private PartyStructTypeManager partyStructTypeManager;
    private MessageHelper messageHelper;
    private BeanMapper beanMapper = new BeanMapper();
    private TenantHolder tenantHolder;

    @RequestMapping("party-struct-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        propertyFilters.add(new PropertyFilter("EQS_tenantId", tenantId));
        page = partyStructManager.pagedQuery(page, propertyFilters);

        List<PartyStructType> partyStructTypes = partyStructTypeManager.findBy(
                "tenantId", tenantId);
        model.addAttribute("page", page);
        model.addAttribute("partyStructTypes", partyStructTypes);

        return "party/party-struct-list";
    }

    @RequestMapping("party-struct-input")
    public String input(@RequestParam(value = "id", required = false) Long id,
            Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PartyStructType> partyStructTypes = partyStructTypeManager.findBy(
                "tenantId", tenantId);
        List<PartyEntity> partyEntities = partyEntityManager.findBy("tenantId",
                tenantId);
        model.addAttribute("partyStructTypes", partyStructTypes);
        model.addAttribute("partyEntities", partyEntities);

        if (id != null) {
            PartyStruct partyStruct = partyStructManager.get(id);
            model.addAttribute("model", partyStruct);
        }

        return "party/party-struct-input";
    }

    @RequestMapping("party-struct-save")
    public String save(@ModelAttribute PartyStruct partyStruct,
            @RequestParam("partyStructTypeId") Long partyStructTypeId,
            @RequestParam("parentEntityId") Long parentEntityId,
            @RequestParam("childEntityId") Long childEntityId,
            RedirectAttributes redirectAttributes) {
        String tenantId = tenantHolder.getTenantId();
        PartyStruct dest = null;
        Long id = partyStruct.getId();

        if (id != null) {
            dest = partyStructManager.get(id);
            beanMapper.copy(partyStruct, dest);
        } else {
            dest = partyStruct;
            dest.setTenantId(tenantId);
        }

        dest.setPartyStructType(partyStructTypeManager.get(partyStructTypeId));
        dest.setParentEntity(partyEntityManager.get(parentEntityId));
        dest.setChildEntity(partyEntityManager.get(childEntityId));
        partyStructManager.save(dest);
        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/party/party-struct-list.do";
    }

    @RequestMapping("party-struct-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        partyStructManager
                .removeAll(partyStructManager.findByIds(selectedItem));
        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/party/party-struct-list.do";
    }

    // ~ ======================================================================
    @Resource
    public void setPartyEntityManager(PartyEntityManager partyEntityManager) {
        this.partyEntityManager = partyEntityManager;
    }

    @Resource
    public void setPartyStructManager(PartyStructManager partyStructManager) {
        this.partyStructManager = partyStructManager;
    }

    @Resource
    public void setPartyStructTypeManager(
            PartyStructTypeManager partyStructTypeManager) {
        this.partyStructTypeManager = partyStructTypeManager;
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
