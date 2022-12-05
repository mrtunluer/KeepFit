# KeepFit

KeppFit is a weight tracking app, helps you to reach your target weight. You can also track your body measurements.

## ScreenShots

![Apple iPhone 11 Pro Max Presentation-min](https://user-images.githubusercontent.com/75806927/205656486-ed1cbe92-90e7-4dd6-b363-97dd986544de.png)

## Architecture

The application adopts MVVM architecture, consists of 1 root activity and several fragments. It is used for navigation, data transfer and switching between fragments.

## Libraries And Tech Stack

- <a href="https://developer.android.com/topic/architecture">MVVM Architecture</a> - Model-View-ViewModel (MVVM) is a software design pattern that is structured to separate program logic and user interface controls.
- <a href="https://developer.android.com/guide/navigation">Navigation</a> - Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app.
- <a href="https://developer.android.com/training/dependency-injection/hilt-android">Hilt</a> - Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
- <a href="https://developer.android.com/topic/libraries/architecture/datastore">DataStore</a> - Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
- <a href="https://developer.android.com/training/data-storage/room">Room</a> - The Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.
- <a href="https://kotlinlang.org/docs/coroutines-overview.html">Coroutines</a> - A coroutine is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
- <a href="https://developer.android.com/kotlin/flow">Flow</a> - In coroutines, a flow is a type that can emit multiple values sequentially, as opposed to suspend functions that return only a single value.