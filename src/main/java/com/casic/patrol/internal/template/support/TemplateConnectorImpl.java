package com.casic.patrol.internal.template.support;


import com.casic.patrol.api.template.TemplateConnector;
import com.casic.patrol.api.template.TemplateDTO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemplateConnectorImpl implements TemplateConnector {
    private JdbcTemplate jdbcTemplate;

    public TemplateDTO findByCode(String code, String tenantId) {
        String templateInfoSql = "select dbid,name,code from TEMPLATE_INFO where code=? and TENANT_ID=?";
        Map<String, Object> templateInfoMap = jdbcTemplate.queryForMap(
                templateInfoSql, code, tenantId);

        return this.processTemplateInfo(templateInfoMap);
    }

    public List<TemplateDTO> findAll(String tenantId) {
        String templateInfoSql = "select dbid,name,code from TEMPLATE_INFO where TENANT_ID=?";
        List<TemplateDTO> list = new ArrayList<TemplateDTO>();

        for (Map<String, Object> templateInfoMap : jdbcTemplate.queryForList(
                templateInfoSql, tenantId)) {
            list.add(this.processTemplateInfo(templateInfoMap));
        }

        return list;
    }

    public TemplateDTO processTemplateInfo(Map<String, Object> templateInfoMap) {
        TemplateDTO templateDto = new TemplateDTO();
        templateDto.setName((String) templateInfoMap.get("info_name"));
        templateDto.setCode((String) templateInfoMap.get("code"));

        String templateFieldSql = "select field_name,content from TEMPLATE_FIELD where INFO_ID=?";
        List<Map<String, Object>> templateFieldList = jdbcTemplate
                .queryForList(templateFieldSql, templateInfoMap.get("ID"));

        for (Map<String, Object> templateFieldMap : templateFieldList) {
            templateDto.getFields().put((String) templateFieldMap.get("field_name"),
                    (String) templateFieldMap.get("content"));
        }

        return templateDto;
    }

    @Resource
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
