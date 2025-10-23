package servlet;

import DAO.LibroDAO;
import DAO.SolicitudDAO;
import model.Libro;
import model.Solicitud;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet controlador para gestionar solicitudes de préstamo
 * Maneja peticiones GET (mostrar formulario) y POST (procesar formulario)
 */
@WebServlet(name = "SolicitudServlet", urlPatterns = {"/solicitud"})
public class SolicitudServlet extends HttpServlet {
    
    private SolicitudDAO solicitudDAO;
    private LibroDAO libroDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        solicitudDAO = new SolicitudDAO();
        libroDAO = new LibroDAO();
    }
    
    /**
     * Maneja peticiones GET para mostrar el formulario de solicitud
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener el ID del libro desde el parámetro
            String libroIdParam = request.getParameter("libroId");
            
            if (libroIdParam != null && !libroIdParam.isEmpty()) {
                int libroId = Integer.parseInt(libroIdParam);
                
                // Obtener información del libro
                Libro libro = libroDAO.obtenerLibroPorId(libroId);
                
                if (libro != null) {
                    // Verificar si el libro está disponible
                    if (!libro.isDisponible()) {
                        request.setAttribute("error", 
                            "El libro '" + libro.getTitulo() + "' no está disponible actualmente.");
                        request.getRequestDispatcher("/index.jsp").forward(request, response);
                        return;
                    }
                    
                    request.setAttribute("libro", libro);
                    request.getRequestDispatcher("/solicitud.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Libro no encontrado.");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
            } else {
                // Si no hay ID de libro, mostrar formulario genérico
                request.getRequestDispatcher("/solicitud.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de libro inválido.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Maneja peticiones POST para procesar el formulario de solicitud
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener datos del formulario
            String nombre = request.getParameter("nombre");
            String correo = request.getParameter("correo");
            String libroIdParam = request.getParameter("libroId");
            
            // Validar que todos los campos estén completos
            if (nombre == null || nombre.trim().isEmpty() ||
                correo == null || correo.trim().isEmpty() ||
                libroIdParam == null || libroIdParam.trim().isEmpty()) {
                
                request.setAttribute("error", 
                    "Todos los campos son obligatorios. Por favor, complete el formulario.");
                request.getRequestDispatcher("/solicitud.jsp").forward(request, response);
                return;
            }
            
            // Validar formato de correo electrónico
            if (!validarEmail(correo)) {
                request.setAttribute("error", 
                    "El formato del correo electrónico no es válido.");
                request.setAttribute("nombre", nombre);
                request.setAttribute("correo", correo);
                request.getRequestDispatcher("/solicitud.jsp").forward(request, response);
                return;
            }
            
            // Parsear ID del libro
            int libroId = Integer.parseInt(libroIdParam);
            
            // Verificar que el libro existe y está disponible
            Libro libro = libroDAO.obtenerLibroPorId(libroId);
            
            if (libro == null) {
                request.setAttribute("error", "El libro seleccionado no existe.");
                request.getRequestDispatcher("/solicitud.jsp").forward(request, response);
                return;
            }
            
            if (!libro.isDisponible()) {
                request.setAttribute("error", 
                    "El libro '" + libro.getTitulo() + "' ya no está disponible.");
                request.getRequestDispatcher("/solicitud.jsp").forward(request, response);
                return;
            }
            
            // Crear objeto Solicitud
            Solicitud solicitud = new Solicitud(nombre.trim(), correo.trim(), libroId);
            
            // Guardar en base de datos
            int solicitudId = solicitudDAO.insertarSolicitud(solicitud);
            
            if (solicitudId > 0) {
                // Éxito - preparar datos para la página de confirmación
                request.setAttribute("solicitudId", solicitudId);
                request.setAttribute("nombreUsuario", nombre.trim());
                request.setAttribute("correoUsuario", correo.trim());
                request.setAttribute("libro", libro);
                request.setAttribute("mensaje", 
                    "¡Solicitud registrada exitosamente!");
                
                // Redirigir a página de confirmación
                request.getRequestDispatcher("/confirmacion.jsp").forward(request, response);
            } else {
                // Error al guardar
                request.setAttribute("error", 
                    "Error al procesar la solicitud. Por favor, intente nuevamente.");
                request.setAttribute("nombre", nombre);
                request.setAttribute("correo", correo);
                request.setAttribute("libro", libro);
                request.getRequestDispatcher("/solicitud.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de libro inválido.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", 
                "Error al procesar la solicitud: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Valida el formato de un correo electrónico
     * @param email correo a validar
     * @return true si el formato es válido
     */
    private boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Expresión regular simple para validar email
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
}
