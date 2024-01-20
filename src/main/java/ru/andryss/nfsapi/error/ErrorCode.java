package ru.andryss.nfsapi.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.andryss.nfsapi.model.NodeType;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * Method completed successfully
     */
    OK(0),
    /**
     * Something was not found during method invocation.
     * Usually inode or file/directory name
     */
    NOT_FOUND(1),
    /**
     * Method cannot complete because of some conflict.
     * Usually creation or deletion conflicts (e.g. passed directory must be empty)
     */
    CONFLICT(2),
    /**
     * Unknown type of object to create (see {@link NodeType})
     */
    UNKNOWN(3);

    private final long code;
}
