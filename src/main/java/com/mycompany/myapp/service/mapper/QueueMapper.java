package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AttencionChannel;
import com.mycompany.myapp.domain.Branch;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.Queue;
import com.mycompany.myapp.service.dto.AttencionChannelDTO;
import com.mycompany.myapp.service.dto.BranchDTO;
import com.mycompany.myapp.service.dto.CompanyDTO;
import com.mycompany.myapp.service.dto.QueueDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Queue} and its DTO {@link QueueDTO}.
 */
@Mapper(componentModel = "spring")
public interface QueueMapper extends EntityMapper<QueueDTO, Queue> {
    @Mapping(target = "attencionChannel", source = "attencionChannel", qualifiedByName = "attencionChannelId")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    @Mapping(target = "branches", source = "branches", qualifiedByName = "branchIdSet")
    QueueDTO toDto(Queue s);

    @Mapping(target = "removeBranch", ignore = true)
    Queue toEntity(QueueDTO queueDTO);

    @Named("attencionChannelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AttencionChannelDTO toDtoAttencionChannelId(AttencionChannel attencionChannel);

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
