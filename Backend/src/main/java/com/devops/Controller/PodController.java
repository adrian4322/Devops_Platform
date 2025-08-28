package com.devops.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.devops.Service.PodService;
import com.devops.Entity.Pod;
import com.devops.Dto.PodDto;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/pod")
@CrossOrigin(origins = "*")
public class PodController {

    @Autowired
    private PodService podService;

    @GetMapping
    public ResponseEntity<List<PodDto>> getAllPodsDto(){
        try{
            List<PodDto> pods = new ArrayList<>();
            for(Pod pod : podService.getAllPods()){
                pods.add(podService.convertToDto(pod));
            }
            return ResponseEntity.ok(pods);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/namespace/{namespace}")
    public ResponseEntity<List<PodDto>> getAllPodsInNamespace(@PathVariable("namespace") String namespace){
        try {
            List<Pod> pods = podService.getAllPods();
            List<PodDto> podsDto = new ArrayList<>();

            for(Pod pod : pods){
                if(pod.getNamespace().equals(namespace)){
                    podsDto.add(podService.convertToDto(pod));
                }
            }
            return ResponseEntity.ok(podsDto);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
