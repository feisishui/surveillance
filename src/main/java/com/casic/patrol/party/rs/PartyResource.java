package com.casic.patrol.party.rs;

import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.api.user.UserDTO;
import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.domain.PartyStruct;
import com.casic.patrol.party.persistence.domain.PartyType;
import com.casic.patrol.party.persistence.manager.PartyEntityManager;
import com.casic.patrol.party.persistence.manager.PartyStructManager;
import com.casic.patrol.party.persistence.manager.PartyTypeManager;
import com.casic.patrol.party.service.PartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("party")
public class PartyResource {
    private static Logger logger = LoggerFactory.getLogger(PartyResource.class);
    private PartyTypeManager partyTypeManager;
    private PartyEntityManager partyEntityManager;
    private PartyStructManager partyStructManager;
    private PartyService partyService;
    private UserConnector userConnector;

    @GET
    @Path("types")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartyTypeDTO> getAllPartyTypes() {
        List<PartyType> partyTypes = partyTypeManager.getAll();

        List<PartyTypeDTO> partyTypeDtos = new ArrayList<PartyTypeDTO>();

        for (PartyType partyType : partyTypes) {
            PartyTypeDTO partyTypeDto = new PartyTypeDTO();
            partyTypeDto.setId(partyType.getId());
            partyTypeDto.setName(partyType.getName());
            partyTypeDtos.add(partyTypeDto);
        }

        return partyTypeDtos;
    }

    @GET
    @Path("entities")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartyEntityDTO> getPartyEntitiesByType(
            @QueryParam("typeId") long typeId) {
        List<PartyEntity> partyEntities = partyEntityManager.findBy(
                "partyType.id", typeId);

        List<PartyEntityDTO> partyEntityDtos = new ArrayList<PartyEntityDTO>();

        for (PartyEntity partyEntity : partyEntities) {
            if (partyEntity.getParentStructs().size() == 1) {
                PartyStruct partyStruct = partyEntity.getParentStructs()
                        .iterator().next();

                if (partyStruct.getParentEntity() == null) {
                    logger.info("skip top entity : {}, {}",
                            partyEntity.getId(), partyEntity.getName());

                    continue;
                }
            }

            PartyEntityDTO partyEntityDto = new PartyEntityDTO();
            partyEntityDto.setId(partyEntity.getId());
            partyEntityDto.setName(partyEntity.getName());
            partyEntityDto.setRef(partyEntity.getRef());
            partyEntityDtos.add(partyEntityDto);
        }

        return partyEntityDtos;
    }

    @POST
    @Path("startFormtree")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map> startFormtree(
            @QueryParam("partyStructTypeId") long partyStructTypeId) {
      /*  List<PartyEntity> partyEntities = partyService
                .getTopPartyEntities(partyStructTypeId);*/
        List<PartyEntity> partyEntities = new ArrayList<PartyEntity>();
        partyEntities.add(partyService.getChiildEntityByNames("监督中心", "监督中心坐席"));
        //TODO LIST:寻找监督中心，下面的巡查部门下面的所有
        return generatePartyEntities(partyEntities, partyStructTypeId);
    }

    @POST
    @Path("taskFormTree")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map> taskFormTree(
            @QueryParam("partyStructTypeId") long partyStructTypeId,
            @QueryParam("taskName") String taskName) throws UnsupportedEncodingException {

        //TODO LIST:根据Task不同展示对应的组织结构
        taskName = URLDecoder.decode(taskName, "UTF-8");
        if (StringUtils.isBlank(taskName)) {
            return new ArrayList<Map>();
        }
        List<PartyEntity> partyEntities = new ArrayList<PartyEntity>();

        if (taskName.equals("事故处理")|| taskName.equals("事故上报")) {
            //指挥中心->指挥中心坐席
            partyEntities.add(partyService.getChiildEntityByNames("指挥中心", "指挥中心坐席"));

        } else if (taskName.equals( "事故立案")) {
            //专业公司->专业公司坐席
            partyEntities.add(partyService.getEntityByNames("专业公司"));

        } else if (taskName.equals("指派核实")) {
            //监督中心->巡查部门
            partyEntities.add(partyService.getChiildEntityByNames("监督中心", "巡查部门"));

        }
        else {
           partyEntities = partyService
                    .getTopPartyEntities(partyStructTypeId);
        }
        return generatePartyEntities(partyEntities, partyStructTypeId);
    }

    @POST
    @Path("tree")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map> tree(
            @QueryParam("partyStructTypeId") long partyStructTypeId) {
        List<PartyEntity> partyEntities = partyService
                .getTopPartyEntities(partyStructTypeId);
        return generatePartyEntities(partyEntities, partyStructTypeId);
    }

    public List<Map> generatePartyEntities(List<PartyEntity> partyEntities,
            long partyStructTypeId) {
        if (partyEntities == null) {
            return null;
        }

        List<Map> list = new ArrayList<Map>();

        try {
            for (PartyEntity partyEntity : partyEntities) {
                list.add(generatePartyEntity(partyEntity, partyStructTypeId));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return list;
    }

    public Map<String, Object> generatePartyEntity(PartyEntity partyEntity,
            long partyStructTypeId) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            map.put("id", partyEntity.getId());
            map.put("name", partyEntity.getName());
            map.put("ref", partyEntity.getRef());

            List<PartyStruct> partyStructs = partyStructManager.find(
                    "from PartyStruct where parentEntity=? order by priority",
                    partyEntity);
            List<PartyEntity> partyEntities = new ArrayList<PartyEntity>();

            for (PartyStruct partyStruct : partyStructs) {
                if (partyStruct.getPartyStructType().getId() == partyStructTypeId) {
                    PartyEntity childPartyEntity = partyStruct.getChildEntity();

                    if (childPartyEntity == null) {
                        logger.info("child party entity is null");

                        continue;
                    }

                    if (childPartyEntity.getPartyType().getType() != 1) {
                        partyEntities.add(childPartyEntity);
                    }
                }
            }

            if (partyEntities.isEmpty()) {
                map.put("open", false);
            } else {
                map.put("open", true);
                map.put("children",
                        generatePartyEntities(partyEntities, partyStructTypeId));
            }

            return map;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);

            return map;
        }
    }

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> search(@QueryParam("name") String name,
            @QueryParam("partyTypeId") long partyTypeId) {
        List<String> names = partyEntityManager
                .find("select name from PartyEntity where name like ? and partyType.id=?",
                        "%" + name + "%", partyTypeId);

        return names;
    }

    @GET
    @Path("searchUser")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, String>> searchUser(
            @QueryParam("parentId") Long parentId) {
        String hql = "select child from PartyEntity child join child.parentStructs parent where child.partyType.id=1 and parent.parentEntity.id=?";
        List<PartyEntity> partyEntities = partyEntityManager
                .find(hql, parentId);

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        for (PartyEntity partyEntity : partyEntities) {
            Map<String, String> map = new HashMap<String, String>();
            UserDTO userDto = userConnector.findById(partyEntity.getRef());
            if (userDto!=null) {
                map.put("id", userDto.getId());
                map.put("username", userDto.getUsername());
                map.put("displayName", userDto.getDisplayName());
                list.add(map);
            }
        }

        return list;
    }

    // ~ ==================================================
    @Resource
    public void setPartyTypeManager(PartyTypeManager partyTypeManager) {
        this.partyTypeManager = partyTypeManager;
    }

    @Resource
    public void setPartyEntityManager(PartyEntityManager partyEntityManager) {
        this.partyEntityManager = partyEntityManager;
    }

    @Resource
    public void setPartyStructManager(PartyStructManager partyStructManager) {
        this.partyStructManager = partyStructManager;
    }

    @Resource
    public void setPartyService(PartyService partyService) {
        this.partyService = partyService;
    }

    @Resource
    public void setUserConnector(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    // ~ ==================================================
    public static class PartyTypeDTO {
        private long id;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class PartyEntityDTO {
        private long id;
        private String name;
        private String ref;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }
    }
}
