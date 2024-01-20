package ru.andryss.nfsapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.andryss.nfsapi.service.NFSService;
import ru.andryss.nfsapi.service.TokenService;

import static org.springframework.http.HttpStatus.*;

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
}
