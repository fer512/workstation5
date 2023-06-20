package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Branch;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.WaitingRoom;
import com.mycompany.myapp.domain.Worker;
import com.mycompany.myapp.service.dto.BranchDTO;
import com.mycompany.myapp.service.dto.CompanyDTO;
import com.mycompany.myapp.service.dto.WaitingRoomDTO;
import com.mycompany.myapp.service.dto.WorkerDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Worker} and its DTO {@link WorkerDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkerMapper extends EntityMapper<WorkerDTO, Worker> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    @Mapping(target = "branches", source = "branches", qualifiedByName = "branchIdSet")
    @Mapping(target = "waitingRoom", source = "waitingRoom", qualifiedByName = "waitingRoomId")
    WorkerDTO toDto(Worker s);

    @Mapping(target = "removeBranch", ignore = true)
    Worker toEntity(WorkerDTO workerDTO);

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

    @Named("waitingRoomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WaitingRoomDTO toDtoWaitingRoomId(WaitingRoom waitingRoom);
}
