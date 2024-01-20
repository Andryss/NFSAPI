package ru.andryss.nfsapi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NodeType {
    /**
     * Directory type
     */
    DT_DIR((byte) 4),
    /**
     * File type
     */
    DT_REG((byte) 8);

    private final byte value;
}
