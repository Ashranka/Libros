package servlet;

import DAO.LibroDAO;
import model.Libro;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet controlador para gestionar el catálogo de libros
 * Maneja peticiones GET para mostrar el listado de libros
 */
@WebServlet(name = "CatalogoServlet", urlPatterns = {"/catalogo", "/index"})
public class CatalogoServlet extends HttpServlet {
    
    private LibroDAO libroDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        libroDAO = new LibroDAO();
    }
    
    /**
     * Maneja peticiones GET para mostrar el catálogo
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Configurar encoding para caracteres especiales
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener parámetro de búsqueda si existe
            String busqueda = request.getParameter("busqueda");
            List<Libro> libros;
            
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                // Si hay búsqueda, filtrar libros
                libros = libroDAO.buscarLibros(busqueda);
                request.setAttribute("busqueda", busqueda);
            } else {
                // Obtener todos los libros
                libros = libroDAO.obtenerTodosLosLibros();
            }
            
            // Calcular estadísticas
            int totalLibros = libros.size();
            long librosDisponibles = libros.stream().filter(Libro::isDisponible).count();
            long librosPrestados = totalLibros - librosDisponibles;
            
            // Establecer atributos para la vista
            request.setAttribute("libros", libros);
            request.setAttribute("totalLibros", totalLibros);
            request.setAttribute("librosDisponibles", librosDisponibles);
            request.setAttribute("librosPrestados", librosPrestados);
            
            // Mensaje de información si no hay resultados
            if (libros.isEmpty()) {
                if (busqueda != null && !busqueda.trim().isEmpty()) {
                    request.setAttribute("mensaje", 
                        "No se encontraron libros que coincidan con: " + busqueda);
                } else {
                    request.setAttribute("mensaje", 
                        "El catálogo está vacío. Por favor, contacte al administrador.");
                }
            }
            
            // Redirigir a la página JSP
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Manejo de errores
            request.setAttribute("error", 
                "Error al cargar el catálogo: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Maneja peticiones POST (redirige a GET)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
