# Release 1
### Introduction
In the first release, we have focused on developing the WorkoutScreen and the logic of adding exercises and keeping track of them even after the app is closed. This is done by storing every exercise completed to a JSON file. This way, next time the user opens the app, the exercises that already completed will show up as options when creating a new workout.

![Homescreen](https://imgur.com/6lGwpWR.png)

### HomeScreen
HighScore and Journal on the HomeScreen will be implemented in a later version of the app. When finished, the journal will let you look through all of your old workouts and see what exercises they consisted of. Highscore will show you all your personal bests.

![Adding a workout](https://i.imgur.com/nXYenyH.png)

### WorkoutScreen
This screen will let the user register a workout, which consists of up to multiple exercises. Each exercise will have a name and some sets. Each set has a weight and a number of reps. The current workout is shown on the right side of the screen. When the user is done with the workout, they will be able to save it to the journal, which can be viewed at a later release. At this point, the user can see the names of all the exercises they have completed in the past.
### Work Cooperation
At the start of the project, we sat down and wrote a contract about how we were going to work. We then tried setting goals for the group that we were going to try to finish within a week. During this week, we ended up changing the idea for the project. At the beginning of the next week, we had another sprint session where we laid down the next goals for the app. The work was split up so we could work separately on different branches. We split the work into:
- Backend and Controller - 2 people worked on this.
- Frontend - 1 person worked on this.
- Reading and writing to JSON - 1 person worked on this.

### Will Change in Later Versions
We are currently using the package-template but will change to the module-template in the next release. This way, we will not only be using different packages, but different modules as well, to make sure our code is easier to scale. For the next release, we will change our file reading and writing logic from using a JSON parsing library (GSON) to writing our own JSON parser. We will also have the option to store data to multiple people. Currently only the person "dummyName" can register workouts.
