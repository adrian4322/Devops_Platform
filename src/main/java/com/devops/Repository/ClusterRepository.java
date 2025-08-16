package com.devops.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.devops.Entity.Cluster;
import java.util.List;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {
    
    Cluster findByName(String name);
    List<Cluster> findAll();
    List<Cluster> findByStatus(String status);
}
