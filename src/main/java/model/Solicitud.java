package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Solicitud implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombreUsuario;
    private String correoUsuario;
    private int libroId;
    private String libroTitulo;
    private Timestamp fechaSolicitud;
    private String estado;

    public Solicitud() {
    }

    public Solicitud(String nombreUsuario, String correoUsuario, int libroId) {
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.libroId = libroId;
        this.estado = "PENDIENTE";
    }

    public Solicitud(int id, String nombreUsuario, String correoUsuario,
                     int libroId, String libroTitulo, Timestamp fechaSolicitud, String estado) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.libroId = libroId;
        this.libroTitulo = libroTitulo;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public int getLibroId() {
        return libroId;
    }

    public void setLibroId(int libroId) {
        this.libroId = libroId;
    }

    public String getLibroTitulo() {
        return libroTitulo;
    }

    public void setLibroTitulo(String libroTitulo) {
        this.libroTitulo = libroTitulo;
    }

    public Timestamp getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Timestamp fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Solicitud{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", correoUsuario='" + correoUsuario + '\'' +
                ", libroId=" + libroId +
                ", libroTitulo='" + libroTitulo + '\'' +
                ", fechaSolicitud=" + fechaSolicitud +
                ", estado='" + estado + '\'' +
                '}';
    }
}
