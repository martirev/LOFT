# Release 3

## Introduction

In this release, our focus has been finalizing the project. This involved incorporating new features, refining existing ones, adjusting styling, and integrating a REST API centered around the core logic. Additionally, we have implemented Gitlab CI to establish coding standards, Mailmap for consistent contributor recognition, and jpackage and jlink to ensure the project's shippability. Similarly to previous releases, we continued to emphasize good work habits and effective use of git.

## Improvements

For this release, some enhancements have been a new color scheme, two new screens (highscore and userinfo), and a reworked Progressscreen. We aimed to improve the existing features more than introducing a plethora of new functionalities.

We decided to change the color scheme of the project for this release. This was to enhance the user experience and make it more appealing. Utilizing [Coolors](https://coolors.co/) allowed us to create color palettes, and we chose the following palette. Additionally, a new styling feature is an animation within some of the screens. This dynamic addition boosts user engagement by introducing a subtle yet visually appealing animation.

<table>
    <tr>
        <td style="text-align:center;"><img src="https://i.imgur.com/AjLM0Iq.png" alt="Color palette" style="width:800px;"></td>
        <td style=text-align:center;><img src="https://imgur.com/tblBTXh.gif" alt="Animation gif" style="width:800px;"></td>
    </tr>
    <tr>
    <td><em>Color palette</em></td>
    <td><em>Løft animation</em></td>
    </tr>
</table>

The Progressscreen underwent a comprehensive redesign with the primary objective of improving the usability of the charts. This involved increasing their size, integrating additional text fields, and introducing the capability for users to assess their performance on a day-by-day basis for each logged exercise through a list view. Through these enhancements, we anticipate achieving a more user-friendly and informative Progressscreen, ultimately elevating the overall user experience within the application.

<table>
    <tr>
        <td style="text-align:center;"><img src="https://i.imgur.com/KyciZNq.png" alt="Progress screen" style="width:800px;"></td>
        <td style="text-align:center;"><img src="https://i.imgur.com/TNba6Vr.gif" alt="Journal screen gif" style="width:800px;"></td>
    </tr>
    <tr>
    <td><em>Progress screen</em></td>
    <td><em>Journal screen demo</em></td>
    </tr>
</table>

## New content

Additionally, we added the final component of the app, the HighscoreScreen. This is a space where users can review their statistics for every exercise they have ever done. We included this feature because many people who work out like to keep track of their records and achievements.

<table>
    <tr>
        <td style="text-align:center;"><img src="https://i.imgur.com/9bY2PgR.png" alt="Highscore screen" style="width:800px;"></td>
        <td style="text-align:center;"><img src="https://i.imgur.com/z3hsVIO.gif" alt="Highscore screen gif" style="width:800px;"></td>
    </tr>
    <tr>
    <td><em>Highscore screen</em></td>
    <td><em>Highscore screen demo</em></td>
    </tr>
</table>

## REST API

The significant addition in this release is the REST API, providing opportunities for cloud-based storage and enabling the extension of user interaction capabilities.

### Implementation

We have implemented a REST API into our project. This is a great way to make our project more scalable as we can, in the future, host a server 24/7 with the REST API and have multiple clients connect to it. The REST API is built around the core logic of our project, and is therefore a great way to access the core logic. The REST API is built with Spring Boot, and is therefore easy to maintain and extend. The REST API is also documented in its own markdown file in [./restapi.md](./restapi.md). As the documentation states, it is possible to use the API through the terminal with `curl`.

### User Connectivity Options

We have made it possible for users to easily connect to a server and choose which server to connect to. This happens on the Loginscreen as this screen is the first the user sees. As we do not have a server hosted 24/7, this is a great way for the user to easily test the app with a server hosted on localhost and on public IP/domain, for example with the server hosted on Eclipse Che. The user can also choose not to connect to a server and use the app without the REST API.

## Architecture

While maintaining a modular template, a new module, [springboot/restserver](../../loft/springboot/restserver/), was introduced. This module adds a REST API and a rest server to the application. Explore the entire application architecture in the [Package Diagram](./PlantUMLFiles/packageDiagram.puml), user interaction with the client in the [Sequence Diagram](./PlantUMLFiles/sequenceDiagram.puml), and a snapshot of our core logic in the [Class Diagram](./PlantUMLFiles/classDiagram.puml):

<table>
    <tr>
        <td style="text-align:center;"><a href="./PlantUMLFiles/packageDiagram.puml"><img src="https://www.plantuml.com/plantuml/png/VP5XRiCW38N_SmhK0x0RJFstSx3KITaS71bJLPfszq4MYT2woQ_elO_tm3udyInvHZFGl524A3O7-sDi-HO_VFi9BD8eBWTDSmD5OK84JkRNx586Oj1ogjwJfxHmYDCdaRmn94d0D-2JzSasnpL7IrL1jSPMw26y9FRi612kFbwm05hTtkzFVYIAJt5V_kR7-nQqQxdF-21jurndMVRWtFjARV_DNczHHGxJLOgZxKFHse6sZ2DCUQvvahHGqmF5gvCnHjzezPlpt_22p4lNSuog3pX3lEGP_m00" alt="Package diagram" style="width:800px;"></a></td>
        <td style="text-align:center;"><a href="./PlantUMLFiles/packageDiagram.puml"><img src="http://www.plantuml.com/plantuml/png/bLNHRfim57tFL_YHA5a-e9kgPWbjCsK1KQ9gJv475seLR6IRPbENVVik3cqSS5760nRsESVNn_TURfIcKjTbCQfeyanp81ahu8doj60y9w-Z4S4dAQXIPCaa97ejCZrF4b0ALqdxV2AlAAHPSZ3yMr6a8DSYmId1hyY3bgZs4Rg7UmTEJa05-jR2UaVe6N_iukKTtdWe6WavKnhaLe6y0zqCoclUfCONKjUu_AMPk7RHypVAfSYJzjGYc0c56itN0BlrmC0vBM5AsecAAhKNClqlqRra6cBHcELPD0xxRSlyAKG1b1louNX9Z6kG6Ks0EDDYSkQexQGjra9D08z2FejQny8GsY-oFuvJCgnmgIzT5oRlKFyNHgkw0ixDwYgb6XgT5S_4yMojMMsSM0JDEFvyxfnTKX6KGiC79GKyhGGw_9Kgs4g6GKShrytDmt2zMipZLR3PVNi8DbQ63s9lW-ZxRXkj_SqYMFhBgm7MOk67yUxUdo_zQ4X-4MnYVnFluX-XVm4yLqtLts7CLittJF7mt5qpnfrhbuLTsQxs8Lz0pWls0Xwga4f8RL_h8CC4K4lc8Fmz4H0GKadBj-nHLa1zT2PaYL7xF14fj9JPR4QgHcAOQixB7fb-ClkT-pCb3Yq4k8JCEij-cwAnHUn2C-nUvXg0kZTztavQmr7EzZUnELwlCrauTn-rOkZ_YcQpFzTM3p8WEu-SeCFe1azSbyK_" alt="Class diagram" style="width:800px;" /></a></td>
    </tr>
    <tr>
    <td><em>Package diagram</em></td>
    <td><em>Class diagram</em></td>
    </tr>
</table>
<table>
    <tr>
        <td style="text-align:center;"><a href="./PlantUMLFiles/sequenceDiagram.puml"><img src="https://www.plantuml.com/plantuml/png/bLDDRzim3BthLn0-fSNY13iLHfirQ_THBC26ThlbeiXCebOMFP5krty_AK6AsFMm-cHPVE_nwOcwPD51cie1u-y6lS5hgpT1Lqff8nJW5sDGTSHOOslj1HgR6_8Io3aCd2zeO_sT2OZ-wcKRD4DVeQsqjWwtsfVE-ar-YnK9BcWjSsEGEKbqzrfihaFahOWa3yWIRJvYo1EuwsCjRp5mRGCQQHlexgbIYrvfHcXYlnk_floUoITe-YgLqeBn3FfJAM1NDywQ1mRP8in_t83PedbOqH-8Ja2UQsHOUkkX2GukO2jI5vE98wFTbbYAi-dPDFwkPP881x6bJtAP8U-ZumnMZKWqrEyZ6kn6Mu1xMUVxBEVEFk8ekZXHNNHIw2HTm9TFFs5lJ7Lguvw6aP2zdqxX-pUGq63Mq-FVEUue_OTBj7tCiS7LFWuiipTUpLEmWWonMg8NgnqVXqm-vfov930yOMX5l4593KTfZhoky1tKclc9GZauxIJ1a_E3qcpfFnoqBiuFgzchEvcrfdkOmrVCgBPx20LyjWuv3wZBkNCVdmLvb9xPWemMI_v4xN3Z_lPtajvqZtnV0idTc5Rt4Jfiu8wPm-RkcFd_yJkznN9t4Yn12BvIXNlugLANwCkcSkel" alt="Sequence diagram" style="width:800px;" /></a></td>
    </tr>
    <tr>
    <td><em>Sequence diagram</em></td>
    </tr>
</table>

## Code quality

In previous releases, we have maintained a strong focus on code quality, emphasizing the creation of optimal tests. A significant effort has been directed towards achieving nearly 100% code coverage with JaCoCo.

It's worth noting that while our coverage is almost complete, we have not reached 100% in the hashing method in the core module due to an exception that is caught but never tested. However, this exception will never happen since we have already set the hashing algorithm to "SHA-256." The NoSuchAlgorithmException will therefore never happen. Therefore, we have ignored this JaCoCo issue.

Additionally, within the UI module, the class App.java remains untested. This is attributed to the nature of this class, which cannot be tested.

Regarding Checkstyle, we adhere to the same standards, taking responsibility for maintaining clean and consistent syntax throughout the entire project. We employ Google's Checkstyle rules with a slight modification, opting for a 4-space indentation instead of the default 2. For further details, refer to our [Checkstyle configuration file](../../loft/config/checkstyle/checkstyle-checker.xml). Furthermore, Spotbugs is employed as part of our quality assurance measures.

## Work habits

After submitting Release 2, we convened for a sprint meeting to plan the next release. We were concerned that the REST API might be the most time-consuming part, so it was prioritized early on. Additionally, our focus extended to maintaining code quality, with tools like GitLab CI and code reviewing playing crucial roles.

GitLab Issues served as the starting point for all our work, and we attempted to create an issue board that would motivate our entire group.

For larger implementations, we utilized a development branch that served as the master for every part of the implementation. Here, we created new branches for specific tasks, such as controllers and ui, and then merged them back into the development branch. This was a good strategy as it allowed for smaller code reviews and kept everyone updated on progress.

## Gitlab CI

![Code Coverage](https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2303/gr2303/badges/master/coverage.svg)
![Pipeline Status](https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2303/gr2303/badges/master/pipeline.svg)

We have set up Gitlab CI and used this as much as we possibly could. This checks that our project builds, that checkstyle and spotbugs are in order and that our tests are good. In this release, we have made it possible for the pipelines to run even our UI tests, which is great for code review. To do this, we implemented monocle, which is a library that makes it possible to run JavaFX tests headlessly.

We have also added a jacoco report to the pipeline, which shows the test coverage of our project. This is a great way to see if we have any modules which needs more test coverage. The generated code coverage can be viewed as a graph over time in the [Repository Analytics page](https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2303/gr2303/-/graphs/master/charts) on Gitlab. Gitlab CI also deploys the jacoco report to Gitlab Pages, which can be viewed at [https://it1901.pages.stud.idi.ntnu.no/groups-2023/gr2303/gr2303/\<branch-name\>/](https://it1901.pages.stud.idi.ntnu.no/groups-2023/gr2303/gr2303/master/). This hosts the jacoco report for the master branch, but it is also possible to view the jacoco report for other branches by changing the branch name in the URL.

To be able to run the UI tests headlessly, we had to create a new Docker image which includes java, maven and javafx. This image is available on [Dockerhub](https://hub.docker.com/r/runarmod/maven-javafx) as runarmod/maven-javafx. This image is used in the [.gitlab-ci.yml](../../.gitlab-ci.yml), as the image previously used could not run UI tests. The Dockerfile used can be found [here](../../Dockerfile).

## Example Data

In previous releases, we have included an example data file for demonstrating data storage and/or testing purposes. Upon the initial use of the application, no data is stored.

Similar to previous releases, to use example data in this release, move the [example data](./userdata.json) to the "user.home" location. On Windows, this location is typically in the "C:\Users\<username>" folder, and on macOS, it is usually "/Users/<username>". This example data file is the most extensive and comprehensive compared to those in previous releases. The password for the user **RunarSM** is **Runar123** and for **MartinR** the password is **Martin123**.

## Storage method

We chose implicit saving because it makes for a natural user experience. Implicit saving automatically preserves user data without requiring explicit user action, ensuring an easy and uninterrupted workflow. When the information is saved, the user does not have to specify where the data is stored, as this happens automatically. This enhances usability, reduces the risk of data loss, and makes the application easier for the user to use.

## Other Information

There are two other README files which describes the app more in depth.

- [README.md](../../README.md) - This file contains information on the content, building, requirements and directory structure.
- [gr2303/README.md](../../gr2303/README.md) - This file contains information on what the app is about and some key fetures it should offer. It also contains some user stories.

This is the last release, and documentation for release one and two can be found in the two other folders [release1](../release1/) and [release2](../release2/) with diagrams and userData.json files.

## Conclusion

As we conclude Release 3, we take pride in the successful development of a robust REST API as part of our project's advancement. Our deliberate focus on enhancing existing features and introducing a novel element has yielded a more polished and versatile application. We are delighted with the final result and consider it a fitting conclusion to this phase of our project.

Our commitment to work habits, exemplified by GitLab CI, GitLab Issues, and disciplined branching and outfilling commit messages has made an efficient development environment. The JavaDoc, markdown files, example data and diagrams provide valuable resources for users and contributors.

The emphasis on code quality, work habits, and continuous integration ensures a robust and maintainable codebase.
