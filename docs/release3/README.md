# Release 3

## Introduction

...

## Gitlab CI

![Code Coverage](https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2303/gr2303/badges/master/coverage.svg)
![Pipeline Status](https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2303/gr2303/badges/master/pipeline.svg)

We have set up Gitlab CI and used this as much as we possibly could. This checks that our project builds, that checkstyle and spotbugs are in order and that our tests are good. In this release, we have made it possible for the pipelines to run even our UI tests, which is great for code review. To do this, we implemented monocle, which is a library that makes it possible to run JavaFX tests headlessly. We have also added a jacoco report to the pipeline, which shows the test coverage of our project. This is a great way to see if we have any modules which needs more test coverage. The generated code coverage can be viewed as a graph over time in the [Repository Analytics page](https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2303/gr2303/-/graphs/master/charts) on Gitlab.

To be able to run the UI tests headlessly, we had to create a new Docker image which includes java, maven and javafx. This image is available on [Dockerhub](https://hub.docker.com/r/runarmod/maven-javafx) as runarmod/maven-javafx. This image is used in the .gitlab-ci.yml, as the image previously used could not run UI tests. The Dockerfile used can be found [here](../../Dockerfile).

## Example Data

When the app is first opened, there are no previously stored user-data. Therefore the exercise list is empty until the user submits a new workout-session. There is an example of how the data is stored in the same folder as this file [here](./userdata.json). To use this example data: Move the file to the location of "user.home". For windows this is usually in the folder "C:\\Users\\\<username\>". For mac this is usually "/Users/\<username\>". If the file is placed in the correct location, the app will automatically load the data from the file when it is opened.

### Other Information

There are two other README files which describes the app more in depth.

- [README.md](../../README.md) - This file contains information on the content, building, requirements and directory structure.
- [gr2303/README.md](../../gr2303/README.md) - This file contains information on what the app is about and some key fetures it should offer. It also contains some user stories.
