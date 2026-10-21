document.addEventListener("DOMContentLoaded", function() {
    var formRegistro = document.getElementById('form-registro');
    if (formRegistro) {
        formRegistro.addEventListener('submit', function(event) {
            event.preventDefault(); // Prevenir el envío del formulario para validar primero

            var nombre = document.getElementById('nombre') ? document.getElementById('nombre').value : '';
            var email = document.getElementById('email') ? document.getElementById('email').value : '';
            var password = document.getElementById('password') ? document.getElementById('password').value : '';
            var mensajeError = document.getElementById('mensaje-error');

            // Verificar si algún campo está vacío
            if (!nombre || !email || !password) {
                if (mensajeError) {
                    mensajeError.innerText = "Por favor complete todos los campos.";
                }
                return;
            }

            // Si todos los campos están llenos, el formulario se envía
            formRegistro.submit();
        });
    }
});