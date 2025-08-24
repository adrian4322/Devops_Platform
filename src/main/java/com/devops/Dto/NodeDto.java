package com.devops.Dto;

import com.devops.Entity.Pod;
import java.util.List;

public class NodeDto {

    private Long id;
    private String name;
    private String status;
    private List<Pod> podsList; 

    public Long getId() {return id;}
    public void setId(Long id) {this.id=id;}

    public String getName() {return name;}
    public void setName(String name) {this.name=name;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    
    public List<Pod> getPodsList() {return podsList;}
    public void setPodsList(List<Pod> podsList) {this.podsList = podsList;}

}