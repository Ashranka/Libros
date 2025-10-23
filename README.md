# 📚 Sistema de Gestión de Biblioteca Universitaria

## Descripción del Proyecto

Aplicación web dinámica desarrollada con tecnologías Java EE que permite a los usuarios consultar el catálogo de libros disponibles y registrar solicitudes de préstamo. El sistema incluye un panel de administración para gestionar las solicitudes recibidas.

**Características principales:**
- Catálogo interactivo de libros con búsqueda
- Sistema de solicitud de préstamos
- Panel de administración para gestión de solicitudes
- Base de datos MySQL con inicialización automática
- Arquitectura MVC (Modelo-Vista-Controlador)
- Patrón DAO para acceso a datos

---
PD: el war se encuentra en la carpeta target
## 🏗️ Arquitectura del Sistema

### Patrón MVC Implementado

```
┌─────────────────────────────────────────────────────────────┐
│                         CLIENTE                              │
│                      (Navegador Web)                         │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    VISTA (JSP)                               │
│  - index.jsp         (Catálogo de libros)                   │
│  - solicitud.jsp     (Formulario de préstamo)               │
│  - confirmacion.jsp  (Confirmación)                          │
│  - admin.jsp         (Panel administrativo)                  │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                  CONTROLADOR (Servlets)                      │
│  - CatalogoServlet    (GET: Lista libros)                   │
│  - SolicitudServlet   (GET/POST: Gestiona préstamos)        │
│  - AdminServlet       (GET/POST: Panel admin)               │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    MODELO (Clases Java)                      │
│  - Libro.java        (Entidad Libro)                        │
│  - Solicitud.java    (Entidad Solicitud)                    │
│  - LibroDAO.java     (Acceso a datos - Libros)              │
│  - SolicitudDAO.java (Acceso a datos - Solicitudes)         │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│              BASE DE DATOS (MySQL)                           │
│  - Tabla: libros                                             │
│  - Tabla: solicitudes                                        │
│  ※ Se crea automáticamente al iniciar la aplicación         │
└─────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java EE** | 8+ | Framework principal |
| **JSP** | 2.3+ | Capa de presentación |
| **Servlets** | 4.0+ | Controladores |
| **JSTL** | 1.2 | Etiquetas JSP |
| **MySQL** | 5.7+ / 8.0+ | Base de datos |
| **JDBC** | - | Conectividad BD |
| **Apache Tomcat** | 9.0+ | Servidor de aplicaciones |

---

## 📋 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

1. **JDK (Java Development Kit)**
    - Versión: 8 o superior
    - Descargar: https://www.oracle.com/java/technologies/downloads/

2. **Apache Tomcat**
    - Versión: 9.0 o superior
    - Descargar: https://tomcat.apache.org/download-90.cgi

3. **MySQL Server**
    - Versión: 5.7 o superior (recomendado 8.0+)
    - Descargar: https://dev.mysql.com/downloads/mysql/

4. **MySQL Connector/J**
    - Driver JDBC para MySQL
    - Descargar: https://dev.mysql.com/downloads/connector/j/

---

## 🗄️ Configuración de la Base de Datos

### Paso 1: Crear la Base de Datos

La base de datos se crea automáticamente al iniciar la aplicación, pero primero debes crearla manualmente en MySQL:

```sql
-- Conectarse a MySQL
mysql -u root -p

-- Crear la base de datos
CREATE DATABASE biblioteca_univeritaria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verificar que se creó correctamente
SHOW DATABASES;

-- Salir de MySQL
EXIT;
```

### Paso 2: Configurar Credenciales

Edita el archivo `src/util/DatabaseConnection.java` si necesitas cambiar las credenciales:

```java
static final String MYSQL_URL = "jdbc:mysql://localhost:3306/biblioteca_univeritaria";
private static final String MYSQL_USER = "root";
private static final String MYSQL_PASSWORD = "root";  // ← Cambia esto si es necesario
```

### Paso 3: Inicialización Automática

**✅ Las tablas y datos de ejemplo se crean automáticamente** al iniciar la aplicación por primera vez. El sistema:

1. Verifica la conexión a la base de datos
2. Crea las tablas `libros` y `solicitudes` si no existen
3. Inserta 10 libros de ejemplo si la tabla está vacía
4. Muestra mensajes de confirmación en la consola

#### Estructura de Tablas Creadas Automáticamente:

**Tabla: libros**
```sql
CREATE TABLE libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    isbn VARCHAR(20),
    anio_publicacion INT,
    disponible BOOLEAN DEFAULT TRUE,
    editorial VARCHAR(255),
    categoria VARCHAR(100)
);
```

**Tabla: solicitudes**
```sql
CREATE TABLE solicitudes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(255) NOT NULL,
    correo_usuario VARCHAR(255) NOT NULL,
    libro_id INT NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    FOREIGN KEY (libro_id) REFERENCES libros(id)
);
```

---

## 📦 Estructura del Proyecto

```
BibliotecaUniversitaria/
│
├── src/
│   ├── model/
│   │   ├── Libro.java              # Entidad Libro
│   │   └── Solicitud.java          # Entidad Solicitud
│   │
│   ├── DAO/
│   │   ├── LibroDAO.java           # CRUD para libros
│   │   └── SolicitudDAO.java       # CRUD para solicitudes
│   │
│   ├── servlet/
│   │   ├── CatalogoServlet.java    # Controlador del catálogo
│   │   ├── SolicitudServlet.java   # Controlador de solicitudes
│   │   └── AdminServlet.java       # Controlador del panel admin
│   │
│   └── util/
│       ├── DatabaseConnection.java      # Gestión de conexiones
│       └── AppContextListener.java      # Inicialización automática
│
├── webapp/
│   ├── WEB-INF/
│   │   ├── web.xml                 # Descriptor de despliegue
│   │   └── lib/
│   │       └── mysql-connector-java.jar
│   │
│   ├── index.jsp                   # Catálogo de libros
│   ├── solicitud.jsp               # Formulario de préstamo
│   ├── confirmacion.jsp            # Confirmación de solicitud
│   ├── admin.jsp                   # Panel administrativo
│   ├── error.jsp                   # Página de error
│   │
│   └── css/
│       └── styles.css              # Estilos de la aplicación
│
└── README.md                       # Este archivo
```

---

## 🚀 Instrucciones de Instalación y Despliegue

### Método 1: Despliegue Manual (Sin Maven)

#### 1. Compilar el Proyecto

```bash
# Navegar al directorio del proyecto
cd BibliotecaUniversitaria

# Crear directorios de salida
mkdir -p build/classes

# Compilar clases Java (ajustar rutas según tu sistema)
javac -cp "lib/servlet-api.jar;lib/mysql-connector-java.jar;lib/jstl-1.2.jar" \
      -d build/classes \
      src/model/*.java \
      src/DAO/*.java \
      src/servlet/*.java \
      src/util/*.java
```


#### 2. Crear el archivo WAR

```bash
# Crear estructura WAR
mkdir -p build/war/WEB-INF/classes
mkdir -p build/war/WEB-INF/lib

# Copiar clases compiladas
cp -r build/classes/* build/war/WEB-INF/classes/

# Copiar librerías
cp lib/mysql-connector-java.jar build/war/WEB-INF/lib/
cp lib/jstl-1.2.jar build/war/WEB-INF/lib/
cp lib/standard-1.1.2.jar build/war/WEB-INF/lib/

# Copiar web.xml
cp webapp/WEB-INF/web.xml build/war/WEB-INF/

# Copiar archivos JSP y recursos
cp webapp/*.jsp build/war/
cp -r webapp/css build/war/

# Crear archivo WAR
cd build/war
jar -cvf ../BibliotecaUniversitaria.war *
cd ../..
```

#### 3. Desplegar en Tomcat

**Opción A: Copiar WAR al directorio webapps**
```bash
# Copiar el archivo WAR
cp build/BibliotecaUniversitaria.war /ruta/a/tomcat/webapps/

# Tomcat lo desplegará automáticamente
```

**Opción B: Despliegue manual**
```bash
# Extraer WAR en webapps
mkdir /ruta/a/tomcat/webapps/BibliotecaUniversitaria
cd /ruta/a/tomcat/webapps/BibliotecaUniversitaria
jar -xvf /ruta/al/BibliotecaUniversitaria.war
```

#### 4. Configurar MySQL Connector en Tomcat

Copia el driver MySQL a la carpeta `lib` de Tomcat (opcional, pero recomendado):

```bash
cp lib/mysql-connector-java.jar /ruta/a/tomcat/lib/
```

#### 5. Iniciar Tomcat

**En Windows:**
```bash
cd C:\ruta\a\tomcat\bin
startup.bat
```


#### 6. Verificar el Despliegue

Revisa los logs de Tomcat para confirmar que la aplicación se inició correctamente:

```bash
# Ver logs en tiempo real
tail -f /ruta/a/tomcat/logs/catalina.out
```

Busca estos mensajes:
```
=================================================
Iniciando aplicación: Biblioteca Universitaria
=================================================
✓ Driver MySQL cargado correctamente
✓ Conexión a la base de datos exitosa
✓ Base de datos inicializada correctamente
✓ Datos de ejemplo insertados correctamente
✓ Aplicación iniciada correctamente
✓ Base de datos lista
=================================================
```

### Método 2: Despliegue desde IDE (Eclipse/IntelliJ)

#### Eclipse:

1. Importar proyecto como "Dynamic Web Project"
2. Configurar servidor Tomcat en Eclipse
3. Click derecho en el proyecto → Run As → Run on Server

#### IntelliJ IDEA:

1. File → Project Structure → Artifacts → Add Web Application Archive
2. Run → Edit Configurations → Add Tomcat Server
3. Deploy artifact y ejecutar

---

## 🌐 Acceso a la Aplicación

Una vez desplegada, accede a la aplicación en:

```
http://localhost:8080/BibliotecaUniversitaria/
```

### Rutas Disponibles:

| URL | Descripción |
|-----|-------------|
| `/` o `/index` o `/catalogo` | Página principal - Catálogo de libros |
| `/solicitud?libroId=X` | Formulario de solicitud de préstamo |
| `/admin` | Panel de administración |
| `/admin?estado=PENDIENTE` | Filtrar solicitudes por estado |

---

## 📱 Funcionalidades del Sistema

### 1. Catálogo de Libros (index.jsp)

**Características:**
- Visualización de todos los libros disponibles
- Búsqueda por título o autor
- Indicador de disponibilidad
- Botón de solicitud de préstamo para libros disponibles
- Estadísticas del catálogo

**Acciones del usuario:**
- Buscar libros
- Ver detalles de cada libro
- Solicitar préstamo de libros disponibles

### 2. Solicitud de Préstamo (solicitud.jsp)

**Formulario incluye:**
- Nombre del usuario (obligatorio)
- Correo electrónico (obligatorio, validado)
- Libro seleccionado (preseleccionado desde el catálogo)

**Validaciones implementadas:**
- Campos obligatorios
- Formato de email válido
- Verificación de disponibilidad del libro
- Prevención de solicitudes duplicadas

### 3. Confirmación (confirmacion.jsp)

Página de confirmación que muestra:
- ID de la solicitud generada
- Datos del usuario
- Información del libro solicitado
- Mensaje de éxito

### 4. Panel de Administración (admin.jsp)

**Funcionalidades:**
- Ver todas las solicitudes
- Filtrar por estado (PENDIENTE, APROBADA, RECHAZADA)
- Aprobar solicitudes pendientes
- Rechazar solicitudes
- Eliminar solicitudes
- Estadísticas en tiempo real

**Estados de solicitud:**
- 🕐 **PENDIENTE**: Solicitud recién creada
- ✅ **APROBADA**: Préstamo autorizado
- ❌ **RECHAZADA**: Préstamo denegado

---

## 🔧 Configuración Avanzada

### Cambiar Puerto de Tomcat

Edita `tomcat/conf/server.xml`:

```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

Cambia `8080` por el puerto deseado.

### Configurar Pool de Conexiones

Para producción, se recomienda usar un pool de conexiones. Edita `tomcat/conf/context.xml`:

```xml
<Resource name="jdbc/bibliotecaDB"
          auth="Container"
          type="javax.sql.DataSource"
          maxTotal="100"
          maxIdle="30"
          maxWaitMillis="10000"
          username="root"
          password="root"
          driverClassName="com.mysql.cj.jdbc.Driver"
          url="jdbc:mysql://localhost:3306/biblioteca_univeritaria"/>
```

---

## 🧪 Pruebas del Sistema

### Pruebas Manuales Recomendadas:

1. **Prueba del Catálogo:**
    - Acceder a la página principal
    - Verificar que se muestran los libros
    - Probar la funcionalidad de búsqueda
    - Verificar indicadores de disponibilidad

2. **Prueba de Solicitud:**
    - Seleccionar un libro disponible
    - Llenar el formulario con datos válidos
    - Verificar validación de email
    - Confirmar que se guarda en BD
    - Intentar solicitar un libro no disponible (debe fallar)

3. **Prueba del Panel Admin:**
    - Acceder a `/admin`
    - Verificar que aparecen las solicitudes
    - Probar filtros por estado
    - Aprobar una solicitud pendiente
    - Rechazar una solicitud
    - Eliminar una solicitud

4. **Prueba de Base de Datos:**
   ```sql
   -- Verificar libros
   SELECT * FROM libros;
   
   -- Verificar solicitudes
   SELECT s.*, l.titulo 
   FROM solicitudes s 
   JOIN libros l ON s.libro_id = l.id;
   ```

---

## 🐛 Solución de Problemas

### Error: Driver MySQL no encontrado

**Síntoma:** `ClassNotFoundException: com.mysql.cj.jdbc.Driver`

**Solución:**
1. Verificar que `mysql-connector-java.jar` está en `WEB-INF/lib`
2. Copiar también a `tomcat/lib`
3. Reiniciar Tomcat

### Error: No se puede conectar a MySQL

**Síntoma:** `SQLException: Access denied for user`

**Solución:**
1. Verificar que MySQL está corriendo:
   ```bash
   # Windows
   net start MySQL80
   
   # Linux
   sudo systemctl start mysql
   ```

2. Verificar credenciales en `DatabaseConnection.java`

3. Crear usuario si es necesario:
   ```sql
   CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
   GRANT ALL PRIVILEGES ON biblioteca_univeritaria.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

### Error: Tablas no se crean automáticamente

**Solución:**
1. Verificar logs de Tomcat
2. Crear manualmente la base de datos
3. Verificar que `AppContextListener` está anotado con `@WebListener`

### Error: Página en blanco o Error 404

**Solución:**
1. Verificar que el contexto está correcto: `/BibliotecaUniversitaria`
2. Revisar `web.xml` para verificar mappings de servlets
3. Verificar que los JSP están en la raíz de `webapp`

### Error: JSTL no funciona

**Síntoma:** `${...}` se muestra como texto literal

**Solución:**
1. Agregar `jstl-1.2.jar` y `standard-1.1.2.jar` a `WEB-INF/lib`
2. Verificar directivas en JSP:
   ```jsp
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   ```

---

## 📚 Caracterización del Entorno J2EE

### ¿Qué es Java EE?

**Java EE (Java Platform, Enterprise Edition)** es una plataforma de desarrollo para aplicaciones empresariales en Java. Proporciona un conjunto de especificaciones que extienden Java SE para desarrollar aplicaciones web, distribuidas y de gran escala.

### Tecnologías J2EE Utilizadas en este Proyecto:

1. **JSP (JavaServer Pages)**
    - Tecnología para crear páginas web dinámicas
    - Permite embeber código Java en HTML
    - Se compila a Servlets en tiempo de ejecución
    - **Rol:** Capa de presentación (Vista en MVC)

2. **Servlets**
    - Clases Java que manejan peticiones HTTP
    - Procesan lógica de negocio
    - Controlan el flujo de la aplicación
    - **Rol:** Controladores en MVC

3. **JDBC (Java Database Connectivity)**
    - API para conectarse a bases de datos
    - Ejecutar consultas SQL
    - Gestionar transacciones
    - **Rol:** Capa de persistencia

4. **JSTL (JSP Standard Tag Library)**
    - Biblioteca de etiquetas para JSP
    - Reduce código Java en vistas
    - Facilita iteraciones, condicionales, formateo
    - **Rol:** Mejora la capa de presentación

5. **DAO (Data Access Object)**
    - Patrón de diseño para separar lógica de acceso a datos
    - Abstrae operaciones CRUD
    - Facilita mantenimiento y pruebas
    - **Rol:** Capa de acceso a datos en el Modelo

### Ventajas de Java EE:

✅ **Escalabilidad:** Diseñado para aplicaciones de gran escala
✅ **Portabilidad:** Funciona en cualquier servidor compatible (Tomcat, GlassFish, WildFly)
✅ **Seguridad:** Mecanismos integrados de autenticación y autorización
✅ **Mantenibilidad:** Separación clara de responsabilidades con MVC
✅ **Comunidad:** Amplio soporte y documentación
✅ **Madurez:** Tecnología probada y estable en producción
✅ **Transacciones:** Gestión automática de transacciones
✅ **Interoperabilidad:** Integración con otros sistemas empresariales

### Comparación con Otras Tecnologías:

| Característica | Java EE | PHP | Node.js | .NET |
|----------------|---------|-----|---------|------|
| Tipado | Fuerte | Débil | Débil | Fuerte |
| Rendimiento | Alto | Medio | Alto | Alto |
| Escalabilidad | Excelente | Bueno | Excelente | Excelente |
| Curva de aprendizaje | Media-Alta | Baja | Media | Media-Alta |
| Ecosistema | Maduro | Maduro | Moderno | Maduro |



---

## 📝 Notas Adicionales

### Datos de Ejemplo Incluidos

El sistema incluye 10 libros de ejemplo al inicializar:
- Cien Años de Soledad (Gabriel García Márquez)
- El Principito (Antoine de Saint-Exupéry)
- Don Quijote de la Mancha (Miguel de Cervantes)
- 1984 (George Orwell) - No disponible
- Crónica de una Muerte Anunciada (Gabriel García Márquez)
- El Aleph (Jorge Luis Borges)
- La Sombra del Viento (Carlos Ruiz Zafón)
- Rayuela (Julio Cortázar) - No disponible
- La Casa de los Espíritus (Isabel Allende)
- Ficciones (Jorge Luis Borges)

### Mejoras Futuras Sugeridas

- [ ] Sistema de autenticación para administradores
- [ ] Envío de emails de confirmación
- [ ] Historial de préstamos por usuario
- [ ] Sistema de devoluciones
- [ ] Reportes en PDF
- [ ] API REST para integración móvil
- [ ] Notificaciones push
- [ ] Sistema de multas por retraso

---

## ✅ Checklist de Entrega

- [x] Implementación del patrón MVC
- [x] Servlets para GET y POST
- [x] JSP con JSTL
- [x] Patrón DAO implementado
- [x] Conexión a base de datos MySQL
- [x] Inicialización automática de BD
- [x] Validación de formularios
- [x] Manejo de errores
- [x] Panel de administración
- [x] Despliegue en Tomcat
- [x] README completo
- [x] Código documentado

---
