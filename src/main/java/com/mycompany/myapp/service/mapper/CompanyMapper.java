package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {}
