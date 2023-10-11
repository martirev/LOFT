# Project Structure

[<img src="https://eclipse.dev/che/docs/_/img/icon-eclipse-che.svg" width="20" /> open in eclipse che](https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2303/gr2303?new)

## Overview

This project is structured following a module template to ensure a clean and organized codebase. The main codebase for the project is located in the [loft/core/src/main/java/core](loft/core/src/main/java/core) directory, while the app is launched from [loft/ui/src/main/java/ui/App.java](loft/ui/src/main/java/ui/App.java).

## Building and Running the App

This project uses Maven to build and run the app. To start, make sure you are in the [project directory](loft/), the same directory as README.md, pom.xml, core/ and ui/. Run `cd loft` if you are in the root directory (the same directory as this README file).

Then, run the following command to build the project: `mvn install`. This will run all tests, including UI tests, and build the project. To run the app run `mvn javafx:run -f ui/pom.xml` as this runs the ui module, and will start the app.

To test the app, run `mvn test` from the [project directory](loft). Make sure the source code is compiled first with `mvn compile`. The command `mvn install` will do both these two in one, among some other things. Doing `mvn test` will run all tests, including UI tests. To only run tests for a specific module, go to that module in the terminal and run `mvn test` from there.

To check the code coverage, run `mvn jacoco:report` from the [project directory](loft). This will generate a report for all modules. These can be viewed by opening the index.html file in each respective target/site/jacoco folder.

To check for possible bugs run `mvn spotbugs:check` from the [project directory](loft). This will run the SpotBugs tool and check for possible bugs in the code. The results are printed to the terminal, or can be viewed with GUI using `mvn spotbugs:gui`.

To check the code for style errors, run `mvn checkstyle:check` from the [project directory](loft). This will generate a report for all modules. The results can be viewed in the terminal, or by viewing the checkstyle-result.xml file in each modules target folder.

## Requirements

- **Java 17**: This project is built using Java 17.
- **Maven 3.8.7**: The project is managed with Maven.
- **JUnit 5.10.0**: Used to test the code in the project.
- **TestFX 4.0.16-alpha**: Used to test the UI of the App.
- **JaCoCo 0.8.7**: Used for generating code coverage reports.
- **Gson 2.10**: Used to parse our JSON-files.
- **SpotBugs 4.7.3**: Used to check for possible bugs in the code.
- **Checkstyle 10.3.4**: Used to check the code for style errors.

## Directory Structure

The project's directory structure is organized as follows:

- loft
  - config
    - checkstyle
      - This is where the checkstyle rules are located.
  - core
    - src
      - main
        - java
          - core
            - Here goes the main codebase for the project. Things like Exercise-, Set-, Workout-classes, etc. should be located here.
      - test
        - java
          - core
            - This is where all the tests for the core package are located.
  - ui
    - src
      - main
        - java
          - ui
            - Here is the code for the user interface. The App.java file is located here, which is the entry point for the application.
            - controller
              - This is where all controllers to different screens are located. For example, the WorkoutScreenController is located here.
        - resources
          - ui
            - This is where all the fxml-files are located. These files are used to define the user interface.
      - test
        - java
          - ui
            - controller
              - This is where all the tests for the controllers are located.
  - filehandling
    - src
      - main
        - java
          - filehandling
            - Here are the necessary files for handling filereading.
      - test
        - java
          - filehandling
            - Here are all the tests for file handling located.
