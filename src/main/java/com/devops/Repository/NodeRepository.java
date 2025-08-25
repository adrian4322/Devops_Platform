package com.devops.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.devops.Entity.Node;
import com.devops.Entity.Pod;
import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    
    Optional<Node> findByName(String name);
    List<Node> findNodesByClusterName(String name);
    List<Node> findNodesByClusterId(Long Id);

}
