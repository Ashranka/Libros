package servlet;


import DAO.SolicitudDAO;
import model.Solicitud;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet controlador para el panel de administración
 * Permite visualizar todas las solicitudes de préstamo
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {
    
    private SolicitudDAO solicitudDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        solicitudDAO = new SolicitudDAO();
    }
    
    /**
     * Maneja peticiones GET para mostrar el panel de administración
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener parámetro de filtro por estado (opcional)
            String filtroEstado = request.getParameter("estado");
            List<Solicitud> solicitudes;
            
            if (filtroEstado != null && !filtroEstado.isEmpty() && !filtroEstado.equals("TODAS")) {
                // Filtrar por estado
                solicitudes = solicitudDAO.obtenerSolicitudesPorEstado(filtroEstado);
                request.setAttribute("filtroActual", filtroEstado);
            } else {
                // Obtener todas las solicitudes
                solicitudes = solicitudDAO.obtenerTodasLasSolicitudes();
                request.setAttribute("filtroActual", "TODAS");
            }
            
            // Calcular estadísticas
            int totalSolicitudes = solicitudDAO.contarSolicitudes();
            int solicitudesPendientes = solicitudDAO.contarSolicitudesPorEstado("PENDIENTE");
            int solicitudesAprobadas = solicitudDAO.contarSolicitudesPorEstado("APROBADA");
            int solicitudesRechazadas = solicitudDAO.contarSolicitudesPorEstado("RECHAZADA");
            
            // Establecer atributos para la vista
            request.setAttribute("solicitudes", solicitudes);
            request.setAttribute("totalSolicitudes", totalSolicitudes);
            request.setAttribute("solicitudesPendientes", solicitudesPendientes);
            request.setAttribute("solicitudesAprobadas", solicitudesAprobadas);
            request.setAttribute("solicitudesRechazadas", solicitudesRechazadas);
            
            // Mensaje informativo si no hay solicitudes
            if (solicitudes.isEmpty()) {
                if (filtroEstado != null && !filtroEstado.isEmpty() && !filtroEstado.equals("TODAS")) {
                    request.setAttribute("mensaje", 
                        "No hay solicitudes con estado: " + filtroEstado);
                } else {
                    request.setAttribute("mensaje", 
                        "No hay solicitudes registradas en el sistema.");
                }
            }
            
            // Redirigir a la página JSP de administración
            request.getRequestDispatcher("/admin.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", 
                "Error al cargar las solicitudes: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Maneja peticiones POST para actualizar estados de solicitudes
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String accion = request.getParameter("accion");
            String solicitudIdParam = request.getParameter("solicitudId");
            
            if (accion == null || solicitudIdParam == null) {
                request.setAttribute("error", "Parámetros inválidos.");
                doGet(request, response);
                return;
            }
            
            int solicitudId = Integer.parseInt(solicitudIdParam);
            boolean exito = false;
            String mensaje = "";
            
            switch (accion) {
                case "aprobar":
                    exito = solicitudDAO.actualizarEstadoSolicitud(solicitudId, "APROBADA");
                    mensaje = exito ? "Solicitud aprobada exitosamente." 
                                   : "Error al aprobar la solicitud.";
                    break;
                    
                case "rechazar":
                    exito = solicitudDAO.actualizarEstadoSolicitud(solicitudId, "RECHAZADA");
                    mensaje = exito ? "Solicitud rechazada exitosamente." 
                                   : "Error al rechazar la solicitud.";
                    break;
                    
                case "eliminar":
                    exito = solicitudDAO.eliminarSolicitud(solicitudId);
                    mensaje = exito ? "Solicitud eliminada exitosamente." 
                                   : "Error al eliminar la solicitud.";
                    break;
                    
                default:
                    mensaje = "Acción no reconocida.";
            }

            if (exito) {
                request.setAttribute("mensajeExito", mensaje);
            } else {
                request.setAttribute("mensajeError", mensaje);
            }
            

            doGet(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de solicitud inválido.");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", 
                "Error al procesar la acción: " + e.getMessage());
            doGet(request, response);
        }
    }
}
