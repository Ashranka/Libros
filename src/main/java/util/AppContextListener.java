package util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=================================================");
        System.out.println("Iniciando aplicación: Biblioteca Universitaria");
        System.out.println("=================================================");

        try {
            // Probar conexión a la base de datos
            DatabaseConnection.testConnection();

            // Inicializar base de datos (crear tablas e insertar datos)
            DatabaseConnection.inicializarBaseDatos();

            System.out.println("✓ Aplicación iniciada correctamente");
            System.out.println("✓ Base de datos lista");
            System.out.println("=================================================");

        } catch (Exception e) {
            System.err.println("=================================================");
            System.err.println("✗ ADVERTENCIA: Error al inicializar la aplicación");
            System.err.println("=================================================");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=================================================");
            System.err.println("Verifique:");
            System.err.println("1. MySQL está corriendo");
            System.err.println("2. Base de datos '2biblioteca_univeritaria' existe");
            System.err.println("3. Usuario 'root' tiene password 'password'");
            System.err.println("4. mysql-connector-java.jar está en WEB-INF/lib");
            System.err.println("=================================================");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=================================================");
        System.out.println("Deteniendo aplicación: Biblioteca Universitaria");
        System.out.println("=================================================");
    }
}