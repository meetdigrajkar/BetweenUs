# Between Us

## About / Synopsis

* A Java & Libgdx adaptation of Among Us/Mafia
* Project status: working/prototype
* Group 66

## Table of contents

> * [Between Us](#title--repository-name)
>   * [About / Synopsis](#about--synopsis)
>   * [Table of contents](#table-of-contents)
>   * [Installation](#installation)
>   * [Usage](#usage)
>     * [Screenshots](#screenshots)
>     * [Features](#features)
>   * [Code](#code)
>     * [Content](#content)
>     * [Build](#build)
>   * [Resources (Documentation and other links)](#resources-documentation-and-other-links)
>   * [Contributing / Reporting issues](#contributing--reporting-issues)


## Installation

Sample:

* The Server (Server.java) must be hosted locally by one of the players and the port 7077 must be forwarded on the Server's machine.
* The player hosting the Server must provide their IPv4 address to the Client players.
* Hopefully, in the future the Server will be hosted on the cloud. Thank you!

## Usage

### Screenshots

Title Screen

![1](https://github.com/meetdigrajkar/BetweenUs/blob/master/screenshots/main_screen.PNG)

Join Room Screen

![1](https://github.com/meetdigrajkar/BetweenUs/blob/master/screenshots/join_screen.PNG)

Create Room Screen

![1](https://github.com/meetdigrajkar/BetweenUs/blob/master/screenshots/create_room_screen.PNG)

Lobby Screen

![1](https://github.com/meetdigrajkar/BetweenUs/blob/master/screenshots/lobby_screen.PNG)

Game Screen

![1](https://github.com/meetdigrajkar/BetweenUs/blob/master/screenshots/game_screen.PNG)

### Features

* Crew Members
* Imposters
* Four Tasks: Admin, Communcations, Electrical and Reactor
* Polus Map
* Multiplayer across the Internet (Client-Server architecture)

## Code

### Content

* Developed in Java using Libgdx and Gradle

### Requirements

* Gradle
* LibGdx

### Build

1. Clone repository from this github page.
2. Download the latest version and setup AdoptJDK in your favourite IDE from https://adoptopenjdk.net/
3. Open the project in your favourite IDE and import the project as a Gradle Project (preferably not in IntelliJ because it sucks!)
4. Change IPv4 address in Client.java (Line 51) to where the Server will be hosted
5. Port forward port 7077 on the machine that will be running Server.java
6. This step is for the player that will be hosting the Server - Run Server.java as a Java Application
7. Run DesktopLauncher.java as a Java Application
8. Create a Room
9. Let other players know to join the newly created room via the "Join Room" Screen. (Click Refresh to fetch the available rooms)
10. Once everyone is in the game, the host will press "Enter" to start the game.
11. Enjoy!

## Resources (Documentation and other links)

* Great thanks to the sprite sheet cutter tool available at https://www.spriters-resource.com/pc_computer/amongus/

## Contributing / Developers

* Meet Digrajkar
* Abdillihai Nur
* Tareq Hanafi
* Alec D’Alessandro
