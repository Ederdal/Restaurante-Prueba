# Restaurante API

![Grails](https://img.shields.io/badge/Grails-3.x-4CAF50?logo=grails&logoColor=white)
![Groovy](https://img.shields.io/badge/Groovy-2.4-blue?logo=apachegroovy&logoColor=white)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?logo=mysql&logoColor=white)
![RESTful](https://img.shields.io/badge/API-RESTful-FF9800)

Sistema backend desarrollado en **Grails + Groovy** con base de datos **MySQL**, que permite administrar menús y platillos de un restaurante mediante una API **RESTful** moderna, segura y estructurada.


## Datos del Autor

| **Campo** | **Información** |
|------------|-----------------|
| **Nombre del Proyecto** | Restaurante API  |
| **Carrera** | Ingeniería en Desarrollo y Gestión de Software |
| **Grado y Grupo** | 10° A |
| **Nombre del Autor** | Edgar Cruz Salas|
| **Rol en el Proyecto** | Desarrollador Backend |

## Descripción General
La **Restaurante API** permite:
- Crear y gestionar distintos **tipos de menú** (Desayunos, Comidas, Cenas, etc.).
- Registrar, editar y eliminar **platillos** asociados a cada tipo.
- Activar, desactivar o eliminar platillos o menús.
- Consultar información y listar datos con **paginación y filtros**.


## Tecnologías Utilizadas

| Tipo | Tecnología |
|------|-------------|
| Framework Backend | [Grails 3.x](https://grails.org) |
| Lenguaje | Groovy |
| Base de Datos | MySQL 8 |
| Estilo API | RESTful (JSON / XML) |


## Estructura del Proyecto

RESTAURANTE/<br>
│
├── grails-app/<br>
│ ├── conf/<br>
│ │ ├── application.yml<br>
│ │ ├── logback.groovy<br>
│ │ └── restaurante/<br>
│ │ └── UrlMappings.groovy<br>
│ │
│ ├── controllers/<br>
│ │ ├── com/ordenaris/restaurante/<br>
│ │ │ ├── MenuController.groovy<br>
│ │ │ └── PlatilloController.groovy<br>
│ │ └── restaurante/<br>
│ │ └── ApplicationController.groovy<br>
│ │
│ ├── domain/com/ordenaris/restaurante/<br>
│ │ ├── Platillo.groovy<br>
│ │ └── TipoMenu.groovy<br>
│ │
│ ├── services/com/ordenaris/restaurante/<br>
│ │ ├── MenuService.groovy<br>
│ │ └── PlatilloService.groovy<br>
│ │
│ ├── init/restaurante/<br>
│ │ ├── Application.groovy<br>
│ │ └── BootStrap.groovy<br>
│ │
│ └── views/ <br>
│
├── src/<br>
│ ├── integration-test/resources/<br>
│ │ └── GebConfig.groovy<br>
│ └── test/groovy/com/ordenaris/restaurante/<br>
│ ├── MenuControllerSpec.groovy<br>
│ ├── MenuServiceSpec.groovy<br>
│ ├── PlatilloControllerSpec.groovy<br>
│ ├── PlatilloServiceSpec.groovy<br>
│ ├── PlatilloSpec.groovy<br>
│ └── TipoMenuSpec.groovy<br>
│
├── .gitignore <br>
├── build.gradle<br>
└── README.md<br>