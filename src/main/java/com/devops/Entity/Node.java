package com.devops.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "nodes")
public class Node {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "numele nu poate sa fie gol")
    private String name;

    @NotBlank()
    private String status;

    @ManyToOne
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL)
    private List<Pod> podsList;

    public Node() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public List<Pod> getPodsList() {
        return podsList;
    }

    public void setPodsList(List<Pod> podsList) {
        this.podsList = podsList;
    }

}
