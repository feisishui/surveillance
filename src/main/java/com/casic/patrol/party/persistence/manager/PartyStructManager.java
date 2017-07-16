package com.casic.patrol.party.persistence.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.domain.PartyStruct;
import com.casic.patrol.party.persistence.domain.PartyType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartyStructManager extends HibernateEntityDao<PartyStruct> {

    public List<PartyEntity> getChilePartiesByParent(PartyEntity parentPartyEntity) {

        List<PartyEntity> childPartyEntities = new ArrayList<PartyEntity>();
        Criteria criteria = this.createCriteria(PartyStruct.class);
        criteria.add(Restrictions.eq("parentEntity", parentPartyEntity));
        List<PartyStruct> partyStructs = criteria.list();
        if (partyStructs != null && partyStructs.size() != 0) {
            for (PartyStruct partyStruct:partyStructs)
            {
                PartyEntity childEntity = partyStruct.getChildEntity();
                if (childEntity!=null)
                    childPartyEntities.add(partyStruct.getChildEntity());
            }
        }
        return childPartyEntities;
    }

    public List<PartyEntity> getParentPartiesByChild(PartyEntity childPartyEntity) {

        List<PartyEntity> parentPartyEntities = new ArrayList<PartyEntity>();
        Criteria criteria = this.createCriteria(PartyStruct.class);
        criteria.add(Restrictions.eq("childEntity", childPartyEntity));
        List<PartyStruct> partyStructs = criteria.list();
        if (partyStructs != null && partyStructs.size() != 0) {
            for (PartyStruct partyStruct:partyStructs)
            {
                PartyEntity parentEntity = partyStruct.getParentEntity();
                if (parentEntity!=null)
                    parentPartyEntities.add(parentEntity);
            }
        }
        return parentPartyEntities;
    }



    public List<PartyEntity> getChildPartiesByParentsAndRegion(List<PartyEntity> partyEntities,Long partyTypeId,String region) {

        List<PartyEntity> childPartyEntities = new ArrayList<PartyEntity>();
        Criteria criteria = this.createCriteria(PartyStruct.class);
        criteria.add(Restrictions.in("parentEntity", partyEntities));
        List<PartyStruct> partyStructs = criteria.list();
        if (partyStructs != null && partyStructs.size() != 0) {
            for (PartyStruct partyStruct:partyStructs)
            {
                PartyEntity childEntity = partyStruct.getChildEntity();
                if (childEntity == null)
                    continue;
                if (childEntity.getPartyType().getId()!=partyTypeId)
                    continue;
                if (region!=null&&(!region.equals(""))&&(!region.equals("不限")))
                {
                    if (!childEntity.getRegion().equals(region))
                        continue;
                }
                childPartyEntities.add(partyStruct.getChildEntity());
            }
        }
        return childPartyEntities;
    }

}
