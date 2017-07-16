package com.casic.patrol.party.persistence.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;

import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.domain.PartyType;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyTypeManager extends HibernateEntityDao<PartyType> {

    public PartyType getPartyTypeByName(String name) {

        Criteria criteria = this.createCriteria(PartyType.class);
        criteria.add(Restrictions.eq("name", name));
        List<PartyType> partyTypes = criteria.list();
        if (partyTypes == null || partyTypes.size() == 0) {
            return null;
        }
        return partyTypes.get(0);
    }
}
