const modal = new bootstrap.Modal(
    document.getElementById("modalAsignacion")
);

const tabla = document.getElementById("tablaDatos");
const encabezados = document.getElementById("encabezados");
const buscador = document.getElementById("buscador");
const mensaje = document.getElementById("mensaje");
const formulario = document.getElementById("formulario");

let tipo = "alumnos";
let registros = [];
let personas = [];
let materias = [];
let idEdicion = null;

const rutaBase = "/api/asignaciones/";

function avisar(texto, error = false) {
    mensaje.textContent = texto;
    mensaje.className = error
        ? "alert alert-danger"
        : "alert alert-success";
}

async function consultar(url) {
    const respuesta = await fetch(url);

    if (!respuesta.ok) {
        throw new Error("Error HTTP " + respuesta.status);
    }

    return respuesta.json();
}

async function cargar() {
    try {
        registros = await consultar(rutaBase + tipo);
        dibujar();
    } catch (error) {
        avisar("No se pudieron cargar las asignaciones: " +
            error.message, true);
    }
}

function celda(fila, valor) {
    const td = document.createElement("td");
    td.textContent = valor ?? "Sin registrar";
    fila.appendChild(td);
}

function dibujar() {
    tabla.replaceChildren();
    encabezados.replaceChildren();

    const tr = document.createElement("tr");

    const titulos = tipo === "alumnos"
        ? ["Alumno", "Materia", "Calificación", "Acciones"]
        : ["Profesor", "Materia", "Acciones"];

    titulos.forEach(titulo => {
        const th = document.createElement("th");
        th.textContent = titulo;
        tr.appendChild(th);
    });

    encabezados.appendChild(tr);

    const filtro = buscador.value.toLowerCase().trim();

    const filtrados = registros.filter(r =>
        `${r.alumno ?? r.profesor ?? ""} ${r.materia}`
            .toLowerCase().includes(filtro)
    );

    if (!filtrados.length) {
        const fila = document.createElement("tr");
        const td = document.createElement("td");
        td.colSpan = titulos.length;
        td.className = "text-center py-4";
        td.textContent = "No se encontraron asignaciones.";
        fila.appendChild(td);
        tabla.appendChild(fila);
        return;
    }

    filtrados.forEach(registro => {
        const fila = document.createElement("tr");

        celda(fila, tipo === "alumnos"
            ? registro.alumno : registro.profesor);

        celda(fila, registro.materia);

        if (tipo === "alumnos") {
            celda(fila, registro.calificacion);
        }

        const acciones = document.createElement("td");

        if (tipo === "alumnos") {
            const editar = document.createElement("button");
            editar.className = "btn-editar me-2";
            editar.textContent = "Calificar";
            editar.onclick = () => abrirModal(registro);
            acciones.appendChild(editar);
        }

        const eliminar = document.createElement("button");
        eliminar.className = "btn-eliminar";
        eliminar.textContent = "Quitar";
        eliminar.onclick = () => quitar(registro.id);
        acciones.appendChild(eliminar);

        fila.appendChild(acciones);
        tabla.appendChild(fila);
    });
}

function llenarSelect(select, datos) {
    select.replaceChildren();

    const inicial = document.createElement("option");
    inicial.value = "";
    inicial.textContent = "Selecciona una opción";
    select.appendChild(inicial);

    datos.forEach(item => {
        const opcion = document.createElement("option");
        opcion.value = item.id;
        opcion.textContent = item.nombre;
        select.appendChild(opcion);
    });
}

async function abrirModal(registro = null) {
    try {
        formulario.reset();
        idEdicion = registro ? registro.id : null;

        const editando = idEdicion !== null;

        document.getElementById("tituloModal").textContent =
            editando ? "Actualizar calificación"
                : tipo === "alumnos"
                    ? "Inscribir alumno"
                    : "Asignar materia a profesor";

        document.getElementById("etiquetaPersona").textContent =
            tipo === "alumnos" ? "Alumno" : "Profesor";

        document.getElementById("grupoCalificacion")
            .classList.toggle("d-none", tipo !== "alumnos");

        const selectPersona = document.getElementById("persona");
        const selectMateria = document.getElementById("materia");

        selectPersona.disabled = editando;
        selectMateria.disabled = editando;

        if (editando) {
            personas = await consultar("/api/alumnos");
            materias = await consultar("/api/materias");

            llenarSelect(selectPersona, personas);
            llenarSelect(selectMateria, materias);

            selectPersona.value = registro.alumnoId;
            selectMateria.value = registro.materiaId;

            document.getElementById("calificacion").value =
                registro.calificacion ?? "";
        } else {
            const rutaPersonas = tipo === "alumnos"
                ? "/api/alumnos"
                : "/api/profesores";

            [personas, materias] = await Promise.all([
                consultar(rutaPersonas),
                consultar("/api/materias")
            ]);

            llenarSelect(selectPersona, personas);
            llenarSelect(selectMateria, materias);
        }

        modal.show();
    } catch (error) {
        avisar("No se pudo abrir el formulario: " +
            error.message, true);
    }
}

async function guardar(evento) {
    evento.preventDefault();

    if (!formulario.reportValidity()) return;

    const personaId = Number(
        document.getElementById("persona").value
    );

    const materiaId = Number(
        document.getElementById("materia").value
    );

    const valor = document.getElementById("calificacion").value;

    const calificacion = valor === ""
        ? null : Number(valor);

    let datos;
    let url = rutaBase + tipo;
    let metodo = "POST";

    if (idEdicion !== null) {
        url += "/" + idEdicion;
        metodo = "PUT";
        datos = { calificacion };
    } else if (tipo === "alumnos") {
        datos = { alumnoId: personaId, materiaId, calificacion };
    } else {
        datos = { profesorId: personaId, materiaId };
    }

    try {
        const respuesta = await fetch(url, {
            method: metodo,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });

        if (!respuesta.ok) {
            throw new Error("Error HTTP " + respuesta.status);
        }

        modal.hide();
        avisar("Los cambios se guardaron correctamente.");
        await cargar();
    } catch (error) {
        avisar("No se pudo guardar. Revisa que la asignación " +
            "no exista previamente. " + error.message, true);
    }
}

async function quitar(id) {
    if (!confirm("¿Deseas quitar esta asignación?")) return;

    try {
        const respuesta = await fetch(
            rutaBase + tipo + "/" + id,
            { method: "DELETE" }
        );

        if (!respuesta.ok) {
            throw new Error("Error HTTP " + respuesta.status);
        }

        avisar("Asignación eliminada correctamente.");
        await cargar();
    } catch (error) {
        avisar("No se pudo quitar: " + error.message, true);
    }
}

document.querySelectorAll("[data-tipo]").forEach(boton => {
    boton.addEventListener("click", () => {
        tipo = boton.dataset.tipo;
        buscador.value = "";

        document.querySelectorAll("[data-tipo]")
            .forEach(b => b.classList.remove("active"));

        boton.classList.add("active");

        document.getElementById("tituloTabla").textContent =
            tipo === "alumnos"
                ? "Inscripciones"
                : "Asignaciones docentes";

        document.getElementById("descripcionTabla").textContent =
            tipo === "alumnos"
                ? "Materias que cursan los alumnos."
                : "Materias que imparten los profesores.";

        document.getElementById("btnNuevo").textContent =
            tipo === "alumnos"
                ? "+ Inscribir alumno"
                : "+ Asignar materia";

        mensaje.className = "alert d-none";
        cargar();
    });
});

document.getElementById("btnNuevo")
    .addEventListener("click", () => abrirModal());

formulario.addEventListener("submit", guardar);
buscador.addEventListener("input", dibujar);

cargar();
