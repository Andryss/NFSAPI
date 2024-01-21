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

    @Column(length = 256)
    private String name;

    private byte type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent")
    private Node parent;

    @Column(length = 1024)
    private String content;
}
