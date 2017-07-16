package com.casic.patrol.internal.template.web;


import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.export.Exportor;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.query.PropertyFilter;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.internal.template.persistence.domain.TemplateInfo;
import com.casic.patrol.internal.template.persistence.manager.TemplateInfoManager;
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
@RequestMapping("template")
public class TemplateInfoController {
    private TemplateInfoManager templateInfoManager;
    private MessageHelper messageHelper;
    private Exportor exportor;
    private BeanMapper beanMapper = new BeanMapper();
    private TenantHolder tenantHolder;

    @RequestMapping("template-info-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        propertyFilters.add(new PropertyFilter("EQS_tenantId", tenantId));
        page = templateInfoManager.pagedQuery(page, propertyFilters);
        model.addAttribute("page", page);

        return "template/template-info-list";
    }

    @RequestMapping("template-info-input")
    public String input(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "type", required = false) String whitelistTypeCode,
            Model model) {
        TemplateInfo templateInfo = null;

        if (id != null) {
            templateInfo = templateInfoManager.get(id);
            model.addAttribute("model", templateInfo);
        }

        return "template/template-info-input";
    }

    @RequestMapping("template-info-save")
    public String save(@ModelAttribute TemplateInfo templateInfo,
            RedirectAttributes redirectAttributes) {
        String tenantId = tenantHolder.getTenantId();
        Long id = templateInfo.getId();
        TemplateInfo dest = null;

        if (id != null) {
            dest = templateInfoManager.get(id);
            beanMapper.copy(templateInfo, dest);
        } else {
            dest = templateInfo;
            dest.setTenantId(tenantId);
        }

        templateInfoManager.save(dest);

        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/template/template-info-list.do";
    }

    @RequestMapping("template-info-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        List<TemplateInfo> templateInfos = templateInfoManager
                .findByIds(selectedItem);

        for (TemplateInfo templateInfo : templateInfos) {
            templateInfoManager.remove(templateInfo);
        }

        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/template/template-info-list.do";
    }

    // ~ ======================================================================
    @Resource
    public void setTemplateInfoManager(TemplateInfoManager templateInfoManager) {
        this.templateInfoManager = templateInfoManager;
    }

    @Resource
    public void setMessageHelper(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    @Resource
    public void setExportor(Exportor exportor) {
        this.exportor = exportor;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }
}
