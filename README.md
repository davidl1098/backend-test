\# TATA – Backend Test (Clientes \& Cuentas)



Microservicios en \*\*Java 21 / Spring Boot 3\*\*, \*\*PostgreSQL\*\*, \*\*RabbitMQ\*\*, \*\*Flyway\*\*, \*\*Docker\*\*.  

Incluye \*\*colección Postman\*\* y script \*\*BaseDatos.sql\*\*.



---



\## Estructura



clientes-service/

cuentas-service/

docker-compose.yml

BaseDatos.sql

TATA-BackendTest.postman\\\_collection.json



---



\## Requisitos



\- Java 21  

\- Maven 3.9+  

\- Docker Desktop  

\- Postman (opcional)



---



\## Ejecución con Docker Compose



La solución completa se levanta con el comando "docker compose up --build"



---





\## Servicios disponibles:



\* \*\*Postgres\*\* → `localhost:5432` (DB `test`, user/pass `test`)

\* \*\*RabbitMQ\*\* → `http://localhost:15672`

\* \*\*clientes-service\*\* → `http://localhost:8081`

\* \*\*cuentas-service\*\* → `http://localhost:8082`



---



\## Endpoints



\### clientes-service



\* `POST /api/clientes`

\* `GET /api/clientes/{id}`

\* `PUT /api/clientes/{id}`

\* `DELETE /api/clientes/{id}`



\### cuentas-service



\* `POST /api/cuentas`

\* `GET /api/cuentas/{id}`

\* `PUT /api/cuentas/{id}`

\* `DELETE /api/cuentas/{id}`

\* `POST /api/movimientos` (valor positivo = crédito, negativo = débito, sin sobregiro)

\* `GET /api/reportes?clienteId=CLI-100\&desde=YYYY-MM-DD\&hasta=YYYY-MM-DD`



---



\## Postman



Importa "TATA-BackendTest.postman\_collection.json"



---



\## Script SQL



"BaseDatos.sql" crea los esquemas \*\*clientes\*\* y \*\*cuentas\*\* con datos de ejemplo.

Se usa como referencia, aunque en runtime Flyway aplica las migraciones automáticamente.





