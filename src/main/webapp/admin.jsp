<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Administración - Biblioteca Universitaria</title>
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
            <h2>🔧 Panel de Administración</h2>
            <p>Gestión de Solicitudes de Préstamo</p>
        </div>

        <!-- Mensajes -->
        <c:if test="${not empty mensajeExito}">
            <div class="alert alert-success">
                ✅ ${mensajeExito}
            </div>
        </c:if>

        <c:if test="${not empty mensajeError}">
            <div class="alert alert-error">
                ⚠️ ${mensajeError}
            </div>
        </c:if>

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

        <!-- Estadísticas -->
        <div class="stats-container">
            <div class="stat-card">
                <div class="stat-number">${totalSolicitudes}</div>
                <div class="stat-label">Total Solicitudes</div>
            </div>
            <div class="stat-card warning">
                <div class="stat-number">${solicitudesPendientes}</div>
                <div class="stat-label">Pendientes</div>
            </div>
            <div class="stat-card success">
                <div class="stat-number">${solicitudesAprobadas}</div>
                <div class="stat-label">Aprobadas</div>
            </div>
            <div class="stat-card error">
                <div class="stat-number">${solicitudesRechazadas}</div>
                <div class="stat-label">Rechazadas</div>
            </div>
        </div>

        <!-- Filtros -->
        <div class="filter-section">
            <form action="${pageContext.request.contextPath}/admin" method="get" class="filter-form">
                <label for="estado">Filtrar por estado:</label>
                <select name="estado" id="estado" class="form-select" onchange="this.form.submit()">
                    <option value="TODAS" ${filtroActual == 'TODAS' ? 'selected' : ''}>Todas</option>
                    <option value="PENDIENTE" ${filtroActual == 'PENDIENTE' ? 'selected' : ''}>Pendientes</option>
                    <option value="APROBADA" ${filtroActual == 'APROBADA' ? 'selected' : ''}>Aprobadas</option>
                    <option value="RECHAZADA" ${filtroActual == 'RECHAZADA' ? 'selected' : ''}>Rechazadas</option>
                </select>
            </form>
        </div>

        <!-- Tabla de solicitudes -->
        <c:if test="${not empty solicitudes}">
            <div class="table-container">
                <table class="data-table admin-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Usuario</th>
                            <th>Correo</th>
                            <th>Libro</th>
                            <th>Fecha</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="solicitud" items="${solicitudes}">
                            <tr>
                                <td><strong>#${solicitud.id}</strong></td>
                                <td>${solicitud.nombreUsuario}</td>
                                <td>${solicitud.correoUsuario}</td>
                                <td class="libro-titulo">${solicitud.libroTitulo}</td>
                                <td>
                                    <fmt:formatDate value="${solicitud.fechaSolicitud}" 
                                                   pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${solicitud.estado == 'PENDIENTE'}">
                                            <span class="badge badge-warning">⏳ Pendiente</span>
                                        </c:when>
                                        <c:when test="${solicitud.estado == 'APROBADA'}">
                                            <span class="badge badge-success">✓ Aprobada</span>
                                        </c:when>
                                        <c:when test="${solicitud.estado == 'RECHAZADA'}">
                                            <span class="badge badge-error">✗ Rechazada</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td class="actions-cell">
                                    <c:if test="${solicitud.estado == 'PENDIENTE'}">
                                        <form action="${pageContext.request.contextPath}/admin" 
                                              method="post" 
                                              style="display: inline;"
                                              onsubmit="return confirm('¿Está seguro de aprobar esta solicitud?');">
                                            <input type="hidden" name="solicitudId" value="${solicitud.id}">
                                            <input type="hidden" name="accion" value="aprobar">
                                            <button type="submit" class="btn btn-xs btn-success" title="Aprobar">
                                                ✓ Aprobar
                                            </button>
                                        </form>

                                        <form action="${pageContext.request.contextPath}/admin" 
                                              method="post" 
                                              style="display: inline;"
                                              onsubmit="return confirm('¿Está seguro de rechazar esta solicitud?');">
                                            <input type="hidden" name="solicitudId" value="${solicitud.id}">
                                            <input type="hidden" name="accion" value="rechazar">
                                            <button type="submit" class="btn btn-xs btn-error" title="Rechazar">
                                                ✗ Rechazar
                                            </button>
                                        </form>
                                    </c:if>

                                    <form action="${pageContext.request.contextPath}/admin" 
                                          method="post" 
                                          style="display: inline;"
                                          onsubmit="return confirm('¿Está seguro de eliminar esta solicitud? Esta acción no se puede deshacer.');">
                                        <input type="hidden" name="solicitudId" value="${solicitud.id}">
                                        <input type="hidden" name="accion" value="eliminar">
                                        <button type="submit" class="btn btn-xs btn-secondary" title="Eliminar">
                                            🗑️ Eliminar
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <!-- Información adicional -->
        <div class="admin-info">
            <h3>📊 Información del Sistema</h3>
            <p>Esta página permite gestionar todas las solicitudes de préstamo de libros.</p>
            <ul>
                <li>Use los filtros para ver solicitudes por estado</li>
                <li>Apruebe o rechace solicitudes pendientes</li>
                <li>Elimine solicitudes que ya no son relevantes</li>
                <li>El sistema mantiene un historial de todas las solicitudes</li>
            </ul>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2025 Biblioteca Universitaria. Sistema de Gestión de Préstamos.</p>
            <p>Desarrollado con Java EE - Patrón MVC</p>
        </div>
    </footer>

    <script>
        // Auto-refrescar cada 30 segundos si hay solicitudes pendientes
        <c:if test="${solicitudesPendientes > 0}">
            setTimeout(function() {
                location.reload();
            }, 30000);
        </c:if>
    </script>
</body>
</html>
