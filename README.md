Currency Exchange App
=====================

This is an Android app for currency exchange. It allows users to view exchange rates for different currencies and convert amounts between them.

Code Architecture
-----------------

This app uses the `Model-View-ViewModel (MVVM)` architecture. In this architecture, the `MainActivity` serves as the View and is responsible for displaying data to the user and handling user interaction. The `ViewModel` manages the data for the View. The `Repository` is responsible for retrieving and processing data from the API and passing it to the ViewModel. I uses `Retrofit` and `OkHttpClient` to make API requests and `Moshi` to convert the JSON response to Kotlin data classes. I choose to use the MVVM architecture for this app as it provides clear separation between the user interface, business logic, and data model. This separation makes it easier to maintain and test the app, and also enhances its scalability.

Further Improvements
--------------------

While the app is functional, there are several areas where it could be improved. Specifically:

1.  Dependency Injection: Currently I am using `ViewModelFactory` to manually create ViewModels and pass in the repository instance, but it can be improved by using a more robust dependency injection framework such as `Koin`.
2.  Currency List: The currency list is displayed using a Spinner, it can be improved by using a `RecyclerView` with search by keyword or alphabet scrolling to make it easier for users to find the currency they want to convert.
3.  Test Cases: The current test cases are basic and incomplete. it can be improved by adding more comprehensive test cases to cover all parts of the app and ensure that it is working as expected.
4.  Enhance the app by adding new features such as a favorites list, a graph displaying the currency's rate history, and an alert for pricing changes.

Feedback
--------

This assignment provided a valuable learning experience for me. It allowed me to apply my knowledge of Android app development and MVVM architecture, while gaining hands-on experience in building a fully functional currency exchange app from scratch. It also helped identify areas for improvement in my skills and knowledge, and I look forward to applying these lessons in future projects.