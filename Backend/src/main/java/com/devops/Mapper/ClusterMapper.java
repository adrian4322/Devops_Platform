package com.devops.Mapper;

import com.devops.Dto.ClusterCreateRequest;
import com.devops.Dto.ClusterResponseDto;
import com.devops.Entity.Cluster;

public class ClusterMapper {
    
    public static Cluster toEntity(ClusterCreateRequest request){
        if(request == null)
             return null;

        Cluster cluster = new Cluster();
        cluster.setName(request.getName());
        cluster.setEndpoint(request.getName());
        cluster.setToken(request.getToken());
        cluster.setStatus(request.getStatus());
        return cluster;
    }

    public static ClusterResponseDto convertToResponseDto(Cluster cluster){
        if(cluster == null) 
            return null;
        
        ClusterResponseDto dto = new ClusterResponseDto();
        dto.setId(cluster.getId());
        dto.setName(cluster.getName());
        dto.setEndpoint(cluster.getEndpoint());
        dto.setStatus(cluster.getStatus());
        return dto;
    }   


}
