<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Biblioteca Universitaria</title>
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
        <div class="error-container">
            <!-- Icono de error -->
            <div class="error-icon">
                ⚠️
            </div>

            <!-- Mensaje de error -->
            <h2 class="error-title">¡Ups! Algo salió mal</h2>
            
            <c:choose>
                <c:when test="${not empty error}">
                    <p class="error-message">${error}</p>
                </c:when>
                <c:otherwise>
                    <p class="error-message">Ha ocurrido un error inesperado al procesar su solicitud.</p>
                </c:otherwise>
            </c:choose>

            <!-- Información técnica (solo en desarrollo) -->
            <c:if test="${not empty pageContext.exception}">
                <div class="error-details">
                    <h3>Detalles Técnicos:</h3>
                    <p><strong>Tipo de Error:</strong> ${pageContext.exception.class.name}</p>
                    <p><strong>Mensaje:</strong> ${pageContext.exception.message}</p>
                </div>
            </c:if>

            <!-- Sugerencias -->
            <div class="error-suggestions">
                <h3>¿Qué puede hacer?</h3>
                <ul>
                    <li>Volver a intentar la operación</li>
                    <li>Verificar que todos los datos ingresados sean correctos</li>
                    <li>Regresar a la página principal</li>
                    <li>Contactar al administrador si el problema persiste</li>
                </ul>
            </div>

            <!-- Acciones -->
            <div class="error-actions">
                <button onclick="history.back()" class="btn btn-primary btn-large">
                    ← Volver Atrás
                </button>
                <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-secondary btn-large">
                    🏠 Ir al Inicio
                </a>
            </div>

            <!-- Información de contacto -->
            <div class="contact-info">
                <p><strong>¿Necesita ayuda?</strong></p>
                <p>Contacte al soporte técnico:</p>
                <p>📧 Email: [email protected]</p>
                <p>📞 Teléfono: (123) 456-7890</p>
            </div>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2025 Biblioteca Universitaria. Sistema de Gestión de Préstamos.</p>
            <p>Desarrollado con Java EE - Patrón MVC</p>
        </div>
    </footer>
</body>
</html>
