# Xplore NU 🚀



> A self-guided tour companion app for Northeastern University’s Boston campus.

[![Deployment workflow](https://github.com/Ruthvikbr/Xplore-NU/actions/workflows/internal_testing.yml/badge.svg?branch=qa)](https://github.com/Ruthvikbr/Xplore-NU/actions/workflows/internal_testing.yml)
---

## Overview 📕

Xplore NU is an Android application that enhances self-paced campus exploration with interactive tours

## Tour experience
<div align="center">
  <video src="https://github.com/user-attachments/assets/99e1433d-66db-4ff3-8da2-4ad8ae36d131"></video>
</div>

## Purpose 💬

Xplore NU is a self-guided tour companion app that allows users to:

- Navigate through Northeastern’s campus using GPS-enabled maps.
- Learn about Points of Interest (POIs) with rich media.
- View upcoming university events.
- Interact with the AMA (Ask Me Anything) chatbot for real-time information.

## Target Users 🧍🏻🧍🏻‍♀️

- **Visitors** exploring campus independently  
- **Prospective students** during open houses  
- **Current students** for event details and campus info  

---

## Key Features ✅

1. **Self-Guided POI Tour**  
2. **Upcoming Campus Events**  
3. **AMA Chatbot**  
4. **Secure Authentication**

---
## Stack

| Tools | Link | Version |
|     :---:      |   :---: | :---:|
| Kotlin | [Kotlin](https://kotlinlang.org) | <img src="https://img.shields.io/badge/Kotlin-2.0.21-blue" /> |
| Jetpack Compose | [Jetpack Compose](https://developer.android.com/jetpack/compose) | <img src="https://img.shields.io/badge/Jetpack%20Compose-2024.04.01-brightgreen" /> |
| Dagger Hilt | [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) | <img src="https://img.shields.io/badge/Dagger%20Hilt-2.51.1-red" /> |
| Coroutines | [Coroutines](https://kotlinlang.org/docs/coroutines-guide.html) | <img src="https://img.shields.io/badge/Coroutines%20-1.8.0-yellow" /> |
| Architecture Components | [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) |  |

## Screenshots

<table style="width:100%">
  <tr>
   <td><img src = "https://github.com/user-attachments/assets/5e00413d-36fa-4baf-9411-58fe0da9d039" width=240 height=500></td>

   <td><img src = "https://github.com/user-attachments/assets/8c3e1bc2-995c-4849-997b-79702d30c8d0" width=240 height=500></td>
    <td><img src = "https://github.com/user-attachments/assets/b68c47b2-1b34-4d20-99eb-2b4003eba23e" width=240 height=500></td>
  </tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/8cd49418-80ee-4176-9feb-d2f367f6e981" width=240 height=500></td>
        <td><img src = "https://github.com/user-attachments/assets/481f564a-da32-4be8-bde2-1ff9a4f3f1ac" width=240 height=500></td>

   <td><img src = "https://github.com/user-attachments/assets/03f7e918-9ec9-406a-b59b-4f1c69d756a9" width=240 height=500></td>
  </tr>
  <tr>
    <td><img src = "https://github.com/user-attachments/assets/8258ac3e-ad02-4479-9beb-dbfe03fc5988" width=240 height=500></td>
  </tr>
</table>
---

### CI Pipeline Configuration Highlights ⚙️

- **Structure**:  
  Each workflow file (`.github/workflows/*.yml`) has:
    1. A **name** (e.g., “Unit Testing workflow”).
    2. **Triggers** (e.g., `on: pull_request: branches: [ dev ]`).
    3. **Jobs** that run on `ubuntu-latest` with steps:
        - **Checkout** your repo
        - **Set up JDK** (actions/setup-java@v4)
        - **Create `local.properties`** from Secrets
        - **Cache Gradle** dependencies (actions/cache@v3)
        - **Run Gradle tasks** (`test`, `bundleRelease`, signing, deployment, etc.)

- **Actions**:
    - [actions/checkout@v4](https://github.com/actions/checkout)
    - [actions/setup-java@v4](https://github.com/actions/setup-java)
    - [actions/cache@v3](https://github.com/actions/cache)
    - [r0adkll/sign-android-release@v1](https://github.com/r0adkll/sign-android-release)
    - [r0adkll/upload-google-play@v1.1.3](https://github.com/r0adkll/upload-google-play)

- **Outputs**:
    - **Build logs** and **test results** in the GitHub Actions UI.
    - A new **signed `.aab`** uploaded to Google Play’s internal track if on `qa`.

- **Containers/Environments**:
    - Runs in an ephemeral Ubuntu container provided by GitHub (`ubuntu-latest`).
    - Java 17 is installed via `actions/setup-java`.

## 5. Conclusion

By combining these two GitHub Actions workflows, we have a streamlined CI/CD process:

- **Every Pull Request to `dev`**:
    - Runs unit tests to ensure code quality.
    - **No deployment**; meant for code validation.

- **Every Push to `qa`**:
    - Runs the same unit tests.
    - On success, increments the version, builds a release bundle, signs it, and uploads to the Google Play **internal testing** track for QA.
