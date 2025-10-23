<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitud de Préstamo - Biblioteca Universitaria</title>
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
        <div class="page-header">
            <h2>Solicitud de Préstamo</h2>
            <p>Complete el formulario para solicitar un libro</p>
        </div>

        <!-- Mensajes de error -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ⚠️ ${error}
            </div>
        </c:if>

        <!-- Información del libro seleccionado -->
        <c:if test="${not empty libro}">
            <div class="libro-info-card">
                <h3>📖 Libro Seleccionado</h3>
                <div class="libro-details">
                    <p><strong>Título:</strong> ${libro.titulo}</p>
                    <p><strong>Autor:</strong> ${libro.autor}</p>
                    <p><strong>Editorial:</strong> ${libro.editorial}</p>
                    <p><strong>Año:</strong> ${libro.anioPublicacion}</p>
                    <p><strong>Categoría:</strong> ${libro.categoria}</p>
                </div>
            </div>
        </c:if>

        <!-- Formulario de solicitud -->
        <div class="form-container">
            <form action="${pageContext.request.contextPath}/solicitud" method="post" class="solicitud-form">
                
                <!-- Campo oculto con ID del libro -->
                <c:choose>
                    <c:when test="${not empty libro}">
                        <input type="hidden" name="libroId" value="${libro.id}">
                    </c:when>
                    <c:otherwise>
                        <div class="form-group">
                            <label for="libroId">ID del Libro: *</label>
                            <input type="number" 
                                   id="libroId" 
                                   name="libroId" 
                                   required 
                                   min="1"
                                   placeholder="Ingrese el ID del libro"
                                   class="form-control">
                            <small class="form-text">Puede encontrar el ID en el catálogo de libros</small>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Nombre del usuario -->
                <div class="form-group">
                    <label for="nombre">Nombre Completo: *</label>
                    <input type="text" 
                           id="nombre" 
                           name="nombre" 
                           required 
                           maxlength="255"
                           placeholder="Ej: Juan Pérez García"
                           value="${nombre}"
                           class="form-control">
                </div>

                <!-- Correo electrónico -->
                <div class="form-group">
                    <label for="correo">Correo Electrónico: *</label>
                    <input type="email" 
                           id="correo" 
                           name="correo" 
                           required 
                           maxlength="255"
                           placeholder="Ej: [email protected]"
                           value="${correo}"
                           class="form-control">
                    <small class="form-text">Se enviará la confirmación a este correo</small>
                </div>

                <!-- Información adicional -->
                <div class="info-box">
                    <h4>📋 Información Importante:</h4>
                    <ul>
                        <li>El plazo de préstamo es de 15 días</li>
                        <li>Recibirá una confirmación por correo electrónico</li>
                        <li>El libro debe recogerse en un plazo de 3 días</li>
                        <li>Las renovaciones pueden solicitarse antes del vencimiento</li>
                    </ul>
                </div>

                <!-- Botones de acción -->
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary btn-large">
                        📤 Enviar Solicitud
                    </button>
                    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-secondary btn-large">
                        ← Volver al Catálogo
                    </a>
                </div>

                <p class="form-note">* Campos obligatorios</p>
            </form>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2025 Biblioteca Universitaria. Sistema de Gestión de Préstamos.</p>
            <p>Desarrollado con Java EE - Patrón MVC</p>
        </div>
    </footer>

    <script>
        // Validación adicional del formulario
        document.querySelector('.solicitud-form').addEventListener('submit', function(e) {
            const nombre = document.getElementById('nombre').value.trim();
            const correo = document.getElementById('correo').value.trim();
            
            if (nombre.length < 3) {
                e.preventDefault();
                alert('El nombre debe tener al menos 3 caracteres');
                return false;
            }
            
            // Validar formato de email
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(correo)) {
                e.preventDefault();
                alert('Por favor, ingrese un correo electrónico válido');
                return false;
            }
        });
    </script>
</body>
</html>
