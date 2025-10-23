package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    static final String MYSQL_URL = "jdbc:mysql://localhost:3306/biblioteca_univeritaria";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String PARAMETROS = "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";


    private static final String URL = MYSQL_URL;
    private static final String USER = MYSQL_USER;
    private static final String PASSWORD = MYSQL_PASSWORD;

    // ✅ CORRECCIÓN: Ya NO lanzamos RuntimeException
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("✓ Driver MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            // Solo imprimimos el error, NO lanzamos excepción
            System.err.println("✗ ADVERTENCIA: No se pudo cargar el driver MySQL");
            System.err.println("  Asegúrese de que mysql-connector-java.jar está en WEB-INF/lib");
            e.printStackTrace();
            // ❌ NO USAR: throw new RuntimeException(...);
        }
    }

    /**
     * Obtiene una conexión a la base de datos
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + PARAMETROS, USER, PASSWORD);
    }

    /**
     * Cierra la conexión proporcionada
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Inicializa la base de datos creando las tablas necesarias
     */
    public static void inicializarBaseDatos() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            // Crear tabla libros
            String createLibrosTable =
                    "CREATE TABLE IF NOT EXISTS libros (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "titulo VARCHAR(255) NOT NULL, " +
                            "autor VARCHAR(255) NOT NULL, " +
                            "isbn VARCHAR(20), " +
                            "anio_publicacion INT, " +
                            "disponible BOOLEAN DEFAULT TRUE, " +
                            "editorial VARCHAR(255), " +
                            "categoria VARCHAR(100)" +
                            ")";
            stmt.execute(createLibrosTable);

            // Crear tabla solicitudes
            String createSolicitudesTable =
                    "CREATE TABLE IF NOT EXISTS solicitudes (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "nombre_usuario VARCHAR(255) NOT NULL, " +
                            "correo_usuario VARCHAR(255) NOT NULL, " +
                            "libro_id INT NOT NULL, " +
                            "fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "estado VARCHAR(50) DEFAULT 'PENDIENTE', " +
                            "FOREIGN KEY (libro_id) REFERENCES libros(id)" +
                            ")";
            stmt.execute(createSolicitudesTable);

            // Insertar datos de ejemplo si la tabla está vacía
            String checkData = "SELECT COUNT(*) as count FROM libros";
            var rs = stmt.executeQuery(checkData);
            if (rs.next() && rs.getInt("count") == 0) {
                insertarDatosEjemplo(stmt);
            }

            System.out.println("✓ Base de datos inicializada correctamente");

        } catch (SQLException e) {
            System.err.println("✗ Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inserta datos de ejemplo en la tabla libros
     */
    private static void insertarDatosEjemplo(Statement stmt) throws SQLException {
        String[] librosEjemplo = {
                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('Cien Años de Soledad', 'Gabriel García Márquez', '978-0307474728', 1967, TRUE, 'Editorial Sudamericana', 'Ficción')",

                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('El Principito', 'Antoine de Saint-Exupéry', '978-0156012195', 1943, TRUE, 'Reynal & Hitchcock', 'Infantil')",

                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('Don Quijote de la Mancha', 'Miguel de Cervantes', '978-8420412146', 1605, TRUE, 'Francisco de Robles', 'Clásicos')",

                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('1984', 'George Orwell', '978-0451524935', 1949, FALSE, 'Secker & Warburg', 'Ciencia Ficción')",

                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('Crónica de una Muerte Anunciada', 'Gabriel García Márquez', '978-0307387493', 1981, TRUE, 'Editorial La Oveja Negra', 'Ficción')",

                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('El Aleph', 'Jorge Luis Borges', '978-8420633138', 1949, TRUE, 'Editorial Losada', 'Cuentos')",

                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('La Sombra del Viento', 'Carlos Ruiz Zafón', '978-8408163374', 2001, TRUE, 'Editorial Planeta', 'Misterio')",

                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('Rayuela', 'Julio Cortázar', '978-8437604572', 1963, FALSE, 'Editorial Sudamericana', 'Ficción')",

                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('La Casa de los Espíritus', 'Isabel Allende', '978-0553383805', 1982, TRUE, 'Plaza & Janés', 'Ficción')",

                "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, disponible, editorial, categoria) " +
                        "VALUES ('Ficciones', 'Jorge Luis Borges', '978-0802130303', 1944, TRUE, 'Editorial Sur', 'Cuentos')"
        };

        for (String insert : librosEjemplo) {
            stmt.execute(insert);
        }

        System.out.println("✓ Datos de ejemplo insertados correctamente");
    }

    /**
     * Método de prueba para verificar la conexión
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Conexión a la base de datos exitosa");
                System.out.println("  URL: " + URL);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}