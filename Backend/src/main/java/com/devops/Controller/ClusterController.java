package com.devops.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;

import com.devops.Service.ClusterService;
import com.devops.Dto.ClusterCreateRequest;
import com.devops.Dto.ClusterResponseDto;

import java.util.List;
import java.net.URI;

@RestController
@RequestMapping("/api/clusters")
@CrossOrigin(origins = "*")
public class ClusterController {
    
    private final ClusterService clusterService;

    public ClusterController(ClusterService clusterService){
            this.clusterService=clusterService;
    }

    @GetMapping
    public ResponseEntity<List<ClusterResponseDto>> getAllClusterDto(){
        List<ClusterResponseDto> clusters = clusterService.getAllClustersResponseDto();
        return ResponseEntity.ok(clusters);
    }

    @GetMapping("/Id/{Id}")
    public ResponseEntity<ClusterResponseDto> getClusterById(@PathVariable("Id") Long Id) {
        return clusterService.getClusterDtoById(Id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ClusterResponseDto> getClusterByName(@PathVariable("name") String name) {
        return clusterService.getClusterDtoByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

 
    @PostMapping
    public ResponseEntity<ClusterResponseDto> createCluster(@Valid @RequestBody ClusterCreateRequest request){
        ClusterResponseDto body = clusterService.createCluster(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/Id/{id}")
                        .buildAndExpand(body.getId())
                        .toUri();
        return ResponseEntity.created(location).body(body);
    }

}
