# Dungine
### RPG Dungeon Crawler API/Language

### Overview
The Dungine language (hereafter just "Dungine") is a tool for quickly and easily creating games using the Dungine API.  
There are two types of Dungine files, each with different syntax: headers (.dgnh) and game files (.dgn).  
Header files follow a simple __command__ *args* syntax, which game files are more similar to JSON.  
The entry point for any Dungine game is __main.dgnh__.  
All files must be in a folder called __LocalFiles__ that is in the same directory as the .jar file.

### Header Commands
 + __read__ "*path*"
   + interprets a game file
   + *path* is the filepath relative to LocalFiles, excluding file extensions
   + For example, to read the file *Worlds/World 1.dgn*, the command is __read__ "*Worlds/World 1"
 + __import__ "*path"
  + similar to __read__, but instead interprets another header file
  + *path* follows the same rules as above
