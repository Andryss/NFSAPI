package ru.andryss.nfsapi.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.andryss.nfsapi.error.ErrorCode;
import ru.andryss.nfsapi.model.Node;
import ru.andryss.nfsapi.model.NodeType;
import ru.andryss.nfsapi.repository.NodeRepository;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.charset.StandardCharsets.US_ASCII;

@SuppressWarnings("DuplicatedCode")
@Service
@RequiredArgsConstructor
public class NFSService {

    private final NodeRepository nodeRepository;

    private AtomicInteger maxInode;

    @PostConstruct
    private void init() {
        maxInode = new AtomicInteger(nodeRepository.findMaximumInode());
    }

    public byte[] lookup(int parent, String name) {
        ByteBuffer buffer = ByteBuffer.allocate(8 + 4 + 1);
        buffer.order(LITTLE_ENDIAN);

        Node parentNode = new Node();
        parentNode.setInode(parent);

        Optional<Node> nodeOptional = nodeRepository.findByParentAndName(parentNode, name);

        if (nodeOptional.isEmpty()) {
            buffer.putLong(ErrorCode.NOT_FOUND.getCode());
            return buffer.array();
        }

        Node node = nodeOptional.get();

        buffer.putLong(ErrorCode.OK.getCode());
        buffer.putInt(node.getInode());
        buffer.put(node.getType());

        return buffer.array();
    }

    public byte[] list(int inode) {
        ByteBuffer buffer = ByteBuffer.allocate(8 + 4 + 8 * (64 + 4 + 1));
        buffer.order(LITTLE_ENDIAN);

        if (!nodeRepository.existsById(inode)) {
            buffer.putLong(ErrorCode.NOT_FOUND.getCode());
            return buffer.array();
        }

        Node parent = new Node();
        parent.setInode(inode);

        List<Node> nodeList = nodeRepository.findAllByParent(parent);

        buffer.putLong(ErrorCode.OK.getCode());
        buffer.putInt(nodeList.size());

        for (Node node : nodeList) {
            byte[] name = Arrays.copyOf(node.getName().getBytes(US_ASCII), 64);
            name[63] = 0;
            buffer.put(name);
            buffer.putInt(node.getInode());
            buffer.put(node.getType());
        }

        return buffer.array();
    }

    public byte[] create(int parent, String name, byte type) {
        ByteBuffer buffer = ByteBuffer.allocate(8 + 4);
        buffer.order(LITTLE_ENDIAN);

        if (type != NodeType.DT_REG.getValue() && type != NodeType.DT_DIR.getValue()) {
            buffer.putLong(ErrorCode.UNKNOWN.getCode());
            return buffer.array();
        }

        if (!nodeRepository.existsById(parent)) {
            buffer.putLong(ErrorCode.NOT_FOUND.getCode());
            return buffer.array();
        }

        Node parentNode = new Node();
        parentNode.setInode(parent);

        if (nodeRepository.existsByParentAndName(parentNode, name)) {
            buffer.putLong(ErrorCode.CONFLICT.getCode());
            return buffer.array();
        }

        int newInode = maxInode.incrementAndGet();

        Node newNode = new Node();
        newNode.setInode(newInode);
        newNode.setName(name);
        newNode.setParent(parentNode);
        newNode.setType(type);

        nodeRepository.save(newNode);

        buffer.putLong(ErrorCode.OK.getCode());
        buffer.putInt(newInode);
        return buffer.array();
    }

    public byte[] remove(int parent, String name) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(LITTLE_ENDIAN);

        if (!nodeRepository.existsById(parent)) {
            buffer.putLong(ErrorCode.NOT_FOUND.getCode());
            return buffer.array();
        }

        Node parentNode = new Node();
        parentNode.setInode(parent);

        Optional<Node> nodeOptional = nodeRepository.findByParentAndName(parentNode, name);

        if (nodeOptional.isEmpty()) {
            buffer.putLong(ErrorCode.NOT_FOUND.getCode());
            return buffer.array();
        }

        Node node = nodeOptional.get();

        if (node.getType() == NodeType.DT_DIR.getValue() && nodeRepository.existsByParent(node)) {
            buffer.putLong(ErrorCode.CONFLICT.getCode());
            return buffer.array();
        }

        nodeRepository.delete(node);

        buffer.putLong(ErrorCode.OK.getCode());
        return buffer.array();
    }
}
