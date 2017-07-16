package com.casic.patrol.user.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.util.DateUtils;
import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.user.domain.Position;
import com.casic.patrol.user.domain.User;
import com.casic.patrol.user.dto.PositionDto;
import com.casic.patrol.user.dto.TaskTraceDto;
import com.casic.patrol.util.DataTable;
import com.casic.patrol.util.DataTableParameter;
import com.casic.patrol.util.DataTableUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@Service
public class PositionManager extends HibernateEntityDao<Position> {

    @Resource
    private UserManager userManager;

    public boolean savePosition(PositionDto positionDto) {
        try {
            //TODO LIST:position
            Position position = new Position();
            position.setLatitude(positionDto.getLatitude().equalsIgnoreCase("") ? 0 : Double.valueOf(positionDto.getLatitude()));
            position.setLongitude(positionDto.getLongitude().equalsIgnoreCase("") ? 0 : Double.valueOf(positionDto.getLongitude()));

//            position.setLocaTime(DateUtils.sdf4.parse(positionDto.getLocaTime()));
            position.setLocaTime(new Date());
            User user = userManager.get(Long.parseLong(positionDto.getUser_id()));
            position.setUser(user);
            save(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PositionDto> findPositions(User user, String locaTime) throws ParseException {
        if (user == null || StringUtils.isBlank(locaTime)) {
            return Collections.emptyList();
        }

        Date date = DateUtils.sdf1.parse(locaTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        Date dayAfter = calendar.getTime();
        Criteria criteria = this.getSession().createCriteria(Position.class);
        criteria.add(Restrictions.eq("user", user))
                .add(Restrictions.ge("locaTime", date))
                .add(Restrictions.lt("locaTime", dayAfter))
                .addOrder(Order.asc("locaTime"));
        List<Position> positionList = criteria.list();
        List<PositionDto> result = new ArrayList<PositionDto>();
        for (Position position : positionList) {
            PositionDto positionDto = new PositionDto(position);
            result.add(positionDto);
        }
        return result;
    }

    public List<PositionDto> findPositionsByTimeRange(User user, String startTime,String endTime) throws ParseException {
        if (user == null || StringUtils.isBlank(startTime)|| StringUtils.isBlank(endTime)) {
            return Collections.emptyList();
        }

        Date startDate = DateUtils.sdf4.parse(startTime);
        Date endDate = DateUtils.sdf4.parse(endTime);

        Criteria criteria = this.getSession().createCriteria(Position.class);
        criteria.add(Restrictions.eq("user", user))
                .add(Restrictions.ge("locaTime", startDate))
                .add(Restrictions.lt("locaTime", endDate))
                .addOrder(Order.asc("locaTime"));
        List<Position> positionList = criteria.list();
        List<PositionDto> result = new ArrayList<PositionDto>();
        for (Position position : positionList) {
            PositionDto positionDto = new PositionDto(position);
            result.add(positionDto);
        }
        return result;
    }
}

