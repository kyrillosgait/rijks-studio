# Rijks-studio
[![Build](https://github.com/kyrillosgait/rijks-studio/actions/workflows/build.yaml/badge.svg)](https://github.com/kyrillosgait/rijks-studio/actions/workflows/build.yaml) [![Release](https://github.com/kyrillosgait/rijks-studio/actions/workflows/release.yaml/badge.svg?branch=develop)](https://github.com/kyrillosgait/rijks-studio/actions/workflows/release.yaml)

Displays an infinite list of art objects and shows additional details when clicked. Supports searching.

![list detail screens](/images/list-detail-screens.png)

## Using the app

To see actual data from API you'll need an [API key](https://data.rijksmuseum.nl/object-metadata/api/), which can be defined in `local.properties` like:
```
API_KEY="your_api_key"
```

> **Note:** By swapping the default API gateway implementation with `FakeRijksGateway` using dependency injection, you can use in-memory data instead of querying the actual API. This way you won't need to get an API key.

## Layering and modularization
MVVM with presentation-domain-data layering. Organized into multiple gradle modules for more flexibility and better separation of concerns.

![gradle modules](/images/modules.png)

- `:app`
  - Application class
  - MainActivity
  - Unit test for dependency injection declarations
  - Test Application class for Instrumentation tests
- `:feature:collection`
  - Collection list Fragment, Adapter, and ViewModel
  - Collection detail Fragment and ViewModel
  - Unit tests for the ViewModels
- `:core:ui` - Common extensions and a reusable view, which feature modules can tap into
- `:core:domain`
  - Domain entities and value objects
  - Repository interface
  - Use cases
  - Unit tests for the use cases, along with fake implementations of the domain entities and the repository to enable testing the module independently
- `:core:data`
  - API gateway interface
  - Default implementation of the repository
  - Unit tests for the repository, along with a fake implementation of the gateway to enable testing the module independently
- `:core:network`
  - API models
  - Default implementation of the API gateway
  - Unit tests for the api gateway
- `:core:di` - Glues domain and data layer modules
- `:build-logic` - Reusable gradle precompiled script plugins

## Tooling
- Kotlin, coroutines for asynchronous operations and flow for observable data streams
- Koin for dependency injection
- Ktor and kotlinx-serialization for networking
- JUnit5 for unit testing
- Androidx lifecycle and navigation components