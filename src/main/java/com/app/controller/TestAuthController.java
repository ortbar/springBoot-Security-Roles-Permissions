package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/method")
//@PreAuthorize("denyAll()") // x defecto prohibe todos los endpoints atodos,
public class TestAuthController {

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('READ')")
    public String helloGet(){
        return "Hello World - GET";
    }

    @PostMapping("/post")
//    @PreAuthorize("hasAuthority('CREATE') or hasAuthority('READ')") comentamos las anotaciones para hacer la configuarcion desde el securityConfig, que es otra forma de ahcerlo
    public String helloPost(){
        return "Hello World - POST";
    }

    @PutMapping("/put")
    public String helloPut(){
        return "Hello World - PUT";
    }

    @DeleteMapping("/delete")
    public String helloDelete(){
        return "Hello World - DELETE";
    }

    @PatchMapping("/patch")
//    @PreAuthorize("hasAuthority('REFACTOR')") comentamos las anotaciones para hacer la configuarcion desde el securityConfig, que es otra forma de ahcerlo
    public String helloPatch(){
        return "Hello World - PATCH";
    }





}
