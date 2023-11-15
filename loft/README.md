# Løft - Your Ultimate Workout Tracking App

## Introduction
Løft is designed to help people track their workouts, offering assistance to individuals who struggle to remember their previous exercise details while also motivating users by allowing them to monitor their progress.

## Key Features

- Workout Logging: Løft allows the user to record and save details of every workout session, ensuring that the user can keep track off all their progress.

- History Tracking: View a comprehensive history of all the users registered workouts. This feature helps analyze progress over time, making it easier to set and achieve fitness goals.

- Personal Records: Keep a record of the users personal bests and milestones in various exercises. Løft will help the user stay motivated by highlighting the important achievements.

- Visual Representation: Løft will provide the users with a visual overview of their progress for every exercise they have ever done, allowing the user to review all their achievements.
  
- Exercise Catalog: Easily access a list of exercises previously performed, making it convenient to track workouts and avoid repetition.

## Screens

### Log in screen

When first opening the app, you will be given the option to log in or register. Once registered, you can enter your username and password. Then, by simply pressing the **Log in** button, the home screen will appear. We do not store passwords in plain text; instead, we use the SHA-256 algorithm to hash them. This ensures that users' passwords are not easily compromised.

<table>
  <tr>
    <td style="text-align:center;"><img src="https://imgur.com/VzjjKuq.png" alt="Login screen" style="width:800px;"></td>
    <td style="text-align:center;"><img src="https://imgur.com/AAo4ptd.png" alt="Register screen" style="width:800px;"></td>
  </tr>
  <tr>
    <td><em>Login screen</em></td>
    <td><em>Register screen</em></td>
  </tr>
  <tr>
    
</table>

We have also made it possible for the user to decide whether to use local save files or a REST API. At the log in screen the user can select which mode to use, and what server to connect to (can be localhost). The user will connect to the server if it is online and respondes to the request. If the server is offline, the user will be notified and the application will switch to use local save files.

<table>
    <tr>
    <td style="text-align:center;"><img src="https://imgur.com/dryGsC1.gif"
    alt="Connect to URL gif" style="width:800px;"></td>
    </tr>
    <td><em>Connect to URL demo</em></td>
</table>

### Home screen

At the Home Screen, the user can navigate to every part of the application. If the user wants to start registering a new workout, they can go into **New workout** and begin adding exercises to their routine. Furthermore, if the user desires to review their previous exercises, they can access the **Journal**. Lastly, we have included the **Highscores** section of the app. Here, users can check all their stats for every exercise they have ever done. The **Return** button will be available in all three parts of the app and will always take you back to the home screen.

<table>
  <tr>
    <td style="text-align:center;"><img src="https://imgur.com/3a6FFxc.png" alt="Home screen" style="width:800px;"></td>
    <td style="text-align:center;"><img src="https://imgur.com/mE34V6P.gif" alt="Home screen gif" style="width:800px;"></td>
  </tr>
  <tr>
    <td><em>Home screen</em></td>
    <td><em>Home screen demo</em></td>
  </tr>
</table>

### New workout screen
When adding an exercise, you will be given the option to select the exercise, specify the number of sets, reps, and weight. Once you've added the exercises, they will be stored in the current workout. After finishing the workout, simply press **Finish Workout**, and the workout will be stored in your journal.
<table>
    <tr>
        <td style="text-align:center;"><img src="https://imgur.com/Eu5iSM8.png" alt="New workout screen" style="width:800px;"></td>
    </tr>
    <tr>
        <td><em>New workout screen</em></td>
    </tr>
</table>

### Journal screen

Once an exercise is added, the journal is updated. To view the workout, press the specific exercise, for example **2023-10-09 Number of exercises: 4 Number of sets: 16**. To view your progress in a graphical interface, press the **Progress** button.

<table>
    <tr>
        <td style="text-align:center;"><img src="https://imgur.com/6bOzCXW.png" alt="Journal screen"
        style="width:800px;"></td>
        <td style="text-align:center;"><img src="https://imgur.com/KyciZNq.png" alt="Progress screen"
        style="width:800px;"></td>
    </tr>
    <tr>
        <td><em>Journal screen</em></td>
        <td><em>Progress screen</em></td>
    </tr>
    
</table>

At the **Progress** section of the journal, you will be able to see a visual representation of all the exercises you have ever done. Here, you will be able to track the progress in your exercise personal records over time.

<table>
    <tr>
        <td style="text-align:center;"><img src="https://imgur.com/TNba6Vr.gif" alt="Journal screen demo"
        style="width:800px;"></td>
    </tr>
    <tr>
        <td><em>Journal screen demo</em></td>
    </tr>
</table>

### Highscore screen

At the highscore screen, you will be able to see key information about every exercise you have ever done. The picture below shows the information that the user can view.

<table>
    <tr>
        <td style="text-align:center;"><img src="https://imgur.com/9bY2PgR.png" alt="Highscore screen"
        style="width:800px;"></td>
    </tr>
    <tr>
        <td><em>Highscore screen</em></td>
    </tr>
</table>

### My profile screen

If the user wants to change or look at their profile, they can go to **My Profile** from the home screen and modify their user information if desired.

<table>
    <tr>
        <td style="Text-align:center;"><img src="https://imgur.com/J4Uww58.png" alt="My profile screen"
        style="width:800px;"></td>
        <td style="Text-align:center;"><img src="https://imgur.com/8AtLAel.gif" alt="My profile screen"
        sytle="width:800px;"></td>
    </tr>
    <tr>
        <td><em>My profile screen</em></td>
        <td><em>Altering password demo</em></td>
    </tr>
</table>


# User Stories LØFT

These user stories serve as motivation for determining the necessary functionality the app should have. We utilized these user stories throughout the development process to find missing features from the user's perspective.

## Logging a Workout Session (US-1):

As an active and fitness-conscious individual, I want to track my workout sessions because it helps me achieve my fitness goals. This involves logging my exercises, including the number of sets, repetitions, and weight per repetition. Additionally, I want the capability to access previously logged exercises and add new exercises if they haven't been logged yet. This possibility gives me an overview of how the exercise is progressing during the workout and allows me to store it.

### Important functionalities:

- Add new exercises
- Manage the number of sets, repetitions, and weight
- Upload the session when completed
  
### Important information to view:

- List of added exercises
- Available exercises to choose from

## Progress Overview (US-2):

As a goal-driven individual, I want to review my progress throughout my training because it provides a clear path to success. This involves the ability to review previous workouts, examine past sessions to observe how I've improved in various exercises with a graphical representation of these improvements. A display of the development in total weight for each exercise on a weekly basis. This overview gives me a visual representation of how to achieve my goals.

### Important functionalities:

- Access previous sessions
- Navigate to a specific session

### Important information to view:

- Graphical representation of progress
- Overview of sessions sorted by date

## View my personal best (US-3):

As a user, I want to view a highscore list because it motivates me to keep working out. This list should display information about my highest total weight in a workout and personal records for specific exercises. This information provides me with motivation to keep progressing and achieve new personal bests.

### Important functionalities:

- Navigate through different exercises.
  
### Important information to view:

- View my personal bests.
- View highest total weight in given exercise.

## Changing user information (US-4):

I desire the ability to modify my user information. By allowing me to edit user information, I have the opportunity to update the email if I wish to link the account to another one. I also want to have the option to change the username if I am not satisfied with it, and to switch passwords for security reasons, for example.

### Important functionalities:

- Updating user information.
- Saving the changes.
- Log out of the application.
  
### Important information to view:

- View my current name, username and email.
- View password fields for changing password.
