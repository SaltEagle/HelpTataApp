# HelpTata App 📱

Aplicación móvil oficial de **HelpTata**, desarrollada para complementar la plataforma web y facilitar el acceso de los usuarios desde dispositivos Android.

La aplicación permite registrarse, iniciar sesión y acceder a la plataforma HelpTata de forma sencilla e intuitiva.

---

# Características

- Inicio de sesión.
- Registro de nuevos usuarios.
- Validación de datos.
- Manejo de sesiones.
- Integración con API REST mediante Retrofit.
- Acceso a la plataforma web mediante WebView.
- Interfaz moderna desarrollada con Jetpack Compose.

---

# Tecnologías

| Tecnología | Uso |
|------------|-----|
| Kotlin | Lenguaje principal |
| Jetpack Compose | Interfaz de usuario |
| Navigation Compose | Navegación |
| Retrofit | Consumo de API REST |
| ViewModel | Gestión de estados |
| Material 3 | Componentes visuales |
| Android Studio | IDE |

---

# Arquitectura

El proyecto sigue una arquitectura por capas.

```
UI (Compose Screens)
        │
        ▼
ViewModel
        │
        ▼
Repository
        │
        ▼
Retrofit
        │
        ▼
API HelpTata
```

---

# Estructura del proyecto

```
app
│
├── model
│      Modelos utilizados por la API
│
├── navegation
│      Pantallas de la aplicación
│
├── remote
│      Retrofit
│      ApiService
│
├── repository
│      Comunicación con la API
│
├── session
│      Manejo del token y sesión
│
├── ui
│      Componentes reutilizables
│
├── util
│      Funciones auxiliares
│
└── viewmodel
       Lógica de negocio
```

---

# Pantallas

Actualmente la aplicación cuenta con:

- Welcome Screen
- Login
- Registro
- Nombre y Apellido
- Correo
- Fecha de nacimiento
- Teléfono
- Contraseña
- WebApp (acceso a la plataforma)

---

# Instalación

## Clonar repositorio

```bash
git clone https://github.com/SaltEagle/HelpTataApp.git
```

---

## Abrir Android Studio

Seleccionar

```
Open Project
```

y abrir la carpeta del proyecto.

---

## Sincronizar Gradle

Android Studio descargará automáticamente todas las dependencias.

---

## Ejecutar

Seleccionar un emulador o dispositivo físico y presionar

```
Run ▶
```

---

# Dependencias principales

- androidx.compose
- androidx.navigation.compose
- retrofit2
- gson
- lifecycle-viewmodel
- material3

---

# Flujo de la aplicación

```
Inicio

↓

Pantalla Bienvenida

↓

Login

↓

API HelpTata

↓

Token

↓

WebView

↓

Plataforma HelpTata
```

---

# Funcionalidades

## Registro

Permite crear una cuenta nueva enviando la información al servidor mediante Retrofit.

---

## Inicio de sesión

Autenticación del usuario utilizando la API del proyecto.

---

## Manejo de sesión

Una vez autenticado, el token es almacenado para mantener la sesión activa.

---

## Acceso a la plataforma

Después del inicio de sesión exitoso, el usuario es redirigido a la plataforma web de HelpTata mediante una WebView integrada.

---

# Requisitos

- Android Studio Koala o superior
- JDK 17
- Android SDK 35
- Gradle

---

# Autor

Proyecto desarrollado por el equipo **HelpTata** como parte del proyecto de título.

---

# Licencia

Proyecto desarrollado con fines académicos.
