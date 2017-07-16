package com.casic.patrol.party.support;

import java.util.ArrayList;
import java.util.List;

import com.casic.patrol.party.persistence.domain.PartyEntity;

public class PartyEntityConverter {
    public List<PartyEntityDTO> createPartyEntityDtos(
            List<PartyEntity> partyEntities) {
        List<PartyEntityDTO> partyEntityDtos = new ArrayList<PartyEntityDTO>();

        for (PartyEntity partyEntity : partyEntities) {
            PartyEntityDTO partyEntityDto = new PartyEntityDTO();
            partyEntityDto.setId(partyEntity.getId());
            partyEntityDto.setType(partyEntity.getPartyType().getName());
            partyEntityDto.setName(partyEntity.getName());
            partyEntityDto.setRegion(partyEntity.getRegion());
            partyEntityDtos.add(partyEntityDto);
        }

        return partyEntityDtos;
    }

    public PartyEntityDTO createPartyEntityDto(
            PartyEntity partyEntity) {
        PartyEntityDTO partyEntityDto = new PartyEntityDTO();
        partyEntityDto.setId(partyEntity.getId());
        partyEntityDto.setType(partyEntity.getPartyType().getName());
        partyEntityDto.setName(partyEntity.getName());
        partyEntityDto.setRegion(partyEntity.getRegion());
        partyEntityDto.setRef(partyEntity.getRef());

        return partyEntityDto;
    }
}
