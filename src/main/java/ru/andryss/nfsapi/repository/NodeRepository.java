package ru.andryss.nfsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.andryss.nfsapi.model.Node;

import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {

    List<Node> findAllByParent(Node parent);

    Optional<Node> findByParentAndName(Node parent, String name);

    boolean existsByParentAndName(Node parent, String name);

    @Query("select max(n.inode) from Node n")
    int findMaximumInode();

    boolean existsByParent(Node parent);

}
