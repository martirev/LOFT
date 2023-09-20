# Project Structure

## Overview

This project is structured following a package template to ensure a clean and organized codebase. The main codebase for the project is located in the [gr2303/src/main/java/core](gr2303/src/main/java/core) directory, while the app is launched from [gr2303/src/main/java/ui/App.java](gr2303/src/main/java/ui/App.java).


## Building and Running the App

This project uses maven to build and run the app. To start, make sure you are in the project directory, the same directory as the pom.xml file. Run `cd gr2303` if you are in the root directory (the same directory as this README file).

Then, run the following command to build the project `mvn install`. This will run all tests, including UI-tests, and build the project. To run the app, run `mvn javafx:run`. This will start the app.

To check the code coverage, run `mvn jacoco:report`. This will generate a report in the target/site/jacoco folder. Open the index.html file to view the report.

## Requirements
- **Java 17.0.5**: This project is built using Java 17.0.5.
- **Maven 3.8.7**: The project is managed with Maven.
- **JUnit 5.10.0**: Used to test the code in the project.
- **TestFX 4.0.16-alpha**: Used to test the UI of the App.
- **JaCoCo 0.8.7**: Used for generating code coverage reports.
- **Gson 2.10**: Used to parse our JSON-files.

## Directory Structure
The project's directory structure is organized as follows:

- gr2303
  - src
    - main
      - java
        - core
          - Here goes the main codebase for the project. Things like Exercise-, Set-, Workout-classes, etc. should be located here.
        - ui
          - Here is the code for the user interface. The App.java file is located here, which is the entry point for the application.
          - controller
            - This is where all controllers to different screens are located. For example, the WorkoutScreenController is located here.
      - resources
        - ui
          - This is where all the fxml-files are located. These files are used to define the user interface.
    - test
      - java
        - core
          - This is where all the tests for the core package are located.
        - ui
          - controller
            - This is where all the tests for the controllers are located.

