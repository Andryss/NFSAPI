package ru.andryss.nfsapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Table(name = "nodes")
public class Node {
    @Id
    private int inode;

    private String name;

    private byte type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent")
    private Node parent;
}
