package com.casic.patrol.party.web;

import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.export.Exportor;
import com.casic.patrol.core.export.TableModel;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.query.PropertyFilter;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.domain.PartyType;
import com.casic.patrol.party.persistence.manager.PartyEntityManager;
import com.casic.patrol.party.persistence.manager.PartyStructManager;
import com.casic.patrol.party.persistence.manager.PartyTypeManager;
import com.casic.patrol.party.support.PartyEntityConverter;
import com.casic.patrol.party.support.PartyEntityDTO;
import com.casic.patrol.user.domain.User;
import com.casic.patrol.user.dto.UserDto;
import com.casic.patrol.user.manager.UserManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("party")
public class PartyEntityController {
    private PartyEntityManager partyEntityManager;
    private PartyTypeManager partyTypeManager;
    private MessageHelper messageHelper;
    private PartyEntityConverter partyEntityConverter = new PartyEntityConverter();
    private Exportor exportor;
    private BeanMapper beanMapper = new BeanMapper();
    private TenantHolder tenantHolder;
    private UserManager userManager;

    @RequestMapping("party-entity-list")
    public String list(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        String tenantId = tenantHolder.getTenantId();
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        propertyFilters.add(new PropertyFilter("EQS_tenantId", tenantId));
        page = partyEntityManager.pagedQuery(page, propertyFilters);
        model.addAttribute("page", page);

        return "party/party-entity-list";
    }

    @RequestMapping("party-entity-input")
    public String input(@RequestParam(value = "id", required = false) Long id,
            Model model) {
        String tenantId = tenantHolder.getTenantId();

        if (id != null) {
            PartyEntity partyEntity = partyEntityManager.get(id);
            model.addAttribute("model", partyEntity);
        }

        List<PartyType> partyTypes = partyTypeManager.findBy("tenantId",
                tenantId);
        List<User> users = userManager.getUsers();
        List<UserDto> userDtos = UserDto.Converts(users);
        model.addAttribute("partyTypes", partyTypes);
        model.addAttribute("users", userDtos);

        return "party/party-entity-input";
    }

    @RequestMapping("party-entity-save")
    public String save(@ModelAttribute PartyEntity partyEntity,
            @RequestParam("partyTypeId") Long partyTypeId,
            RedirectAttributes redirectAttributes) {
        String tenantId = tenantHolder.getTenantId();
        PartyEntity dest = null;
        Long id = partyEntity.getId();

        if (id != null) {
            dest = partyEntityManager.get(id);
            beanMapper.copy(partyEntity, dest);
        } else {
            dest = partyEntity;
            dest.setTenantId(tenantId);
        }

        dest.setPartyType(partyTypeManager.get(partyTypeId));
        partyEntityManager.save(dest);

        messageHelper.addFlashMessage(redirectAttributes, "core.success.save",
                "保存成功");

        return "redirect:/party/party-entity-list.do";
    }

    @RequestMapping("party-entity-remove")
    public String remove(@RequestParam("selectedItem") List<Long> selectedItem,
            RedirectAttributes redirectAttributes) {
        partyEntityManager
                .removeAll(partyEntityManager.findByIds(selectedItem));
        messageHelper.addFlashMessage(redirectAttributes,
                "core.success.delete", "删除成功");

        return "redirect:/party/party-entity-list.do";
    }

    @RequestMapping("party-entity-export")
    public void export(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<PropertyFilter> propertyFilters = PropertyFilter
                .buildFromMap(parameterMap);
        page = partyEntityManager.pagedQuery(page, propertyFilters);

        List<PartyEntity> partyEntities = (List<PartyEntity>) page.getResult();
        List<PartyEntityDTO> partyDtos = partyEntityConverter
                .createPartyEntityDtos(partyEntities);
        TableModel tableModel = new TableModel();
        tableModel.setName("party entity");
        tableModel.addHeaders("id", "type", "code", "name");
        tableModel.setData(partyDtos);
        exportor.export(request, response, tableModel);
    }


    @RequestMapping("get-person")
    @ResponseBody
    public Map<String, Object>  getPersonByDepartmentAndRegion(
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "department", required = false) String department) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<PartyEntityDTO> partyEntityDTO=new ArrayList<PartyEntityDTO>();
        List<PartyEntity> childPartyEntities=new ArrayList<PartyEntity>();
        try{
            if (department==null)
                return null;

            childPartyEntities = partyEntityManager.getPersonByDepartmentAndRegion(department,region);
            if (childPartyEntities!=null)
            {
                partyEntityDTO = partyEntityConverter.createPartyEntityDtos(childPartyEntities);
                map.put("success", true);
            }
            map.put("result",partyEntityDTO);

        }catch (Exception e){
            e.printStackTrace();
            map.put("success", false);
        }
        return map;
    }

    // ~ ======================================================================
    @Resource
    public void setPartyEntityManager(PartyEntityManager partyEntityManager) {
        this.partyEntityManager = partyEntityManager;
    }

    @Resource
    public void setPartyTypeManager(PartyTypeManager partyTypeManager) {
        this.partyTypeManager = partyTypeManager;
    }

    @Resource
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
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
