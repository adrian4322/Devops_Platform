package com.devops.Mapper;

import com.devops.Dto.PodDto;
import com.devops.Entity.Pod;

public class PodMapper {
    
    public static PodDto convertToDto(Pod pod){
        PodDto dto = new PodDto();
        dto.setId(pod.getId());
        dto.setName(pod.getName());
        dto.setNamespace(pod.getNamespace());
        dto.setPhase(pod.getPhase());
        dto.setPodIP(pod.getPodIP());
        dto.setHostIP(pod.getHostIP());
        dto.setRestartCount(pod.getRestartCount());
        dto.setNodeName(pod.getNodeName());
        return dto;
    }

}
