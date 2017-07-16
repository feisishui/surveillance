package com.casic.patrol.region.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.region.domain.Region;
import com.casic.patrol.region.domain.RegionPoint;
import com.casic.patrol.region.dto.RegionDTO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionPointManager extends HibernateEntityDao<RegionPoint> {

    public void saveAll(List<RegionPoint> list) {
        for (RegionPoint temp : list) {
            save(temp);
        }
    }
}

