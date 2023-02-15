# Rijks-studio
Displays a paginated list of art objects and additional details when clicked.

## Tooling
- Kotlin, coroutines for asynchronous operations and flow for observable data streams
- Koin for dependency injection
- Ktor and kotlinx-serialization for networking 
- JUnit5 for unit testing
- Androidx lifecycle, navigation and paging3 components

## Software design
Presentation-domain-data layering, with MVVM in the presentation layer, split into several gradle modules.

![gradle modules](https://github.com/kyrillosgait/rijks-studio/blob/master/images/modules.png)

- `:app` -> Application class, single activity, test for dependency injection declarations.
- `:feature:collection` -> Collection list and detail fragments along with their corresponding view models.
- `:core:ui` -> Common extensions and reusable views that feature modules can tap into.
- `:core:domain` -> Use cases and models, encapsulating reusable business logic and behavior.
- `:core:data` -> Data models, a repository consuming the api gateway (plus a fake implementation of it), in-memory caching, and unit tests.
- `:core:network` -> The api gateway along with network models, and unit tests.
- `:core:di` -> Gathers domain and data layer DI modules and exposes them to :app.
- `:build-logic` -> Reusable gradle precompiled scripts for the gradle modules.