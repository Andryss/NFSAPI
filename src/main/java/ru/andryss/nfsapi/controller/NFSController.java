package ru.andryss.nfsapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.andryss.nfsapi.service.NFSService;
import ru.andryss.nfsapi.service.TokenService;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NFSController {

    private final TokenService tokenService;
    private final NFSService nfsService;

    @GetMapping("/api/list")
    private ResponseEntity<byte[]> list(
            @RequestParam("token") String token,
            @RequestParam("inode") String inode
    ) {
        log.info("GET /api/list token=\"{}\", inode=\"{}\"", token, inode);
        if (!tokenService.isValid(token)) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
        int inodeVal;
        try {
            inodeVal = Integer.parseInt(inode);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(nfsService.list(inodeVal));
    }

    @GetMapping("/api/lookup")
    private ResponseEntity<byte[]> lookup(
            @RequestParam("token") String token,
            @RequestParam("parent") String parent,
            @RequestParam("name") String name
    ) {
        log.info("GET /api/lookup token=\"{}\", parent=\"{}\", name=\"{}\"", token, parent, escapeJava(name));
        if (!tokenService.isValid(token)) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
        int parentVal;
        try {
            parentVal = Integer.parseInt(parent);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        return ResponseEntity.status(OK).body(nfsService.lookup(parentVal, name));
    }

    @GetMapping("/api/create")
    private ResponseEntity<byte[]> create(
            @RequestParam("token") String token,
            @RequestParam("parent") String parent,
            @RequestParam("name") String name,
            @RequestParam("type") String type
    ) {
        log.info("GET /api/create token=\"{}\", parent=\"{}\", name=\"{}\", type=\"{}\"", token, parent, escapeJava(name), type);
        if (!tokenService.isValid(token)) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
        int parentVal;
        byte typeVal;
        try {
            parentVal = Integer.parseInt(parent);
            typeVal = Byte.parseByte(type);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        return ResponseEntity.status(OK).body(nfsService.create(parentVal, name, typeVal));
    }

    @GetMapping("/api/remove")
    private ResponseEntity<byte[]> remove(
            @RequestParam("token") String token,
            @RequestParam("parent") String parent,
            @RequestParam("name") String name
    ) {
        log.info("GET /api/remove token=\"{}\", parent=\"{}\", name=\"{}\"", token, parent, escapeJava(name));
        if (!tokenService.isValid(token)) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
        int parentVal;
        try {
            parentVal = Integer.parseInt(parent);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        return ResponseEntity.status(OK).body(nfsService.remove(parentVal, name));
    }

    @GetMapping("/api/read")
    private ResponseEntity<byte[]> read(
            @RequestParam("token") String token,
            @RequestParam("inode") String inode
    ) {
        log.info("GET /api/read token=\"{}\", inode=\"{}\"", token, inode);
        if (!tokenService.isValid(token)) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
        int inodeVal;
        try {
            inodeVal = Integer.parseInt(inode);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        return ResponseEntity.status(OK).body(nfsService.read(inodeVal));
    }

    @GetMapping("/api/write")
    private ResponseEntity<byte[]> write(
            @RequestParam("token") String token,
            @RequestParam("inode") String inode,
            @RequestParam("content") String content
    ) {
        log.info("GET /api/write token=\"{}\", inode=\"{}\", content=\"{}\"", token, inode, escapeJava(content));
        if (!tokenService.isValid(token)) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
        int inodeVal;
        try {
            inodeVal = Integer.parseInt(inode);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        return ResponseEntity.status(OK).body(nfsService.write(inodeVal, content));
    }
}
