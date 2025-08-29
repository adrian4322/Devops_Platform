package com.devops.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.devops.Service.NodeService;
import com.devops.Dto.NodeDto;

import java.util.List;

@RestController
@RequestMapping("api/nodes")
@CrossOrigin(origins = "*")
public class NodeController {

    private NodeService nodeService;
    
   public NodeController(NodeService nodeService){
         this.nodeService = nodeService;
   }

    @GetMapping
    public ResponseEntity<List<NodeDto>> getAllNodesDto(){
        List<NodeDto> nodes = nodeService.getAllNodesDto();
        return ResponseEntity.ok(nodes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NodeDto> getNodeById(@PathVariable("id") Long id){
        return nodeService.getNodeDtoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<NodeDto> getNodeByName(@PathVariable("name") String name){
        try {
            NodeDto dto = nodeService.getNodeDtoByName(name); 
            return ResponseEntity.ok(dto);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
