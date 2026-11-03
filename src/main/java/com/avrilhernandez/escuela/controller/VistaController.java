package com.avrilhernandez.escuela.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/alumnos")
    public String alumnos() {
        return "alumnos";
    }

    @GetMapping("/profesores")
    public String profesores() {
        return "profesores";
    }

    @GetMapping("/materias")
    public String materias() {
        return "materias";
    }
}
