package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Branch;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.WorkerProfile;
import com.mycompany.myapp.domain.WorkerProfileAttencionChannel;
import com.mycompany.myapp.service.dto.BranchDTO;
import com.mycompany.myapp.service.dto.CompanyDTO;
import com.mycompany.myapp.service.dto.WorkerProfileAttencionChannelDTO;
import com.mycompany.myapp.service.dto.WorkerProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkerProfile} and its DTO {@link WorkerProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkerProfileMapper extends EntityMapper<WorkerProfileDTO, WorkerProfile> {
    @Mapping(target = "attencionChannel", source = "attencionChannel", qualifiedByName = "workerProfileAttencionChannelId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    @Mapping(target = "branches", source = "branches", qualifiedByName = "branchIdSet")
    WorkerProfileDTO toDto(WorkerProfile s);

    @Mapping(target = "removeBranches", ignore = true)
    WorkerProfile toEntity(WorkerProfileDTO workerProfileDTO);

    @Named("workerProfileAttencionChannelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkerProfileAttencionChannelDTO toDtoWorkerProfileAttencionChannelId(WorkerProfileAttencionChannel workerProfileAttencionChannel);

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
