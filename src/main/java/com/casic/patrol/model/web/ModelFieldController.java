package com.casic.patrol.model.web;

import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.export.Exportor;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.query.PropertyFilter;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.model.persistence.domain.ModelField;
import com.casic.patrol.model.persistence.manager.ModelFieldManager;
import com.casic.patrol.model.persistence.manager.ModelInfoManager;
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
@RequestMapping("model")
public class ModelFieldController {
    private ModelFieldManager modelFieldManager;
    private ModelInfoManager modelInfoManager;
    private MessageHelper messageHelper;
    private Exportor exportor;
    private BeanMapper beanMapper = new BeanMapper();
    private TenantHolder tenantHolder;

    @RequestMapping("model-field-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        propertyFilters.add(new PropertyFilter("EQS_tenantId", tenantId));
        page = modelFieldManager.pagedQuery(page, propertyFilters);
        model.addAttribute("page", page);

        return "model/model-field-list";
    }

    @RequestMapping("model-field-input")
    public String input(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "type", required = false) String whitelistTypeCode,
            Model model) {
        ModelField modelField = null;

        if (id != null) {
            modelField = modelFieldManager.get(id);
            model.addAttribute("model", modelField);
        }

        model.addAttribute("modelInfos", modelInfoManager.getAll());

        return "model/model-field-input";
    }

    @RequestMapping("model-field-save")
    public String save(@ModelAttribute ModelField modelField,
            RedirectAttributes redirectAttributes) {
        String tenantId = tenantHolder.getTenantId();
        Long id = modelField.getId();
        ModelField dest = null;

        if (id != null) {
            dest = modelFieldManager.get(id);
            beanMapper.copy(modelField, dest);
        } else {
            dest = modelField;
            dest.setTenantId(tenantId);
        }

        modelFieldManager.save(dest);

        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/model/model-field-list.do";
    }

    @RequestMapping("model-field-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        List<ModelField> modelFields = modelFieldManager
                .findByIds(selectedItem);

        for (ModelField modelField : modelFields) {
            modelFieldManager.remove(modelField);
        }

        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/model/model-field-list.do";
    }

    // ~ ======================================================================
    @Resource
    public void setModelFieldManager(ModelFieldManager modelFieldManager) {
        this.modelFieldManager = modelFieldManager;
    }

    @Resource
    public void setModelInfoManager(ModelInfoManager modelInfoManager) {
        this.modelInfoManager = modelInfoManager;
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
