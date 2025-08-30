package com.devops.Controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

import com.devops.Dto.ClusterResponseDto;
import com.devops.Service.ClusterService;
import com.devops.Service.NodeService;
import com.devops.Service.PodService;

import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = ClusterController.class)
public class ClusterControllerTest {
    

    @Autowired MockMvc mockMvc;
    
    @MockitoBean ClusterService clusterService;
    @MockitoBean NodeService nodeService; 
    @MockitoBean PodService podService; 


    @Test
    void getAll_returns200_andList() throws Exception {
        ClusterResponseDto dto = new ClusterResponseDto();
        dto.setId(1L); dto.setName("prod"); dto.setEndpoint("https://k8s"); dto.setStatus("ACTIVE");
        when(clusterService.getAllClustersResponseDto()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/clusters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("prod"));
    }

    @Test
    void getClusterById_returns200_andDto() throws Exception {

        ClusterResponseDto dto = new ClusterResponseDto();
        dto.setId(1L); 
        dto.setName("prod"); 
        dto.setEndpoint("https://k8s");
        dto.setStatus("ACTIVE");

        when(clusterService.getClusterDtoById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/clusters/Id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("prod"));
    }

    @Test
    void getClusterById_notFound_returns404() throws Exception {
        when(clusterService.getClusterDtoById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clusters/Id/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_invalid_returns() throws Exception {
        String body =  """
                {
                    "endpoint": "https://k8s",
                    "token": "SECRET",
                    "status": "ACTIVE"
                }
                """;

        mockMvc.perform(post("/api/clusters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());    
    }
}
