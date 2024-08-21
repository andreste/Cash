# Cash App

# Architectural approach
* This project is using an MVVM architecture.
* It contains a ViewModel, a Repository, a Service and Data models.
* The UI was made using Jetpack Compose.

# Tradeoffs you made and why
* The UI is not looking the best considering the time constraints. 
* I decided to spend some time setting up the navigation with Androidx Navigation. This exercise only
has one screen, but it's good to start with a good base to start building new screens on. So,
if during the next interview I am asked to add another screen, the app is going to be able to handle
that very easily.


# How to run the project
* This app was built using Android Studio Koala 2024.1.1 Patch 2, so you would need a relatively recent 
version of Android Studio.
* Once you download the project, you can import it into Android Studio and you should only need to 
run the project directly.

# 3rd party libraries
* I used Hilt for DI, Retrofit and OKhttp for the network call, Mockk for testing and kotlinx serialization
for serializing the json response.


# Any other information
* It took me the full 3 hours to work on this because I felt like creating a solid foundation to keep
building on. This project is using MVVM, a good files organization, Hilt for DI, Retrofit and Coroutines 
for the network call and Jetpack Compose. I believe this is a good foundation for a solid new Android app.
