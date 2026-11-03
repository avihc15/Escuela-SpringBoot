
package com.avrilhernandez.escuela.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PROFESOR_MATERIA")
public class ProfesorMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFESOR_ID", nullable = false)
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATERIA_ID", nullable = false)
    private Materia materia;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }
}
