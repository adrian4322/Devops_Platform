package com.devops.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.devops.Service.NodeService;
import com.devops.Entity.Node;
import com.devops.Dto.NodeDto;

import java.util.List;

@RestController
@RequestMapping("api/nodes")
@CrossOrigin(origins = "*")
public class NodeController {
    
    @Autowired
    private NodeService nodeService;

    @GetMapping
    public ResponseEntity<List<NodeDto>> getAllNodesDto(){
        try {
            List<NodeDto> nodes = nodeService.getAllNodesDto();
            return ResponseEntity.ok(nodes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NodeDto> getNodeById(@PathVariable Long id){
        try {
            Node node = nodeService.getNodeById(id);      
            NodeDto dto = nodeService.convertToDto(node); 
            return ResponseEntity.ok(dto);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<NodeDto> getNodeByName(@PathVariable String name){
        try {
            Node node = nodeService.getNodeByName(name);      
            NodeDto dto = nodeService.convertToDto(node); 
            return ResponseEntity.ok(dto);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
