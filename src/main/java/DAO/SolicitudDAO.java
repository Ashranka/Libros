package DAO;

import model.Solicitud;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SolicitudDAO {
    
    /**
     * Obtiene todas las solicitudes de préstamo
     * @return Lista de solicitudes
     */
    public List<Solicitud> obtenerTodasLasSolicitudes() {
        List<Solicitud> solicitudes = new ArrayList<>();
        String query = "SELECT s.id, s.nombre_usuario, s.correo_usuario, s.libro_id, " +
                      "l.titulo as libro_titulo, s.fecha_solicitud, s.estado " +
                      "FROM solicitudes s " +
                      "INNER JOIN libros l ON s.libro_id = l.id " +
                      "ORDER BY s.fecha_solicitud DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Solicitud solicitud = mapearResultSetASolicitud(rs);
                solicitudes.add(solicitud);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return solicitudes;
    }
    
    /**
     * Obtiene solicitudes por estado
     * @param estado Estado de la solicitud (PENDIENTE, APROBADA, RECHAZADA)
     * @return Lista de solicitudes con el estado especificado
     */
    public List<Solicitud> obtenerSolicitudesPorEstado(String estado) {
        List<Solicitud> solicitudes = new ArrayList<>();
        String query = "SELECT s.id, s.nombre_usuario, s.correo_usuario, s.libro_id, " +
                      "l.titulo as libro_titulo, s.fecha_solicitud, s.estado " +
                      "FROM solicitudes s " +
                      "INNER JOIN libros l ON s.libro_id = l.id " +
                      "WHERE s.estado = ? " +
                      "ORDER BY s.fecha_solicitud DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, estado);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Solicitud solicitud = mapearResultSetASolicitud(rs);
                    solicitudes.add(solicitud);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return solicitudes;
    }
    
    /**
     * Obtiene una solicitud por su ID
     * @param id ID de la solicitud
     * @return Solicitud encontrada o null si no existe
     */
    public Solicitud obtenerSolicitudPorId(int id) {
        String query = "SELECT s.id, s.nombre_usuario, s.correo_usuario, s.libro_id, " +
                      "l.titulo as libro_titulo, s.fecha_solicitud, s.estado " +
                      "FROM solicitudes s " +
                      "INNER JOIN libros l ON s.libro_id = l.id " +
                      "WHERE s.id = ?";
        Solicitud solicitud = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    solicitud = mapearResultSetASolicitud(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener solicitud por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return solicitud;
    }
    
    /**
     * Inserta una nueva solicitud de préstamo
     * @param solicitud Solicitud a insertar
     * @return ID de la solicitud insertada o -1 si hay error
     */
    public int insertarSolicitud(Solicitud solicitud) {
        String query = "INSERT INTO solicitudes (nombre_usuario, correo_usuario, libro_id, estado) " +
                      "VALUES (?, ?, ?, ?)";
        int solicitudId = -1;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, solicitud.getNombreUsuario());
            pstmt.setString(2, solicitud.getCorreoUsuario());
            pstmt.setInt(3, solicitud.getLibroId());
            pstmt.setString(4, solicitud.getEstado() != null ? solicitud.getEstado() : "PENDIENTE");
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        solicitudId = generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar solicitud: " + e.getMessage());
            e.printStackTrace();
        }
        
        return solicitudId;
    }
    
    /**
     * Actualiza el estado de una solicitud
     * @param id ID de la solicitud
     * @param nuevoEstado Nuevo estado (PENDIENTE, APROBADA, RECHAZADA)
     * @return true si se actualizó correctamente
     */
    public boolean actualizarEstadoSolicitud(int id, String nuevoEstado) {
        String query = "UPDATE solicitudes SET estado = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de solicitud: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Elimina una solicitud de la base de datos
     * @param id ID de la solicitud a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarSolicitud(int id) {
        String query = "DELETE FROM solicitudes WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar solicitud: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene solicitudes de un usuario específico por su correo
     * @param correoUsuario Correo del usuario
     * @return Lista de solicitudes del usuario
     */
    public List<Solicitud> obtenerSolicitudesPorUsuario(String correoUsuario) {
        List<Solicitud> solicitudes = new ArrayList<>();
        String query = "SELECT s.id, s.nombre_usuario, s.correo_usuario, s.libro_id, " +
                      "l.titulo as libro_titulo, s.fecha_solicitud, s.estado " +
                      "FROM solicitudes s " +
                      "INNER JOIN libros l ON s.libro_id = l.id " +
                      "WHERE s.correo_usuario = ? " +
                      "ORDER BY s.fecha_solicitud DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, correoUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Solicitud solicitud = mapearResultSetASolicitud(rs);
                    solicitudes.add(solicitud);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes por usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return solicitudes;
    }
    
    /**
     * Cuenta el total de solicitudes en la base de datos
     * @return número total de solicitudes
     */
    public int contarSolicitudes() {
        String query = "SELECT COUNT(*) as total FROM solicitudes";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar solicitudes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Cuenta las solicitudes por estado
     * @param estado Estado a contar
     * @return número de solicitudes con ese estado
     */
    public int contarSolicitudesPorEstado(String estado) {
        String query = "SELECT COUNT(*) as total FROM solicitudes WHERE estado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, estado);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar solicitudes por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Mapea un ResultSet a un objeto Solicitud
     * @param rs ResultSet con datos de la solicitud
     * @return Objeto Solicitud
     * @throws SQLException si hay error al leer datos
     */
    private Solicitud mapearResultSetASolicitud(ResultSet rs) throws SQLException {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(rs.getInt("id"));
        solicitud.setNombreUsuario(rs.getString("nombre_usuario"));
        solicitud.setCorreoUsuario(rs.getString("correo_usuario"));
        solicitud.setLibroId(rs.getInt("libro_id"));
        solicitud.setLibroTitulo(rs.getString("libro_titulo"));
        solicitud.setFechaSolicitud(rs.getTimestamp("fecha_solicitud"));
        solicitud.setEstado(rs.getString("estado"));
        return solicitud;
    }
}
