function mostrarImagen(input) {
    if (input.files && input.files[0]) {
        const imagen = input.files[0];
        const maximo = 512 * 1024;

        if (imagen.size <= maximo) {
            var lector = new FileReader();

            lector.onload = function (e) {
                $('#blah').attr('src', e.target.result).height(200);
            };

            lector.readAsDataURL(imagen);
        } else {
            alert("La imagen seleccionada es muy grande... no debe superar los 512 Kb!");
            input.value = "";
        }
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const confirmModal = document.getElementById('confirmModal');

    if (confirmModal) {
        confirmModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;

            const modalId = document.getElementById('modalId');
            const modalDescripcion = document.getElementById('modalDescripcion');

            if (modalId) {
                modalId.value = button.getAttribute('data-bs-id');
            }

            if (modalDescripcion) {
                modalDescripcion.textContent =
                        button.getAttribute('data-bs-descripcion');
            }
        });
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const aprobarModal = document.getElementById('aprobarModal');
    if (aprobarModal) {
        aprobarModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            document.getElementById('aprobarModalId').value = button.getAttribute('data-bs-id');
            document.getElementById('aprobarModalDescripcion').textContent = button.getAttribute('data-bs-descripcion');
        });
    }

    const rechazarModal = document.getElementById('rechazarModal');
    if (rechazarModal) {
        rechazarModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            document.getElementById('rechazarModalId').value = button.getAttribute('data-bs-id');
            document.getElementById('rechazarModalDescripcion').textContent = button.getAttribute('data-bs-descripcion');
        });
    }

    const cancelarSolicitudModal = document.getElementById('cancelarSolicitudModal');
    if (cancelarSolicitudModal) {
        cancelarSolicitudModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            document.getElementById('cancelarSolicitudModalId').value = button.getAttribute('data-bs-id');
            document.getElementById('cancelarSolicitudModalDescripcion').textContent = button.getAttribute('data-bs-descripcion');
        });
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const actualizarEstadoModal = document.getElementById('actualizarEstadoModal');
    if (actualizarEstadoModal) {
        actualizarEstadoModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            document.getElementById('actualizarEstadoModalId').value = button.getAttribute('data-bs-id');
            document.getElementById('actualizarEstadoModalDescripcion').textContent = button.getAttribute('data-bs-descripcion');
        });
    }
});
setTimeout(() => {
    document.querySelectorAll('.toast').forEach(t => t.classList.remove('show'));
}, 4000);