# Smart Security

This project was created to demonstate sms based security system monitoring.

## Features

This project includes the following features:

- Receive intrusion sms notification and sound of alarm on mobile device
- Check system status
- Activate panic alarms
- Local data storage using shared preference
- Event handling of incoming sms using event bus

## Getting Started

To get started with the project, follow the instructions below:

1. Clone this repository to your local machine using Git or download the ZIP file.
2. Open Android Studio and select "Open an existing Android Studio project."
3. Navigate to the cloned or downloaded project directory and click "OK."
4. Android Studio will build the project and download any necessary dependencies.
5. Connect an Android device or use an emulator to run the application.

## Configuration

The example project requires the following configuration:

- Minimum SDK version: 14
- Target SDK version: 23

You may need to update these values in the `build.gradle` file based on your project requirements.

## Usage

Smart Security project demonstrates a simple sms based application that retrieves sms from from an embedded control unit and updates the UI on the screen to reflect the state of the security system. It also sends sms to control the state of the security system. You can use it as a reference to understand how to implement similar functionality in your own projects.

The application consists of multiple fragments, each showcasing a different Android component or feature. Use the navigation drawer to switch between fragments and explore the different features.

If you're looking to integrate with a smartsecuity hardware project, make sure to check out the the repository [corresponding ariduino firmware](https://github.com/cgardesey/Arduino-Projects/tree/6081e6e4ac8a747e5fff8c71dcf9dd85fc328a83/Smart_Security) for detailed instructions.

## License

Smart Security project is licensed under the [MIT License](LICENSE). Feel free to use it as a reference or starting point for your own projects.
