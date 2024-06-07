package com.example.courtstar.mapper;

import com.example.courtstar.dto.request.CentreRequest;
import com.example.courtstar.dto.response.CentreResponse;
import com.example.courtstar.entity.Centre;
import com.example.courtstar.entity.Image;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CentreMapper {
    Set<Image> map(List<String> value);
    Image map(String value);
    Centre toCentre(CentreRequest request);
    CentreResponse toCentreResponse(Centre centre);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCentre(@MappingTarget Centre centre,CentreRequest request);

}
