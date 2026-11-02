const api = document.body.dataset.api;
const tabla = document.getElementById("tablaDatos");
const formulario = document.getElementById("formulario");
const buscador = document.getElementById("buscador");
const mensaje = document.getElementById("mensaje");
const modal = new bootstrap.Modal(
    document.getElementById("modalRegistro")
);

const nombres = {
    "/api/alumnos": "alumno",
    "/api/profesores": "profesor",
    "/api/materias": "materia"
};

const nombreModulo = nombres[api];

const campos = [...formulario.querySelectorAll("[name]")];
let registros = [];
let idActual = null;
let nombreId = "id";

function notificar(texto, error = false) {
    mensaje.textContent = texto;
    mensaje.className = error
        ? "alert alert-danger"
        : "alert alert-success";
}

function obtenerId(registro) {
    return registro[nombreId];
}

function detectarId(registro) {
    const opciones = [
        "id",
        "idAlumno",
        "idProfesor",
        "idMateria",
        "id_alumno",
        "id_profesor",
        "id_materia"
    ];

    return opciones.find(c => c in registro) || "id";
}

function crearCelda(fila, valor) {
    const celda = document.createElement("td");
    celda.textContent = valor ?? "";
    fila.appendChild(celda);
}

function dibujarTabla() {
    tabla.replaceChildren();

    const texto = buscador.value.toLowerCase().trim();

    const filtrados = registros.filter(registro =>
        String(registro.nombre ?? "")
            .toLowerCase()
            .includes(texto)
    );

    if (filtrados.length === 0) {
        const fila = document.createElement("tr");
        const celda = document.createElement("td");
        celda.colSpan = campos.length + 2;
        celda.className = "text-center py-4";
        celda.textContent = "No se encontraron registros.";
        fila.appendChild(celda);
        tabla.appendChild(fila);
        return;
    }

    filtrados.forEach(registro => {
        const fila = document.createElement("tr");

        crearCelda(fila, obtenerId(registro));

        campos.forEach(campo => {
            crearCelda(fila, registro[campo.name]);
        });

        const acciones = document.createElement("td");

        const editar = document.createElement("button");
        editar.type = "button";
        editar.textContent = "Editar";
        editar.className = "btn-editar me-2";
        editar.onclick = () => abrirFormulario(registro);

        const eliminar = document.createElement("button");
        eliminar.type = "button";
        eliminar.textContent = "Eliminar";
        eliminar.className = "btn-eliminar";
        eliminar.onclick = () => eliminarRegistro(registro);

        acciones.append(editar, eliminar);
        fila.appendChild(acciones);
        tabla.appendChild(fila);
    });
}

async function cargarDatos() {
    try {
        const respuesta = await fetch(api);

        if (!respuesta.ok) {
            throw new Error("No se pudo consultar la información.");
        }

        const datos = await respuesta.json();

        if (!Array.isArray(datos)) {
            throw new Error("La API no devolvió una lista.");
        }

        registros = datos;

        if (registros.length > 0) {
            nombreId = detectarId(registros[0]);
        }

        dibujarTabla();
    } catch (error) {
        notificar(error.message, true);
    }
}

function abrirFormulario(registro = null) {
    formulario.reset();
    idActual = registro ? obtenerId(registro) : null;

    document.getElementById("tituloModal").textContent =
        registro ? "Editar " + nombreModulo : "Nuevo registro";

    if (registro) {
        campos.forEach(campo => {
            campo.value = registro[campo.name] ?? "";
        });
    }

    modal.show();
}

async function guardarRegistro(evento) {
    evento.preventDefault();

    if (!formulario.reportValidity()) return;

    const datos = {};

    campos.forEach(campo => {
        datos[campo.name] = campo.type === "number"
            ? Number(campo.value)
            : campo.value.trim();
    });

    const esNuevo = idActual === null;

    const direccion = esNuevo
        ? api
        : `${api}/${encodeURIComponent(idActual)}`;

    try {
        const respuesta = await fetch(direccion, {
            method: esNuevo ? "POST" : "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(datos)
        });

        if (!respuesta.ok) {
            throw new Error(
                `No se pudo guardar. Código HTTP ${respuesta.status}.`
            );
        }

        modal.hide();
        notificar("Registro guardado correctamente.");
        await cargarDatos();

    } catch (error) {
        notificar(error.message, true);
    }
}

async function eliminarRegistro(registro) {
    const id = obtenerId(registro);

    if (id === undefined || id === null) {
        notificar("No se encontró el identificador.", true);
        return;
    }

    if (!confirm("¿Deseas eliminar este registro?")) return;

    try {
        const respuesta = await fetch(
            `${api}/${encodeURIComponent(id)}`,
            { method: "DELETE" }
        );

        if (!respuesta.ok) {
            throw new Error(
                `No se pudo eliminar. Código HTTP ${respuesta.status}.`
            );
        }

        notificar("Registro eliminado correctamente.");
        await cargarDatos();

    } catch (error) {
        notificar(
            "No se pudo eliminar el registro. " +
            "Comprueba si tiene relaciones con otras tablas. " +
            error.message,
            true
        );
    }
}

document.getElementById("btnNuevo")
    .addEventListener("click", () => abrirFormulario());

buscador.addEventListener("input", dibujarTabla);

formulario.addEventListener("submit", guardarRegistro);

cargarDatos();
