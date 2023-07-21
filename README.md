# Smart Security

This project was created to demonstate sms based security system monitoring.

## Features

This project includes the following features:

- Receive intrusion sms notification and sound of alarm on mobile device
- Check status of remote intrusion detection system
- Remotely ctivate panic alarms
- Remotely top up credit balance on remote intrusion detection system

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

If you're looking to integrate with arduino project, make sure to check out the the repository corresponding to the [ariduino firmware](https://github.com/cgardesey/smart_security_firmware) for detailed instructions.

## License

Smart Security project is licensed under the [MIT License](LICENSE). Feel free to use it as a reference or starting point for your own projects.
