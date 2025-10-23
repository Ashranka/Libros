<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo de Libros - Biblioteca Universitaria</title>
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
            <h2>Catálogo de Libros</h2>
            <p>Explore nuestra colección y solicite préstamos</p>
        </div>

        <!-- Barra de búsqueda -->
        <div class="search-section">
            <form action="${pageContext.request.contextPath}/catalogo" method="get" class="search-form">
                <input type="text" 
                       name="busqueda" 
                       placeholder="Buscar por título o autor..." 
                       value="${busqueda}"
                       class="search-input">
                <button type="submit" class="btn btn-primary">🔍 Buscar</button>
                <c:if test="${not empty busqueda}">
                    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-secondary">Limpiar</a>
                </c:if>
            </form>
        </div>

        <!-- Estadísticas -->
        <div class="stats-container">
            <div class="stat-card">
                <div class="stat-number">${totalLibros}</div>
                <div class="stat-label">Total de Libros</div>
            </div>
            <div class="stat-card success">
                <div class="stat-number">${librosDisponibles}</div>
                <div class="stat-label">Disponibles</div>
            </div>
            <div class="stat-card warning">
                <div class="stat-number">${librosPrestados}</div>
                <div class="stat-label">Prestados</div>
            </div>
        </div>

        <!-- Mensajes -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ⚠️ ${error}
            </div>
        </c:if>

        <c:if test="${not empty mensaje}">
            <div class="alert alert-info">
                ℹ️ ${mensaje}
            </div>
        </c:if>

        <!-- Tabla de libros -->
        <c:if test="${not empty libros}">
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Título</th>
                            <th>Autor</th>
                            <th>Editorial</th>
                            <th>Categoría</th>
                            <th>Año</th>
                            <th>Estado</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="libro" items="${libros}">
                            <tr>
                                <td>${libro.id}</td>
                                <td class="libro-titulo">${libro.titulo}</td>
                                <td>${libro.autor}</td>
                                <td>${libro.editorial}</td>
                                <td><span class="categoria">${libro.categoria}</span></td>
                                <td>${libro.anioPublicacion}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${libro.disponible}">
                                            <span class="badge badge-success">✓ Disponible</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-error">✗ Prestado</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${libro.disponible}">
                                            <a href="${pageContext.request.contextPath}/solicitud?libroId=${libro.id}" 
                                               class="btn btn-sm btn-primary">
                                                Solicitar
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-sm btn-disabled" disabled>
                                                No disponible
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2025 Biblioteca Universitaria. Sistema de Gestión de Préstamos.</p>
            <p>Desarrollado con Java EE - Patrón MVC</p>
        </div>
    </footer>
</body>
</html>
