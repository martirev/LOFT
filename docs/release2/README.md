# Release 2

### Introduction

In this release we have made our project modular, added the possibility for multiple users, and created a journal and progress screen. We have also had focus on code quality, such as improving the test coverage, enhancing code quality with the help of Checkstyle and Spotbugs. Also, we have now set the name for the project to "loft."

### LoginScreen and RegisterScreen

When running the application, the first screen to be shown is the LoginScreen. The user can here either register or log in. The RegisterScreen will ask for name, username, password, and email. This data will be stored in the user-data file located in "user.home". During login, the user's data will be fetched from the user-data file. A successful login will send the user to the HomeScreen.

| ![RegisterScreenImage](https://i.imgur.com/dsMf0Fm.png) | ![LoginScreenGif](https://i.imgur.com/owe5dlN.gif) |
|:--:|:--:|
| *RegisterScreenImage* | *LoginScreenGif* |

We do know that our login and registration system is not the most secure. Since this project is not mainly focused on security, we did not prioritize it. At least the passwords are not stored as plain text but stored as a hash using the SHA-256 algorithm.

### JournalScreen

In this release, we have added the possibility to see logged workouts through the JournalScreen. All workouts with dates are shown in the "Workout History" table, and once selected, a specific workout is shown in the "Current Workout" table. Entering "Progress" will let you view a graph following the increase in total weight lifted and some of your personal bests.

| ![JournalScreenImage](https://i.imgur.com/yR6ilxQ.png) | ![JournalScreenGif](https://i.imgur.com/SDNhYtw.gif) |
|:--:|:--:|
| *ExcersiceJournalScreenImage* | *ExcersiceJournalScreenGif* |

### Example Data

When the app is first opened, there are no previously stored users or user data. Therefore, the *userData.json* file is not created until you register a user. As an example of how the data is stored, please see the following file [here](./userdata.json). To use this example data, move the file to the location of "user.home". For windows this is usually in the folder "C:\\Users\\\<username\>". For mac this is usually "/Users/\<username\>". If the file is placed in the correct location, the app will automatically load the users and user-data from the file when it is opened.

### Architecture

As mentioned in release 1, we have now changed from the package-template to the module-template. There are now three modules: ui, core and fileHandling. This improves our encapsulation, gives an easier-to-manage codebase, and better dependency management.

| ![Package Diagram](https://www.plantuml.com/plantuml/png/XP1H3i8W44J_znISW2EO_kar2AKLNNOR2gQ9yUxIXaJGJUIBl9qPTXZMP6BA0S5oM9WS9PMzUe8gPp7sRcQdaAUaivUOjamfCgABAxoWIR4SdPIpyruvUNGNGoDwcYL7E--TvvMfE7fuCmypRJMeUMKB-8NgxiVYfRJMfqDLxvznxQfqzFCrbObKNc2TeJR_A-lhmY-JKa5G5S7-Fp0u6dF0Dm00) | ![Class Diagram](https://i.imgur.com/NKh3pp5.png) |
|:--:|:--:|
| *Package Diagram* | *Class Diagram* |

| ![Sequence Diagram1](https://www.plantuml.com/plantuml/png/LS_HoSCm30JWTqzn2_mBvE4dK2XaW0wWnWguTQKWAMJznYrfoAllFdpgFSXYUTL4OwZXvcnf9eiobfaaFW-31Dj48v-IYqgOrhg1J45tSiR3LQwbSahDuU__x3iCCY_XQ02J6WZ2ArQrfowHJkMcZp-whoaE3pNEA0BwmQFNrBwYstDGpXi9nTJ6zInvE_aD) | ![Sequence Diagram2](https://www.plantuml.com/plantuml/png/RP71JiCm44Jl_eezme4Vo05LGY0uSAa4d4qyRGt66tOt3VrxKhUfPSHrykPbibcfUR7nApdV6mkzACGDSnJxEFXi9NZA1jdu7isJEyucd1BaJonmmPj_zmhwKCuFCS6veuXkRbjXHoKMV8zGett45FEM1i4-ygWqiNpoQEgQrb5JtzvHOX_kVo1zL3n6DPR3rRMhn8aNTBI5FRFQ6ZsolPtbXPLYNUPsbqcZGYuxQ7UokuMhvZLqu1neYhOxbnQuoDl9BPvca7XJlxQI_rpCXr0FGyQb_XenjKTQcXs5g4FoXpfeXHpcN_uB) |
|:--:|:--:|
|*Sequence Diagram 1* | *Sequence Diagram 2*|

### Code quality

Another focus during this release was the test coverage and code quality. We have used JaCoCo, which was implemented in release 1, to ensure good coverage in all modules for tests. We have not managed to have 100% in core due to an exception which is caught but never tested. However, this exception will never happen since we have already set the hashing algorithm to "SHA-256." The NoSuchAlgorithmException will therefore never happen. Therefore, we have ignored this JaCoCo issue.

| ![JacocoCoreCoverageImage](https://i.imgur.com/d6SmNRw.png) | ![JacocoCoreIssueImage](https://i.imgur.com/u48Eino.png) |
|:--:|:--:|
| *JacocoCoreCoverageImage* | *JacocoCoreIssueImage* |

Also, inside the ui module the class App.java is never tested. This is because this class cannot be tested. We have also implemented code quality checkers such as Checkstyle and Spotbugs. Checkstyle enforces a set of coding standards and guidelines, which adds consistency to our code. Spotbugs finds potential defects in our code, such as potential NullPointerExeptions not being handled or revealing private fields in classes. We have used googles checkstyle rules with the slight modification that we use 4 spaces indentation instead of 2. See our [checkstyle config file](../../loft/config/checkstyle/checkstyle-checker.xml).

We have also set up Gitlab CI so that we can check if the project builds, that our (non-UI) tests are ran successfully with jacoco results, and with automatic checkstyle and spotbugs analysis.

### Work habits

At the end of release 1 we had another sprint session where we started discussing new issues to add to our project and a new milestone. For this release we have also been working more in pairs. Seen in some commits there are now Co-authours. For the most part we split the work between the two pairs Runar,Martin and Sigurd,Markus. 

### Improvements for release 3

For our next release, release 3, we will extend the application so that the user can view their highscores by opening the "HighScoreScreen". We will also explore the possibility of creating a REST API through which we can store and retrieve data from a remote server. This addition will enhance the functionality and connectivity of our application. For future releases, this will open the possibility to create solutions that allow users to access and share their highscore data seamlessly.

### Other Information

The project is now compatible with Eclipse Che. The URL for our Eclipse Che workspace is in the [root README.md](../../README.md). This ensures that our project will run regardless of OS, JDK installed and so on. Instructions on how to run and see UI with Eclipse Che is instructed inside [root README.md](../../README.md).