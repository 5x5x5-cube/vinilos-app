# VinilosApp

VinilosApp es una aplicación Android desarrollada con Jetpack Compose. Permite explorar álbumes musicales.

---

## Requisitos previos

- [Android Studio](https://developer.android.com/studio) 
- JDK 17 o compatible
- Gradle (incluido en Android Studio)
- Un dispositivo físico o emulador Android

---

## Ejecutar la aplicación

1. **Clona el repositorio:**

   ```bash
   git clone https://github.com/5x5x5-cube/vinilos-app.git
   cd vinilos-app

## Generar APK

- Ejecutar ./gradlew assembleDebug
- El apk se encontrara en app/build/outputs/apk/debug/app-debug.apk

## Ejecutar pruebas

- Ejecutar comando ./gradlew connectedAndroidTest
- Los resultados los veremos en app/build/reports/androidTests/connected/index.html
