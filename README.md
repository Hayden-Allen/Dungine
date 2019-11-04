# Dungine
### RPG Dungeon Crawler API/Language

### Overview
The Dungine language (hereafter just "Dungine") is a tool for quickly and easily creating games using the Dungine API.

### Files
There are two types of Dungine files, each with different syntax: headers (**.dgnh**) and game files (**.dgn**).  
Header files follow a simple **command** *args* syntax, while game files are more similar to JSON.  
The entry point for every game is **main.dgnh**.  
All files must be in a folder called **LocalFiles** that is in the same directory as the .jar file.

### Header Files
##### Commands
 + ```read "path"```
   + interprets a game file
   + *path* is the filepath relative to LocalFiles, excluding file extensions
   + For example, to read the file *LocalFiles/Worlds/World 1.dgn*, the command is ```read "Worlds/World 1"```
 + ```import "path"```
   + similar to **read**, but instead interprets another header file
   + *path* follows the same rules as above
 + ```param "path" type value```
   + creates or overwrites a value in the game's registry
   + allows you to set many different parameters for your game, as well as store custom data
   + this is complicated, and we will cover it in more detail later

##### Example
```
param con.setting.echo b false
import "definitions"
read "Worlds/World 1"
read "Player"
```
Line-by-line:  
 + ```param con.setting.echo b false```  
 Set the registry **param**eter at **con.setting.echo** to the **b**oolean value of **false**  
 This particular command prevents the API from displaying warning messages when registry values are overwritten
 + ```import "definitions"```  
 Interprets the header file with filepath LocalFiles/**definitions**.dgnh
 + ```read "Worlds/World 1"```  
 Interprets the game file with filepath LocalFiles/**Worlds/World 1**.dgn
 + ```read "Player"```  
 Interprets the game file with filepath LocalFiles/**Player**.dgn
