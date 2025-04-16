# Vinilos App

Aplicación móvil para amantes de los vinilos que permite explorar álbumes, artistas y coleccionistas.

## Requisitos Previos

- Android Studio Flamingo (2023.2.1) o superior
- JDK 17 o superior
- Android SDK (mínimo API 24 - Android 7.0)
- Un dispositivo Android o emulador para pruebas

## Configuración del Entorno

1. Clonar el repositorio:

   ```
   git clone https://github.com/tu-usuario/vinilos-app.git
   cd vinilos-app
   ```

2. Abrir el proyecto en Android Studio:

   - Inicia Android Studio
   - Selecciona "Open an existing project"
   - Navega y selecciona la carpeta del proyecto

3. Sincronizar Gradle:
   - Android Studio debería sincronizar automáticamente
   - Si no lo hace, haz clic en "Sync Project with Gradle Files"

## Compilar y Ejecutar

### Desde Android Studio

1. Selecciona un dispositivo o emulador desde el menú desplegable
2. Presiona el botón "Run" (triángulo verde)

### Desde la línea de comandos

```bash
# Para compilar la aplicación
./gradlew build

# Para instalar y ejecutar la versión de depuración
./gradlew installDebug
```

## Generar APK para Instalación

### APK de Depuración

```bash
./gradlew assembleDebug
```

El archivo APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### APK de Lanzamiento (Firmado)

1. Configura la firma en `app/build.gradle.kts`:

   - Asegúrate de tener configurado el bloque `signingConfigs`
   - Verifica que el bloque `buildTypes` esté configurado para usar la firma

2. Genera el APK de lanzamiento:
   ```bash
   ./gradlew assembleRelease
   ```

El archivo APK firmado estará en: `app/build/outputs/apk/release/app-release.apk`

## Estructura del Proyecto

- `app/src/main/java/` - Código fuente Kotlin
- `app/src/main/res/` - Recursos (layouts, imágenes, strings, etc.)
- `app/src/androidTest/` - Pruebas instrumentadas
- `app/src/test/` - Pruebas unitarias

## Solución de Problemas

- **Error de Gradle**: Intenta "File > Invalidate Caches / Restart"
- **Problemas de dependencias**: Verifica la conectividad a internet y actualiza Gradle
- **Errores de compilación**: Asegúrate de tener el JDK correcto configurado en Android Studio

## Contribuciones

Para contribuir al proyecto:

1. Crea una nueva rama para tu función (`git checkout -b feature/nueva-funcion`)
2. Realiza tus cambios y haz commit (`git commit -m 'Añade nueva función'`)
3. Sube los cambios al repositorio remoto (`git push origin feature/nueva-funcion`)
4. Abre un Pull Request
