package com.avrilhernandez.escuela.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ALUMNO")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "EDAD", nullable = false)
    private Integer edad;

    @Column(name = "PROMEDIO", nullable = false,
            precision = 4, scale = 2)
    private BigDecimal promedio;

    public Alumno() {
    }

    public Alumno(String nombre, Integer edad,
                  BigDecimal promedio) {
        this.nombre = nombre;
        this.edad = edad;
        this.promedio = promedio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }
}
