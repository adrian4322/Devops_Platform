package com.devops.Mapper;

import com.devops.Entity.Node;
import com.devops.Dto.NodeDto;
import com.devops.Dto.PodDto;

import java.util.stream.Collectors;
import java.util.List;

public class NodeMapper {
    
    public static NodeDto convertNodeToDto(Node node){
        NodeDto dto = new NodeDto();
        dto.setId(node.getId());
        dto.setName(node.getName());
        dto.setStatus(node.getStatus());
        if(node.getPodsList() != null) {
            List<PodDto> podDtos = node.getPodsList().stream()
                                    .map(PodMapper::convertToDto)
                                    .collect(Collectors.toList());
            dto.setPodsList(podDtos);
        }
        return dto;
    }

}
