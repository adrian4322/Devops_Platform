package com.devops.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devops.Entity.Pod;
import com.devops.Entity.Node;

import java.util.List;

@Repository
public interface PodRepository extends JpaRepository<Pod, Long> {

    Pod findPodById(Long Id);
    Pod findPodByName(String name);
    List<Pod> findPodsByPhase(String Phase);
    List<Pod> findByNamespace(String namespace);
    List<Pod> findPodsByNodeName(Node node);
    
    
}
