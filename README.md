# Dungine
### RPG Dungeon Crawler API/Language

### Overview
The Dungine language (hereafter just "Dungine") is a tool for quickly and easily creating games using the Dungine API.

### Files
There are two types of Dungine files, each with different syntax: headers (__.dgnh__) and game files (__.dgn__).  
Header files follow a simple __command__ *args* syntax, while game files are more similar to JSON.  
The entry point for any Dungine game is __main.dgnh__.  
All files must be in a folder called __LocalFiles__ that is in the same directory as the .jar file.

### Header Files
##### Commands
 + __read__ "*path*"
   + interprets a game file
   + *path* is the filepath relative to LocalFiles, excluding file extensions
   + For example, to read the file *Worlds/World 1.dgn*, the command is __read__ "*Worlds/World 1"
 + __import__ "*path*"
   + similar to __read__, but instead interprets another header file
   + *path* follows the same rules as above
 + __param__ "*path*" *type value*
   + creates or overwrites a value in the game's registry
   + allows you to set many different parameters for your game, as well as store custom data
   + this is complicated, and we will cover it in more detail later

##### Example
```
param con.setting.echo b false
import "definitions"
read "Worlds/World 1"
read "Worlds/World 2"
read "Player"
```
Line-by-line:  
 + ```param con.setting.echo b false```  
 Set the registry __param__eter at __con.setting.echo__ to the __b__oolean value of __false__
