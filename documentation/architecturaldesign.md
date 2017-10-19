# Project Architectural Design

This document provides a comprehensive architectural overview of the project, to depict different aspects of the project.

The Graph below is the modular organization diagram.

![Project Architecture Design](images/architecturedesign.jpeg)

## Modules Description

### 1.1 Game Module
The map module includes all the basic elements of the game, like player etc..

File_Name  | Description
------------- | -------------
Player  | The class is to describe attributes of players and calculate how many armies are given according to numbers of cards.
RiskGame  | 

### 1.2 MapEditor Module
The MapEditor Module includes all the elements about editting map.

File_Name  | Description
------------- | -------------
MapEditor  | The class is used to create a window for player to edit maps of games.
BasicInfoView  | The class is used to present the basic information of maps.
UsefulRenders  | The class is used to render the map.

### 1.3 Map Module
The map module includes all the basic elements of maps, like continent, country etc..

File_Name  | Description
------------- | -------------
Continent  | The class is to describe attributes of the continent.
Country  | The class is to describe attributes of the country.
RiskMap  | The class is to achieve the function of map editor.
NodeRecord  | 

### 1.4 RiskGame View Module
The view module include all the GUIs of each phase, like startup phase etc..

File_Name  | Description
------------- | -------------
StartupPhase  | Fill the table with countries' name and their armies
FortificationPhase  | In this class, it contains all necessary actions in fortification phase. Player can move army from one country to another
ReinforcementPhase  | In this class, it contains all necessary actions in reinforcement phase. Players place reinforcement armies on the map based on the calculation of correct reinforcement armies' number.
RiskGameMain  | The class is used to create a window for player to edit maps for games.
UsefulRenders  |	The class is used to render maps after phases.
ExchangeInteraction | This class is GUI for Exchange cards to armies.

### 1.5 Maps
The module is used to store map files.

### 1.6 Images
The module includes all resource files used in the risk game.

### 1.7 JavaDoc
The module includes JavaDoc generated from the project

### 1.8 Documentation
The module includes all the docments of the project