package com.example.courtstar.mapper;

import com.example.courtstar.dto.request.CentreManagerRequest;
import com.example.courtstar.dto.request.CentreRequest;
import com.example.courtstar.dto.response.CentreResponse;
import com.example.courtstar.entity.Centre;
import com.example.courtstar.entity.CentreManager;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CentreMapper {
    Centre toCentre(CentreRequest request);
    CentreResponse toCentreResponse(Centre centre);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCentre(@MappingTarget Centre centre,CentreRequest request);

}
