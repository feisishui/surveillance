package com.casic.patrol.user.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.user.domain.Role;
import com.casic.patrol.user.domain.User;
import com.casic.patrol.user.dto.UserDto;
import com.casic.patrol.util.DataTable;
import com.casic.patrol.util.DataTableParameter;
import com.casic.patrol.util.DataTableUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 2016/4/20.
 */
@Service("userInfoManager")
public class UserManager extends HibernateEntityDao<User> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RoleManager roleManager;

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    public User AppLogin(User user) {
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.eq("userName", user.getUserName()));
        criteria.add(Restrictions.eq("password", user.getPassword()));
        criteria.createAlias("role","role");
        criteria.add(Restrictions.eq("role.roleName", "巡查部门"));
        criteria.add(Restrictions.eq("isValid", 1));
        if (CollectionUtils.isEmpty(criteria.list())) {
            return null;
        }
        return (User) criteria.list().get(0);
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    public User login(User user) {
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.eq("userName", user.getUserName()));
        criteria.add(Restrictions.eq("password", user.getPassword()));
        criteria.add(Restrictions.eq("isValid", 1));
        if (CollectionUtils.isEmpty(criteria.list())) {
            return null;
        }
        return (User) criteria.list().get(0);
    }

    /**
     * 以分页方式获取用户列表
     *
     * @param params json格式的请求参数
     * @param user   登录用户的信息
     * @return
     */
    public DataTable<UserDto> pageUser(String params, User user) {
        DataTable<UserDto> result = new DataTable<UserDto>();
        DataTableParameter parameter = DataTableUtils.getDataTableParameterByJsonParam(params);
        int start = parameter.getiDisplayStart();
        int pageSize = parameter.getiDisplayLength();
        int pageNo = (start / pageSize) + 1;
        Criteria criteria = this.createCriteria(User.class);
        criteria.addOrder(Order.desc("id"));
        criteria.add(Restrictions.eq("isValid", 1));
        if (StringUtils.isNotBlank(parameter.getsSearch())) {
            criteria.add(Restrictions.like("userName", "%" + parameter.getsSearch() + "%"));
        }
        Page page = pagedQuery(criteria, pageNo, pageSize);
        List<UserDto> userDtos = UserDto.Converts((List<User>) page.getResult());
        result.setAaData(userDtos);
        result.setiTotalDisplayRecords((int) page.getTotalCount());
        result.setiTotalRecords((int) page.getTotalCount());
        result.setsEcho(parameter.getsEcho());
        return result;
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    public UserDto getUserDtoById(Long userId) {
        User user = getUserById(userId);
        return UserDto.Convert(user);
    }

    public User getUserById(Long userId) {
        if (userId == null) {//参数不合法
            logger.error("ERROR,UserInfoManager's method -> getUserInfoById(Long userId) ,the parameter userId is null");
            return null;
        }
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.eq("id", userId)).add(Restrictions.eq("isValid", 1));
        User user = (User) criteria.uniqueResult();
        return user;
    }

    /**
     * 保存或更新用户信息
     *
     * @param user
     */
    public void saveUser(User user, HttpSession session) {
        if (user == null) {
            logger.error("UserInfoService->saveUserInfo ERROR,userInfo is null!");
            return;
        }
        if (user.getId() == null) {//新增
            user.setIsValid(1);
        }
        this.getSession().saveOrUpdate(user);
    }

    /**
     * 删除用户
     *
     * @param userId
     */
    public boolean deleteUserById(Long userId) {
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.eq("id", userId));
        if (CollectionUtils.isEmpty(criteria.list())) {
            return false;
        }
        try {
            User user = (User) criteria.list().get(0);
            user.setIsValid(0);
            save(user);
        } catch (Exception e) {
            return false;
        }

        return true;
    }


    /**
     * @param user
     * @param newPassword
     */
    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword.trim());
        save(user);
    }

    /**
     * 根据用户名查询用户，用以限制唯一用户名
     *
     * @param userName
     * @return
     */
    public User getUserByName(String userName) {
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.eq("userName", userName));
        criteria.add(Restrictions.eq("isValid", 1));
        List<User> users = criteria.list();
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public List<User> getUsers() {
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.eq("isValid", 1));
        return criteria.list();
    }

    /**
     * 根据用户名查询用户，用以限制唯一用户名
     *
     * @param userName
     * @return
     */
    public User getPatrolerByName(String userName, Role role) {
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.eq("userName", userName));
//        criteria.add(Restrictions.eq("role", role));
        criteria.add(Restrictions.eq("isValid", 1));
        List<User> users = criteria.list();
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public List<User> getPatrolers(Role role) {
        Criteria criteria = this.createCriteria(User.class);
//        criteria.add(Restrictions.eq("role", role));
        criteria.add(Restrictions.eq("isValid", 1));
        List<User> users = criteria.list();
        return users;
    }

    public List<User> getUserByParties(List<PartyEntity> partyEntities) {
        List<Long> ids =new ArrayList<Long>();
        for (PartyEntity partyEntity:partyEntities)
        {
            String id = partyEntity.getRef();
            if (id!=null)
            {
                ids.add(Long.parseLong(id));
            }
        }
        if (ids == null||ids.size()==0)
        {
            return null;
        }
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.in("id", ids));
        List<User> users = criteria.list();
        return users;
    }

    public List<User> getUserByRoleName(String roleName) {
        Role role = roleManager.getRoleByName(roleName);
        if(role == null) {
            logger.error("RoleName为：--" + roleName + "--的用户不存在");
            return Collections.emptyList();
        }
        Criteria criteria = this.createCriteria(User.class);
        criteria.add(Restrictions.eq("role", role)).add(Restrictions.eq("isValid", 1));
        return criteria.list();
    }

    public String getNameById(long id)
    {
        User user = this.get(id);
        if (user!=null)
            return user.getUserName();
        return null;
    }
}
