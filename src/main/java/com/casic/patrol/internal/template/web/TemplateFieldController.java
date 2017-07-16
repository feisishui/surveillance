package com.casic.patrol.internal.template.web;


import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.export.Exportor;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.query.PropertyFilter;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.internal.template.persistence.domain.TemplateField;
import com.casic.patrol.internal.template.persistence.manager.TemplateFieldManager;
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
@RequestMapping("/template")
public class TemplateFieldController {
    private TemplateFieldManager templateFieldManager;
    private TemplateInfoManager templateInfoManager;
    private MessageHelper messageHelper;
    private Exportor exportor;
    private BeanMapper beanMapper = new BeanMapper();
    private TenantHolder tenantHolder;

    @RequestMapping("template-field-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        propertyFilters.add(new PropertyFilter("EQS_tenantId", tenantId));
        page = templateFieldManager.pagedQuery(page, propertyFilters);
        model.addAttribute("page", page);

        return "template/template-field-list";
    }

    @RequestMapping("template-field-input")
    public String input(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "type", required = false) String whitelistTypeCode,
            Model model) {
        String tenantId = tenantHolder.getTenantId();
        TemplateField templateField = null;

        if (id != null) {
            templateField = templateFieldManager.get(id);
            model.addAttribute("model", templateField);
        }

        model.addAttribute("templateInfos",
                templateInfoManager.findBy("tenantId", tenantId));

        return "template/template-field-input";
    }

    @RequestMapping("template-field-save")
    public String save(@ModelAttribute TemplateField templateField,
            @RequestParam("infoId") Long infoId,
            RedirectAttributes redirectAttributes) {
        String tenantId = tenantHolder.getTenantId();
        Long id = templateField.getId();
        TemplateField dest = null;

        if (id != null) {
            dest = templateFieldManager.get(id);
            beanMapper.copy(templateField, dest);
        } else {
            dest = templateField;
            dest.setTenantId(tenantId);
        }

        dest.setTemplateInfo(templateInfoManager.get(infoId));

        templateFieldManager.save(dest);

        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/template/template-field-list.do";
    }

    @RequestMapping("template-field-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        List<TemplateField> templateFields = templateFieldManager
                .findByIds(selectedItem);

        for (TemplateField templateField : templateFields) {
            templateFieldManager.remove(templateField);
        }

        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/template/template-field-list.do";
    }

    // ~ ======================================================================
    @Resource
    public void setTemplateFieldManager(
            TemplateFieldManager templateFieldManager) {
        this.templateFieldManager = templateFieldManager;
    }

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
