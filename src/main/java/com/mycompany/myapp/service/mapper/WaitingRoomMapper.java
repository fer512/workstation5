package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Branch;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.WaitingRoom;
import com.mycompany.myapp.domain.WaitingRoomAttencionChannel;
import com.mycompany.myapp.service.dto.BranchDTO;
import com.mycompany.myapp.service.dto.CompanyDTO;
import com.mycompany.myapp.service.dto.WaitingRoomAttencionChannelDTO;
import com.mycompany.myapp.service.dto.WaitingRoomDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WaitingRoom} and its DTO {@link WaitingRoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface WaitingRoomMapper extends EntityMapper<WaitingRoomDTO, WaitingRoom> {
    @Mapping(target = "attencionChannel", source = "attencionChannel", qualifiedByName = "waitingRoomAttencionChannelId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    @Mapping(target = "branches", source = "branches", qualifiedByName = "branchIdSet")
    WaitingRoomDTO toDto(WaitingRoom s);

    @Mapping(target = "removeBranch", ignore = true)
    WaitingRoom toEntity(WaitingRoomDTO waitingRoomDTO);

    @Named("waitingRoomAttencionChannelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WaitingRoomAttencionChannelDTO toDtoWaitingRoomAttencionChannelId(WaitingRoomAttencionChannel waitingRoomAttencionChannel);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);

    @Named("branchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchDTO toDtoBranchId(Branch branch);

    @Named("branchIdSet")
    default Set<BranchDTO> toDtoBranchIdSet(Set<Branch> branch) {
        return branch.stream().map(this::toDtoBranchId).collect(Collectors.toSet());
    }
}
