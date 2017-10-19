# Project Architectural Design

This document provides a comprehensive architectural overview of the project, to depict different aspects of the project.

The Graph below is the modular organization diagram.

![Project Architecture Design](images/architecturedesign.jpeg)

## Modules Description

### 1.1 Game Elements
The GameElements folder includes all the elements about the risk game.

File_Name  | Description
------------- | -------------
Player  | The class is to describe attributes of players and calculate how many armies are given according to numbers of cards.
RiskGame  | 

### 1.2 Map Editor
The MapEditor folder includes all the elements about editting map.

File_Name  | Description
------------- | -------------
MapEditor  | The class is used to create a window for player to edit maps of games.
BasicInfoView  | The class is used to present the basic information of maps.
UsefulRenders  | The class is used to render the map.

### 1.3 Map Elements
The MapElements folder includes all the elements used in the map file.

File_Name  | Description
------------- | -------------
Continent  | The class is to describe attributes of the continent.
Country  | The class is to describe attributes of the country.
RiskMap  | The class is to achieve the function of map editor.
NodeRecord  | 

### 1.4 Game Controller
The GameController folder includes all the phases in the risk game

File_Name  | Description
------------- | -------------
StartupPhase  | Fill the table with countries' name and their armies
FortificationPhase  | In this class, it contains all necessary actions in fortification phase. Player can move army from one country to another
ReinforcementPhase  | In this class, it contains all necessary actions in reinforcement phase. Players place reinforcement armies on the map based on the calculation of correct reinforcement armies' number.
RiskGameMain  | The class is used to create a window for player to edit maps for games.
UsefulRenders  |	The class is used to render maps after phases.

### 1.5 Maps
The folder is used to store map files.

### 1.6 Images
The folder includes all the images used in the risk game.