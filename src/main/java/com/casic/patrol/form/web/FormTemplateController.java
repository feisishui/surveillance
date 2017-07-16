package com.casic.patrol.form.web;

import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.auth.CurrentUserHolder;
import com.casic.patrol.core.auth.MockCurrentUserHolder;
import com.casic.patrol.core.export.Exportor;
import com.casic.patrol.core.export.TableModel;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.query.PropertyFilter;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.form.persistence.domain.FormTemplate;
import com.casic.patrol.form.persistence.manager.FormTemplateManager;
import com.casic.patrol.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("form")
public class FormTemplateController {
    private static Logger logger = LoggerFactory
            .getLogger(FormTemplateController.class);
    private FormTemplateManager formTemplateManager;
    private Exportor exportor;
    private BeanMapper beanMapper = new BeanMapper();
    private JsonMapper jsonMapper = new JsonMapper();
    private MessageHelper messageHelper;
    private MultipartResolver multipartResolver;
    private TenantHolder tenantHolder;

    @RequestMapping("form-template-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        propertyFilters.add(new PropertyFilter("EQS_tenantId", tenantId));
        page = formTemplateManager.pagedQuery(page, propertyFilters);
        model.addAttribute("page", page);

        return "form/form-template-list";
    }

    @RequestMapping("form-template-input")
    public String input(@RequestParam(value = "id", required = false) Long id,
            Model model) {
        if (id != null) {
            FormTemplate formTemplate = formTemplateManager.get(id);
            model.addAttribute("model", formTemplate);
        }

        return "form/form-template-input";
    }

    @RequestMapping("form-template-save")
    public String save(@ModelAttribute FormTemplate formTemplate,
            @RequestParam Map<String, Object> parameterMap,
            RedirectAttributes redirectAttributes) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());
        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        FormTemplate dest = null;
        Long id = formTemplate.getId();

        if (id != null) {
            dest = formTemplateManager.get(id);
            beanMapper.copy(formTemplate, dest);
        } else {
            dest = formTemplate;
            dest.setType(0);
            dest.setCreateTime(new Date());
            dest.setUserId(userId);
            dest.setTenantId(tenantId);
        }

        formTemplateManager.save(dest);

        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/form/form-template-list.do";
    }

    @RequestMapping("form-template-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        List<FormTemplate> formTemplates = formTemplateManager
                .findByIds(selectedItem);

        formTemplateManager.removeAll(formTemplates);
        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/form/form-template-list.do";
    }

    @RequestMapping("form-template-export")
    public void export(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String tenantId = tenantHolder.getTenantId();
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        propertyFilters.add(new PropertyFilter("EQS_tenantId", tenantId));
        page = formTemplateManager.pagedQuery(page, propertyFilters);

        List<FormTemplate> dynamicModels = (List<FormTemplate>) page
                .getResult();

        TableModel tableModel = new TableModel();
        tableModel.setName("dynamic model");
        tableModel.addHeaders("id", "name");
        tableModel.setData(dynamicModels);
        exportor.export(request, response, tableModel);
    }

    @Autowired
    private HttpSession session;
    @RequestMapping("form-template-copy")
    public String copy(@RequestParam("id") Long id,
            RedirectAttributes redirectAttributes) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        FormTemplate formTemplate = formTemplateManager.get(id);

        if (formTemplate == null) {
            return "redirect:/form/form-template.do";
        }

        int index = 1;
        String code = formTemplate.getCode();
        String name = formTemplate.getName();

        while (true) {
            FormTemplate targetFormTemplate = formTemplateManager.findUniqueBy(
                    "code", code + "" + index);

            if (targetFormTemplate == null) {
                code = code + "" + index;
                name = name + "" + index;

                break;
            }

            index++;
        }

        FormTemplate targetFormTemplate = new FormTemplate();
        beanMapper.copy(formTemplate, targetFormTemplate);
        targetFormTemplate.setId(null);
        targetFormTemplate.setCode(code);
        targetFormTemplate.setName(name);
        targetFormTemplate.setUserId(userId);
        targetFormTemplate.setFormSchemas(new HashSet());
        formTemplateManager.save(targetFormTemplate);

        return "redirect:/form/form-template-list.do";
    }

    // ~ ======================================================================
    @Resource
    public void setFormTemplateManager(FormTemplateManager formTemplateManager) {
        this.formTemplateManager = formTemplateManager;
    }

    @Resource
    public void setExportor(Exportor exportor) {
        this.exportor = exportor;
    }

    @Resource
    public void setMessageHelper(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    @Resource
    public void setMultipartResolver(MultipartResolver multipartResolver) {
        this.multipartResolver = multipartResolver;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }

}
