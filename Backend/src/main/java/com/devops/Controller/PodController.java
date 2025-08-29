package com.devops.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.devops.Service.PodService;
import com.devops.Entity.Pod;
import com.devops.Dto.PodDto;

import java.util.List;

@RestController
@RequestMapping("/api/pod")
@CrossOrigin(origins = "*")
public class PodController {

    private final PodService podService;

    public PodController(PodService podService){
        this.podService=podService;
    }

        @GetMapping
        public ResponseEntity<List<PodDto>> getAllPodsDto(){
            List<PodDto> pods = podService.getAllPodsDto();
            return ResponseEntity.ok(pods);
        }

    @GetMapping("/namespace/{namespace}")
    public ResponseEntity<List<PodDto>> getAllPodsInNamespace(@PathVariable("namespace") String namespace){
        List<PodDto> pods = podService.getAllPodsInNamespace(namespace);
        return ResponseEntity.ok(pods);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Pod> getPodByName(@PathVariable String name){
        Pod pod = podService.getPodByName(name);
        if(pod  != null) {
            return ResponseEntity.ok(pod);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }    
    }


    
}
