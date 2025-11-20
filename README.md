# ProyectBeer --- Sistema de Gestión de Cervezas, Usuarios y Ventas

Aplicación móvil desarrollada en **Kotlin + Android Studio**, diseñada
para administrar el inventario y las ventas de una distribuidora de
cervezas. Incluye un sistema de login, manejo de usuarios, registro de
cervezas y control completo de ventas.

## Descripción del Proyecto

ProyectBeer es una aplicación que simula el funcionamiento interno de
una distribuidora de cervezas. El sistema permite a los usuarios
registrados acceder a un panel de gestión donde pueden:

-   Registrar, editar, actualizar y eliminar cervezas.
-   Registrar ventas asociadas a productos del inventario.
-   Ver el historial completo de ventas.
-   Administrar usuarios (CRUD).
-   Controlar el stock disponible.

## Funciones Principales

### Gestión de Usuarios (CRUD)

**Atributos del usuario** - idUsuario - nombre  - contraseña 

### Gestión de Cervezas (CRUD)

**Atributos de la cerveza** - idCerveza - marca - tipo - ABV - IBU -
proveedor - precioUnitario - cantidad

### Gestión de Ventas (CRUD)

**Atributos de la venta** - idVenta - idCerveza - cantidadVendida -
precioTotal - fechaVenta - vendedor - cliente

## Sistema de Login

Incluye validación de usuario y contraseña.

## Tecnologías Utilizadas

-   Kotlin
-   Android Studio
-   XML
-   SQLite
-   Git/GitHub

## Instalación

    git clone https://github.com/Oswaldo-Sierra/ProyectBeer-AppMovil.git

## Autor

Oswaldo Sierra
