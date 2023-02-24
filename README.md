# Rijks-studio
[![Build](https://github.com/kyrillosgait/rijks-studio/actions/workflows/build.yaml/badge.svg)](https://github.com/kyrillosgait/rijks-studio/actions/workflows/build.yaml) [![Release](https://github.com/kyrillosgait/rijks-studio/actions/workflows/release.yaml/badge.svg?branch=develop)](https://github.com/kyrillosgait/rijks-studio/actions/workflows/release.yaml)

Displays a paginated list of art objects and additional details when clicked.

![list detail screens](/images/list-detail-screens.png)

## Using the app

To see actual data from API you'll need an [API key](https://data.rijksmuseum.nl/object-metadata/api/), which can be defined in `local.properties` like:
```
API_KEY="your_api_key"
```

Alternatively, you can configure the app via DI to use the `FakeRijksGateway` as a data source, instead of the default implementation.

## Layering and modularization
Presentation-domain-data layering, with MVVM in the presentation layer, split into several gradle modules.

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