# B2B Marketplace Android

> A focused Android reference implementation for operational B2B order workflows.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=flat-square&logo=kotlin&logoColor=white) ![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.12-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white) ![Status](https://img.shields.io/badge/status-reference%20build-17212B?style=flat-square)

## Product preview

<table><tr><td><img src="docs/screenshots/orders-overview.svg" alt="Orders overview" width="220"></td><td><img src="docs/screenshots/order-details.svg" alt="Order details" width="220"></td><td><img src="docs/screenshots/ready-filter.svg" alt="Ready orders filter" width="220"></td></tr></table>

## What is implemented

- Orders dashboard with loading, empty, and error states
- Search by order ID or customer name
- Status filters for New, Processing, Ready, and Delivered
- Priority order highlighting and deterministic demo data
- Repository boundary that can be swapped from the demo source to a real backend
- Pull-to-refresh-ready ViewModel flow through a dedicated refresh action

## Architecture

```text
MainActivity (Compose UI)
        ↓ collects
OrdersViewModel (StateFlow + user intents)
        ↓ coordinates
OrdersRepository (filtering + Result handling)
        ↓ depends on
ApiService / DemoApiService (replaceable data source)
```

### Key files

| File | Responsibility |
| --- | --- |
| `MainActivity.kt` | Compose screen, search, filters, order cards, loading and empty states |
| `OrdersViewModel.kt` | UI state, refresh, search and status intents |
| `OrdersRepository.kt` | Data loading, priority sorting, and filtering rules |
| `ApiService.kt` | API contract and local deterministic data source |
| `Order.kt` | Domain model, status enum, and currency formatting |

## Build configuration

- Android Gradle Plugin: `8.5.2`
- Kotlin: `2.0.21`
- Compile / target SDK: `35`
- Minimum SDK: `26`
- UI: Jetpack Compose + Material 3
- Async state: Kotlin Coroutines + StateFlow

## Run locally

1. Open the repository in Android Studio Ladybug or newer.
2. Let Gradle sync using the Android and Maven Central repositories.
3. Run the `app` configuration on an API 26+ emulator or device.

## Scope

This repository is intentionally small and reviewable: it demonstrates a complete vertical slice of an order workflow without pretending to be a full production backend. The `DemoApiService` keeps the sample runnable offline; a network implementation can be introduced behind the same `ApiService` interface.

## Related work

[Portfolio overview](https://github.com/credithelper094-dotcom/freelance-portfolio) · [GitHub profile](https://github.com/credithelper094-dotcom)