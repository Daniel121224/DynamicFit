package com.software.Dynamicfit.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @GetMapping
    private String nombre(){
        return "Pepe";
    }
}
