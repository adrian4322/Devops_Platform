package com.devops.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.devops.Entity.Cluster;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {
    
    Optional<Cluster> findByName(String name);
    List<Cluster> findByStatus(String status);
    
}
