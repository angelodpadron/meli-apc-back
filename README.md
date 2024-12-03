# Meli APC

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=angelodpadron_meli-apc-back&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=angelodpadron_meli-apc-back)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=angelodpadron_meli-apc-back&metric=coverage)](https://sonarcloud.io/summary/new_code?id=angelodpadron_meli-apc-back)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=angelodpadron_meli-apc-back&metric=bugs)](https://sonarcloud.io/summary/new_code?id=angelodpadron_meli-apc-back)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=angelodpadron_meli-apc-back&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=angelodpadron_meli-apc-back)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=angelodpadron_meli-apc-back&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=angelodpadron_meli-apc-back)

## 📖 Overview
Este repositorio contiene el backend de Meli APC, Asesor Personal de Compras. Proyecto de la materia Practicas de Desarrollo de Software (UNQ).

## 🌐 Enlaces Útiles
- 🔗 [Repositorio del Frontend de Meli APC](https://github.com/angelodpadron/meli-apc-front)
- 🔗 [Code Analysis de SonarCloud](https://sonarcloud.io/project/overview?id=angelodpadron_meli-apc-back)

---

## 🛠 Tecnologías Utilizadas
- **Lenguaje**: Kotlin 1.9.24
- **Framework**: Spring Boot 3.3.2
- **Base de Datos**: PostgreSQL
- **Monitorización**: Prometheus + Grafana
- **Contenerización**: Docker

## 📋 Requisitos

### Sin Docker
1. **JDK 21**
2. **PostgreSQL**
3. **Token de acceso** para consumir la API de MercadoLibre.

### Con Docker
1. **Docker Compose**
2. **Token de acceso** para consumir la API de MercadoLibre.

## ⚙️ Configuración y Ejecución

### Variables de Entorno
Configurar las siguientes variables de entorno antes de ejecutar el proyecto:

| Variable            | Descripción                                                                     |
|---------------------|---------------------------------------------------------------------------------|
| `MELI_SITE_ID`      | ID del sitio de MercadoLibre (determina el país de los resultados de búsqueda). |
| `MELI_ACCESS_TOKEN` | Token de acceso para la API de MercadoLibre.                                    |
| `APP_BASE_URL`      | URL base de la aplicación (usada en la documentación de Swagger).               |
| `DB_NAME`           | Nombre de la base de datos.                                                     |
| `DB_USER`           | Usuario de la base de datos.                                                    |
| `DB_PASSWORD`       | Contraseña del usuario de la base de datos.                                     |

### Ejecución sin Docker
1. **Compila el proyecto**:  
   Ejecutar el siguiente comando en el directorio raíz para generar el archivo `.jar`:
   ```bash
   ./mvnw clean package
  
2. **Ejecutar el proyecto**:  
   Iniciar la aplicacion con el comando:

   ```bash
   java -jar target/backend-x.x.x.jar
  
- Donde `x.x.x` es la version actual del proyecto.

3. El servicio estara disponible en http://localhost:8080

### Ejecución con Docker-Compose
1. En el directorio raíz del proyecto, ejecutar el siguiente comando:

  ```bash
  docker-compose up
  ```

2. Los siguientes servicios estaran disponibles:
   - Backend: http://localhost:8080
   - Frontend: http://localhost (puerto 80)
   - Prometheus: http://localhost:9090
   - Grafana: http://localhost:3000

## 🧪 Tests
Es posible ejecutar los tests del proyecto utilizando el siguiente comando:
```bash
./mvnw test
  ```

## 🛡 Disclaimer
Este proyecto es didáctico y **no está destinado para realizar compras reales** en MercadoLibre, ni diseñado para entornos de producción.
