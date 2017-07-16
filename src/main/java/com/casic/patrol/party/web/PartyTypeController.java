package com.casic.patrol.party.web;

import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.query.PropertyFilter;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.party.persistence.domain.PartyType;
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
public class PartyTypeController {
    private PartyTypeManager partyTypeManager;
    private MessageHelper messageHelper;
    private BeanMapper beanMapper = new BeanMapper();
    private TenantHolder tenantHolder;

    @RequestMapping("party-type-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        propertyFilters.add(new PropertyFilter("EQS_tenantId", tenantId));
        page = partyTypeManager.pagedQuery(page, propertyFilters);

        model.addAttribute("page", page);

        return "party/party-type-list";
    }

    @RequestMapping("party-type-input")
    public String input(@RequestParam(value = "id", required = false) Long id,
            Model model) {
        if (id != null) {
            PartyType partyType = partyTypeManager.get(id);
            model.addAttribute("model", partyType);
        }

        return "party/party-type-input";
    }

    @RequestMapping("party-type-save")
    public String save(@ModelAttribute PartyType partyType,
            RedirectAttributes redirectAttributes) {
        String tenantId = tenantHolder.getTenantId();
        PartyType dest = null;
        Long id = partyType.getId();

        if (id != null) {
            dest = partyTypeManager.get(id);
            beanMapper.copy(partyType, dest);
        } else {
            dest = partyType;
            dest.setTenantId(tenantId);
        }

        partyTypeManager.save(dest);
        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/party/party-type-list.do";
    }

    @RequestMapping("party-type-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        partyTypeManager.removeAll(partyTypeManager.findByIds(selectedItem));
        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/party/party-type-list.do";
    }

    // ~ ======================================================================
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
