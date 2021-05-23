# GameOfLife
[![Build Status](https://travis-ci.org/Maretzky85/GameOfLife.svg)](https://travis-ci.org/Maretzky85/GameOfLife)
[![Known Vulnerabilities](https://snyk.io//test/github/Maretzky85/GameOfLife/badge.svg)](https://snyk.io//test/github/Maretzky85/GameOfLife)
[![Maintainability](https://api.codeclimate.com/v1/badges/96d4e1c3279b447a3d5b/maintainability)](https://codeclimate.com/github/Maretzky85/GameOfLife/maintainability)


  * [Getting Started](#getting-started)
    + [Prerequisites](#prerequisites)
    + [Launching](#launching)
  * [Screenshots](#screenshots)
  * [Authors](#authors)
  * [Acknowledgments](#acknowledgments)

JavaFX Game Of Life implementation (at least in the beginning)

## Getting Started

To run this project just clone repository and run mvn package exec:java

### Prerequisites

  - [Java](https://www.java.com/download/)
  - [Maven](https://maven.apache.org/download.cgi)
  
What things you need to install the software and how to install them
Ubuntu:

```
apt-get install maven
```
  [Tips for installing Java8 on Ubuntu](https://askubuntu.com/questions/1136401/how-to-install-oracle-java-8-on-ubuntu-19-04)

### Launching

Launch console, go to the project folder, and type:

```
mvn package javafx:run
```

and hit enter

alternatively, you can go for:

```
mvn package
mvn javafx:run
```
mvn package must be run only once. mvn exec:java is for starting a built project.

If everything is in order You should see something like :

```
[INFO] BUILD SUCCESS
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ GameOfLife ---
Gtk-Message: 00:03:24.835: Failed to load module "canberra-gtk-module"
Game Of Life - Starting game...
```
And then a window should pop up.

## Screenshots

![menu](/assets/menu.jpg)
![screenshot1](/assets/screen1.jpg)
![screenshot5](/assets/screen5.jpg)
![screenshot2](/assets/screen2.jpg)
![screenshot3](/assets/screen3.jpg)
![screenshot4](/assets/screen4.jpg)

## Authors

* **Marek Sikora** - *Making this thing alive* - [Maretzky85](https://github.com/Maretzky85)

## Acknowledgments

* Small homework project that evolved. My playground for learning things like:
  - OOP principals
  - Programming patterns
  - Graphics and scaling
  - Math ;)
* 
*
* Initial project was a homework from CodeCool school for getting to know JavaFX and TDD
* TDD is not here, at that point I fail to understand the purpose of TDD and tests in general.
* Tests will be here if I ever have time to dive in this project again

