# Meli APC

## Overview
Este repositorio contiene el backend de Meli APC, Asesor Personal de Compras.

> **Disclaimer**: Este es un proyecto universitario con fines educativos. **No está destinado para realizar compras reales** en MercadoLibre.

## Enlaces
- [Repositorio central del frontend](http://github.com/angelodpadron/meli-apc-front)

## Requisitos

### Sin Docker
- **JDK 21**
- **PostgreSQL**
- **Token de acceso** para realizar consultas a los servicios de MercadoLibre.

## Configuración y Ejecución del Proyecto

### Variables de Entorno
Antes de ejecutar el proyecto, asegúrate de configurar las siguientes variables de entorno:

- `MELI_SITE_ID`: ID del sitio de MercadoLibre, determina el pais del que se proveen los resultados de busqueda.
- `MELI_ACCESS_TOKEN`: Token de acceso para la API de MercadoLibre.
- `APP_BASE_URL`: URL base de la aplicación (se utiliza para la configuración de Swagger).
- `APP_SERVER_PORT` (opcional): Puerto en el que se ejecutará el servidor (por defecto, **8080**).



### Compilar y Ejecutar el JAR
1. En el directorio raíz del proyecto, ejecuta el siguiente comando para compilar el proyecto y generar el archivo `.jar`:

   ```bash
   ./mvnw clean package
  
2. Ejecuta el archivo .jar generado con el siguiente comando:

   ```bash
   java -jar target/backend-x.x.x.jar
  
- Donde `x.x.x` es la version actual del proyecto.

### Ejecución con Docker-Compose
En el directorio raíz del proyecto, ejecuta el siguiente comando:

  ```bash
  docker-compose up
  ```

El servicio estará disponible en http://localhost:8080 o en el puerto que hayas configurado con la variable de entorno `APP_SERVER_PORT`.
