package DAO;

import model.Libro;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para gestionar operaciones de base de datos
 * relacionadas con la entidad Libro
 */
public class LibroDAO {
    
    /**
     * Obtiene todos los libros del catálogo
     * @return Lista de libros
     */
    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT * FROM libros ORDER BY titulo";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Libro libro = mapearResultSetALibro(rs);
                libros.add(libro);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener libros: " + e.getMessage());
            e.printStackTrace();
        }
        
        return libros;
    }
    
    /**
     * Obtiene solo los libros disponibles
     * @return Lista de libros disponibles
     */
    public List<Libro> obtenerLibrosDisponibles() {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT * FROM libros WHERE disponible = TRUE ORDER BY titulo";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Libro libro = mapearResultSetALibro(rs);
                libros.add(libro);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener libros disponibles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return libros;
    }
    
    /**
     * Obtiene un libro por su ID
     * @param id ID del libro
     * @return Libro encontrado o null si no existe
     */
    public Libro obtenerLibroPorId(int id) {
        String query = "SELECT * FROM libros WHERE id = ?";
        Libro libro = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    libro = mapearResultSetALibro(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener libro por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return libro;
    }
    
    /**
     * Busca libros por título o autor
     * @param busqueda término de búsqueda
     * @return Lista de libros que coinciden con la búsqueda
     */
    public List<Libro> buscarLibros(String busqueda) {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT * FROM libros WHERE " +
                      "LOWER(titulo) LIKE LOWER(?) OR LOWER(autor) LIKE LOWER(?) " +
                      "ORDER BY titulo";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            String parametroBusqueda = "%" + busqueda + "%";
            pstmt.setString(1, parametroBusqueda);
            pstmt.setString(2, parametroBusqueda);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Libro libro = mapearResultSetALibro(rs);
                    libros.add(libro);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar libros: " + e.getMessage());
            e.printStackTrace();
        }
        
        return libros;
    }
    
    /**
     * Inserta un nuevo libro en la base de datos
     * @param libro Libro a insertar
     * @return ID del libro insertado o -1 si hay error
     */
    public int insertarLibro(Libro libro) {
        String query = "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, " +
                      "disponible, editorial, categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int libroId = -1;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setString(3, libro.getIsbn());
            pstmt.setInt(4, libro.getAnioPublicacion());
            pstmt.setBoolean(5, libro.isDisponible());
            pstmt.setString(6, libro.getEditorial());
            pstmt.setString(7, libro.getCategoria());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        libroId = generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
            e.printStackTrace();
        }
        
        return libroId;
    }
    
    /**
     * Actualiza la disponibilidad de un libro
     * @param id ID del libro
     * @param disponible nueva disponibilidad
     * @return true si se actualizó correctamente
     */
    public boolean actualizarDisponibilidad(int id, boolean disponible) {
        String query = "UPDATE libros SET disponible = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setBoolean(1, disponible);
            pstmt.setInt(2, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar disponibilidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Actualiza un libro existente
     * @param libro Libro con datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizarLibro(Libro libro) {
        String query = "UPDATE libros SET titulo = ?, autor = ?, isbn = ?, " +
                      "anio_publicacion = ?, disponible = ?, editorial = ?, categoria = ? " +
                      "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setString(3, libro.getIsbn());
            pstmt.setInt(4, libro.getAnioPublicacion());
            pstmt.setBoolean(5, libro.isDisponible());
            pstmt.setString(6, libro.getEditorial());
            pstmt.setString(7, libro.getCategoria());
            pstmt.setInt(8, libro.getId());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Elimina un libro de la base de datos
     * @param id ID del libro a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarLibro(int id) {
        String query = "DELETE FROM libros WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cuenta el total de libros en el catálogo
     * @return número total de libros
     */
    public int contarLibros() {
        String query = "SELECT COUNT(*) as total FROM libros";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar libros: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Mapea un ResultSet a un objeto Libro
     * @param rs ResultSet con datos del libro
     * @return Objeto Libro
     * @throws SQLException si hay error al leer datos
     */
    private Libro mapearResultSetALibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setId(rs.getInt("id"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setIsbn(rs.getString("isbn"));
        libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
        libro.setDisponible(rs.getBoolean("disponible"));
        libro.setEditorial(rs.getString("editorial"));
        libro.setCategoria(rs.getString("categoria"));
        return libro;
    }
}
