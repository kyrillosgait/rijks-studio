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

- `:app` -> Application class, single activity, test for dependency injection declarations.
- `:feature:collection` -> Collection list and detail fragments along with their corresponding view models.
- `:core:ui` -> Common extensions and reusable views that feature modules can tap into.
- `:core:domain` -> Core models of the app, the repository interface, and use cases encapsulating reusable business logic, unit tested.
- `:core:data` -> Repository implementation consuming the API gateway, unit tested.
- `:core:network` -> API gateway implementation along with the network models, unit tested.
- `:core:di` -> Glues domain and data layer modules. Provides them to `:app`.
- `:build-logic` -> Reusable gradle precompiled script plugins.

## Tooling
- Kotlin, coroutines for asynchronous operations and flow for observable data streams
- Koin for dependency injection
- Ktor and kotlinx-serialization for networking
- JUnit5 for unit testing
- Androidx lifecycle and navigation components