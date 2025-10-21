# Weather App (Android)

A modern weather application built with **Jetpack Compose** and **MVVM architecture**. The app fetches weather data from the **OpenWeatherMap API**, supports location-based updates, and provides a sleek Material Design 3 UI.

## 🌤️ Features

- ✅ City search with text input
- ✅ Display current temperature & weather conditions
- ✅ Loading & error states with proper handling
- ✅ Clean and responsive **Material Design 3 UI**
- ✅ **Retrofit** with suspend functions for API calls
- ✅ **Current location detection** using FusedLocationProvider
- ✅ **5-day weather forecast** in a LazyColumn
- ✅ **Room database caching** with 1-hour cache validity

## 🧩 Key Components

- **Retrofit** — For making network calls to the OpenWeatherMap API.
- **Room** — For local caching of weather data to improve performance and offline usage.
- **Jetpack Compose** — For a declarative, modern UI experience.
- **Coroutines** — For clean, asynchronous background operations.
- **MVVM Architecture** — Ensures clean separation of concerns using ViewModel and Repository layers.

## ⚙️ Architecture Overview

- **Model** — Data classes, Room entities, and API response models.
- **ViewModel** — Manages UI-related data and handles business logic.
- **Repository** — Mediates between the network (Retrofit) and local database (Room).
- **UI Layer** — Composables that render weather data and handle user interactions.

## 🚀 Getting Started

1. Clone this repository.
2. Open the project in **Android Studio**.
3. Add your **OpenWeatherMap API key** in the constants file.
4. Run the project on an Android device or emulator.

## 🛠️ Tech Stack

- Kotlin
- Jetpack Compose
- Retrofit2
- Room
- Coroutines
- Material Design 3

---



