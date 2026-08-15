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

setTimeout(() => {
    document.querySelectorAll('.toast').forEach(t => t.classList.remove('show'));
}, 4000);