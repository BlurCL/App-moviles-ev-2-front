# App-Catálogo Frontend  
**Proyecto de desarrollo móvil (Evaluación Parcial 2**

## Información del equipo  
- **Integrante 1:** Consuelo Jerez  
- **Integrante 2:** Cristóbal Venegas 
- **Repositorio GitHub:** https://github.com/BlurCL/App-moviles-ev-2-front    

## Objetivo del proyecto  
- Desarrollar una aplicación movil que permita visualizar el catalogo de productos de una pastelería, busca usar una persistencia local y recursos nativos.

## Funcionalidades principales
- Visualización de catálogo con listado de productos 
- Vista de inicio de sesión 
- Navegación entre pantallas: Main-Login / Main-Catalogo / Catalogo-Main / Login-Main.    
- Animación de carga e interacción para mejorar la experiencia  
- Gestión del proyecto con GitHub

## Stack tecnológico  
- Lenguaje: Kotlin  
- Framework: Android Studio + Jetpack Compose  
- Arquitectura: MVVM (Model-View-ViewModel)  
- Persistencia: Room / DataStore  
- Red: Retrofit / OkHttp  
- Navegación: Navigation Compose  
- Gestión de estados: LiveData / StateFlow  
- Control de versiones: Git + GitHub  
# Proyecto: Pastelería Mil Sabores 🍰

## Integrantes
* **Consuelo Jerez**
* **Cristóbal Venegas**

## Descripción
Aplicación móvil para la gestión y venta de productos de pastelería. Permite a los clientes ver productos y añadirlos al carrito, y a los administradores gestionar el inventario (CRUD).

## Funcionalidades Implementadas
* **Catálogo de Productos:** Gestión completa (Crear, Leer, Editar, Eliminar) conectado a Backend Spring Boot (H2).
* **Carrito de Compras:** Persistencia local de estado y simulación de compra.
* **API Externa:** Consumo de indicador económico (Dólar) en tiempo real desde *mindicador.cl*.
* **Seguridad:** Perfiles diferenciados para Cliente y Administrador.
* **Información:** Pantalla "Quiénes Somos" con scroll y diseño personalizado.

## Tecnologías
* **Frontend:** Android (Kotlin) + Jetpack Compose.
* **Backend:** Spring Boot (Java 21) + H2 Database.
* **Arquitectura:** MVVM (Model-View-ViewModel).

## Endpoints Utilizados
* **Backend Propio:** `http://10.0.2.2:8080/productos`
    * `GET /productos`: Listar.
    * `POST /productos`: Crear.
    * `PUT /productos/{id}`: Actualizar.
    * `DELETE /productos/{id}`: Eliminar.
* **API Externa:** `https://mindicador.cl/api` (GET)

## Pasos para Ejecutar
1. **Backend:** Abrir carpeta `App-moviles-ev-2-Back`, asegurar Java 21 y ejecutar `PasteleriaApplication`.
2. **Frontend:** Abrir en Android Studio, sincronizar Gradle y ejecutar en Emulador (API 34+).
3. **Login:** Usuario `admin` / Clave `1234` para gestión completa.
