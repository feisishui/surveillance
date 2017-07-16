package com.casic.patrol.party.persistence.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PartyEntityManager extends HibernateEntityDao<PartyEntity> {

    @Resource
    private PartyStructManager partyStructManager;

    public List<PartyEntity> getPartyEntities() {

        Criteria criteria = this.createCriteria(PartyEntity.class);
        List<PartyEntity> partyEntities = criteria.list();
        if (partyEntities == null || partyEntities.size() == 0) {
            return Collections.emptyList();
        }
        return partyEntities;
    }

    public List<PartyEntity> getPerson() {

        Criteria criteria = this.createCriteria(PartyEntity.class);
        criteria.add(Restrictions.eq("partyType.id", Long.parseLong("1")));
        List<PartyEntity> partyEntities = criteria.list();
        if (partyEntities == null || partyEntities.size() == 0) {
            return Collections.emptyList();
        }
        return partyEntities;
    }

    public List<PartyEntity> getPost() {

        Criteria criteria = this.createCriteria(PartyEntity.class);
        criteria.add(Restrictions.eq("partyType.id", Long.parseLong("3")));
        List<PartyEntity> partyEntities = criteria.list();
        if (partyEntities == null || partyEntities.size() == 0) {
            return Collections.emptyList();
        }
        return partyEntities;
    }

    public PartyEntity getPartyEntityByName(String name) {

        Criteria criteria = this.createCriteria(PartyEntity.class);
        criteria.add(Restrictions.eq("name", name));
        List<PartyEntity> partyEntities = criteria.list();
        if (partyEntities == null || partyEntities.size() == 0) {
            return null;
        }
        return partyEntities.get(0);
    }

    public List<PartyEntity> getPartyEntitiesByNames(List<String> names) {

        Criteria criteria = this.createCriteria(PartyEntity.class);
        criteria.add(Restrictions.in("name", names));
        List<PartyEntity> partyEntities = criteria.list();
        if (partyEntities == null || partyEntities.size() == 0) {
            return null;
        }
        return partyEntities;
    }

    public List<PartyEntity> getPartyEntitiesByIds(Long[] ids) {

        Criteria criteria = this.createCriteria(PartyEntity.class);
        criteria.add(Restrictions.in("id", ids));
        List<PartyEntity> partyEntities = criteria.list();
        if (partyEntities == null || partyEntities.size() == 0) {
            return null;
        }
        return partyEntities;
    }

    public PartyEntity getPartyEntityByRef(String ref, String tenantId) {
        String hql = "from PartyEntity t where t.ref=? and t.partyType.name=? and t.tenantId=?";
        List<PartyEntity> partyEntities = (List<PartyEntity>)find(hql, ref, "人员", tenantId);
        if (partyEntities == null || partyEntities.size() == 0) {
            return null;
        }
        return partyEntities.get(0);
    }


    public List<PartyEntity> getPersonByDepartmentAndRegion(String department,String region) {

        List<PartyEntity> parentPartyEntities=new ArrayList<PartyEntity>();
        List<PartyEntity> childPartyEntities = new ArrayList<PartyEntity>();

        long partyTypeId = 1;
        if (department.equals("指挥中心"))
        {
            parentPartyEntities.add(this.getPartyEntityByName("指挥中心坐席"));
            childPartyEntities = partyStructManager.getChildPartiesByParentsAndRegion(parentPartyEntities,partyTypeId,region);
        }
        else if (department.equals("巡查部门"))
        {
            parentPartyEntities.add(this.getPartyEntityByName("巡查部门"));
            childPartyEntities =partyStructManager.getChildPartiesByParentsAndRegion(parentPartyEntities,partyTypeId,region);
        }
        else if (department.equals("专业公司"))
        {
            List<String> names = new ArrayList<String>();
            names.add("自来水公司坐席");
            names.add("自来水公司养护队伍");
            names.add("燃气公司坐席");
            names.add("燃气公司养护队伍");
            names.add("排水公司坐席");
            names.add("排水公司养护队伍");
            names.add("热力公司坐席");
            names.add("热力公司养护队伍");
            names.add("电力公司坐席");
            names.add("电力公司养护队伍");
            parentPartyEntities = this.getPartyEntitiesByNames(names);
            childPartyEntities =partyStructManager.getChildPartiesByParentsAndRegion(parentPartyEntities,partyTypeId,region);
        }
        else if (department.equals("自来水公司"))
        {
            List<String> names = new ArrayList<String>();
            names.add("自来水公司坐席");
            names.add("自来水公司养护队伍");
            parentPartyEntities = this.getPartyEntitiesByNames(names);
            childPartyEntities =partyStructManager.getChildPartiesByParentsAndRegion(parentPartyEntities,partyTypeId,region);
        }
        else if (department.equals("燃气公司"))
        {
            List<String> names = new ArrayList<String>();
            names.add("燃气公司坐席");
            names.add("燃气公司养护队伍");
            parentPartyEntities = this.getPartyEntitiesByNames(names);
            childPartyEntities =partyStructManager.getChildPartiesByParentsAndRegion(parentPartyEntities,partyTypeId,region);
        }
        else if (department.equals("排水公司"))
        {
            List<String> names = new ArrayList<String>();
            names.add("排水公司坐席");
            names.add("排水公司养护队伍");
            parentPartyEntities = this.getPartyEntitiesByNames(names);
            childPartyEntities =partyStructManager.getChildPartiesByParentsAndRegion(parentPartyEntities,partyTypeId,region);
        }
        else if (department.equals("热力公司"))
        {
            List<String> names = new ArrayList<String>();
            names.add("热力公司坐席");
            names.add("热力公司养护队伍");
            parentPartyEntities = this.getPartyEntitiesByNames(names);
            childPartyEntities =partyStructManager.getChildPartiesByParentsAndRegion(parentPartyEntities,partyTypeId,region);
        }
        else if (department.equals("电力公司"))
        {
            List<String> names = new ArrayList<String>();
            names.add("电力公司坐席");
            names.add("电力公司养护队伍");
            parentPartyEntities = this.getPartyEntitiesByNames(names);
            childPartyEntities =partyStructManager.getChildPartiesByParentsAndRegion(parentPartyEntities,partyTypeId,region);
        }
        return childPartyEntities;
    }

}
