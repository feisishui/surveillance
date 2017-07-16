package com.casic.patrol.region.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.region.domain.Region;
import com.casic.patrol.region.dto.RegionDTO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionManager extends HibernateEntityDao<Region> {

    public List<RegionDTO> findAllRegions() {
        Criteria criteria = getSession().createCriteria(Region.class);
        criteria.add(Restrictions.eq("status", 1));
        return RegionDTO.ConvertDTOs((List<Region>)criteria.list());
    }

    public Region getRegionByName(String name) {
        Criteria criteria = getSession().createCriteria(Region.class);
        criteria.add(Restrictions.eq("name", name));
        criteria.add(Restrictions.eq("status", 1));
        List<Region> regions = criteria.list();
        if (regions.size() > 0) {
            return regions.get(0);
        }
        return null;
    }
}

