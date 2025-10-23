<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmación - Biblioteca Universitaria</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>📚 Biblioteca Universitaria</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/catalogo">Catálogo</a>
                <a href="${pageContext.request.contextPath}/admin">Administración</a>
            </nav>
        </div>
    </header>

    <main class="container">
        <div class="confirmation-container">
            <!-- Icono de éxito -->
            <div class="success-icon">
                ✅
            </div>

            <!-- Mensaje de confirmación -->
            <h2 class="confirmation-title">¡Solicitud Enviada con Éxito!</h2>
            
            <c:if test="${not empty mensaje}">
                <p class="confirmation-message">${mensaje}</p>
            </c:if>

            <!-- Detalles de la solicitud -->
            <div class="confirmation-details">
                <h3>📋 Detalles de su Solicitud</h3>
                
                <div class="detail-row">
                    <span class="detail-label">Número de Solicitud:</span>
                    <span class="detail-value">#${solicitudId}</span>
                </div>

                <div class="detail-row">
                    <span class="detail-label">Nombre:</span>
                    <span class="detail-value">${nombreUsuario}</span>
                </div>

                <div class="detail-row">
                    <span class="detail-label">Correo Electrónico:</span>
                    <span class="detail-value">${correoUsuario}</span>
                </div>

                <c:if test="${not empty libro}">
                    <div class="detail-row">
                        <span class="detail-label">Libro Solicitado:</span>
                        <span class="detail-value">${libro.titulo}</span>
                    </div>

                    <div class="detail-row">
                        <span class="detail-label">Autor:</span>
                        <span class="detail-value">${libro.autor}</span>
                    </div>

                    <div class="detail-row">
                        <span class="detail-label">Editorial:</span>
                        <span class="detail-value">${libro.editorial}</span>
                    </div>
                </c:if>
            </div>

            <!-- Próximos pasos -->
            <div class="next-steps">
                <h3>📌 Próximos Pasos</h3>
                <ol class="steps-list">
                    <li>
                        <strong>Confirmación por Email:</strong>
                        <p>Recibirá un correo electrónico de confirmación a <strong>${correoUsuario}</strong></p>
                    </li>
                    <li>
                        <strong>Revisión:</strong>
                        <p>Nuestro personal revisará su solicitud en un plazo de 24 horas</p>
                    </li>
                    <li>
                        <strong>Notificación:</strong>
                        <p>Le notificaremos cuando el libro esté listo para retiro</p>
                    </li>
                    <li>
                        <strong>Retiro:</strong>
                        <p>Puede recoger el libro en el mostrador de préstamos presentando su identificación</p>
                    </li>
                </ol>
            </div>

            <!-- Información adicional -->
            <div class="info-box">
                <h4>ℹ️ Información Importante:</h4>
                <ul>
                    <li>Plazo de préstamo: <strong>15 días</strong></li>
                    <li>El libro debe recogerse dentro de <strong>3 días</strong> después de la aprobación</li>
                    <li>Puede renovar el préstamo antes del vencimiento</li>
                    <li>Consulte el estado de su solicitud con el número: <strong>#${solicitudId}</strong></li>
                </ul>
            </div>

            <!-- Acciones -->
            <div class="confirmation-actions">
                <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary btn-large">
                    📚 Volver al Catálogo
                </a>
                <a href="${pageContext.request.contextPath}/solicitud" class="btn btn-secondary btn-large">
                    ➕ Nueva Solicitud
                </a>
            </div>

            <!-- Información de contacto -->
            <div class="contact-info">
                <p><strong>¿Tiene preguntas?</strong></p>
                <p>Puede contactarnos en:</p>
                <p>📧 Email: [email protected]</p>
                <p>📞 Teléfono: (123) 456-7890</p>
                <p>🕐 Horario: Lunes a Viernes, 8:00 AM - 6:00 PM</p>
            </div>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2025 Biblioteca Universitaria. Sistema de Gestión de Préstamos.</p>
            <p>Desarrollado con Java EE - Patrón MVC</p>
        </div>
    </footer>

    <script>
        // Opcional: Auto-redirección después de 30 segundos
        setTimeout(function() {
            const redirect = confirm('¿Desea volver al catálogo?');
            if (redirect) {
                window.location.href = '${pageContext.request.contextPath}/catalogo';
            }
        }, 30000);
    </script>
</body>
</html>
