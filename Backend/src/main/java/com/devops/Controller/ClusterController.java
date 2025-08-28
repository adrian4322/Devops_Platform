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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/Id/{Id}")
    public ResponseEntity<ClusterDto> getClusterById(@PathVariable("Id") Long Id) {
            return clusterService.getClusterDtoById(Id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ClusterDto> getClusterByName(@PathVariable("name") String name) {
        ClusterDto clusterDto = clusterService.getClusterDtoByName(name);
        if (clusterDto != null) {
            return ResponseEntity.ok(clusterDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCluster(@RequestBody ClusterDto clusterDto){
        try {
            clusterService.createCluster(clusterDto);
            return ResponseEntity.ok("Clusterul a fost creat cu succes");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
