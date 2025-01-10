Here is a detailed documentation on how to build and run the Inventory-Tracker app:

## Building and Running the Inventory-Tracker App

### Prerequisites
- [Android Studio Canary](https://developer.android.com/studio/preview) installed on your machine.
- JDK 17 or higher.
- Internet connection for downloading dependencies.

### Steps to Build and Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/kaushik8946/Inventory-Tracker.git
   cd Inventory-Tracker
   ```

2. **Open in Android Studio**
   - Open Android Studio.
   - Select `Open an existing project`.
   - Navigate to the directory where you cloned the repository and select it.

3. **Sync Project with Gradle Files**
   - Once the project is opened, Android Studio will prompt you to sync the project with Gradle files. Click on `Sync Now`.

4. **Build the Project**
   - After the Gradle sync is complete, go to `Build` in the top menu and select `Make Project` (or alternatively, press `Ctrl+F9`).

5. **Run the App**
   - Connect an Android device via USB, or set up an Android Virtual Device (AVD) using the AVD Manager in Android Studio.
   - Click on the `Run` button (green play button) in the toolbar, or go to `Run > Run 'app'`.

### Project Structure
- **App-level build file (`app/build.gradle.kts`)**: Contains specific configurations for the Android application module.
- **Top-level build file (`build.gradle.kts`)**: Contains configuration options common to all sub-projects/modules.

### Dependencies
The project uses several dependencies including:
- AndroidX libraries for core functionalities.
- Retrofit for network operations.
- Gson for JSON parsing.
- Kotlin Coroutines for asynchronous programming.
- Room for database operations.
- Coil for image loading.
- Navigation Component for navigation within the app.

For the complete list of dependencies, refer to the `build.gradle.kts` files in the project.

You can find the detailed build files here:
- [Top-level build file](https://github.com/kaushik8946/Inventory-Tracker/blob/main/build.gradle.kts)
- [App-level build file](https://github.com/kaushik8946/Inventory-Tracker/blob/main/app/build.gradle.kts)

This documentation should help you build and run the Inventory-Tracker app. If you encounter any issues, please refer to the [Android Developer Documentation](https://developer.android.com/docs) for further assistance.