package com.devops.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devops.Service.ClusterService;
import com.devops.Dto.ClusterDto;

import java.util.List;

@RestController
@RequestMapping("/api/clusters")
@CrossOrigin(origins = "*")
public class ClusterController {
    
    @Autowired
    private ClusterService clusterService;

    @GetMapping
    public ResponseEntity<List<ClusterDto>> getAllClusterDto(){
        try {
            List<ClusterDto> clusters = clusterService.getAllClustersDto();
            return ResponseEntity.ok(clusters);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{Id}")
    public ResponseEntity<ClusterDto> getClusterById(@PathVariable Long Id) {
            return clusterService.getClusterDtoById(Id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ClusterDto> getClusterByName(@PathVariable String name) {
        try {
            ClusterDto clusterDto = clusterService.getClusterDtoByName(name);
            return ResponseEntity.ok(clusterDto);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


}
