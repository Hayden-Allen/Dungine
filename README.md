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


### Game Files
##### Basics
Every Dungine object is made up of 3 distinct types: *attributes*, *lists*, and other *objects*. Here is some info about each. 
+ *Attributes*
   + the most basic data type, being only a key and a primitive value (int, String, boolean, char)
+ *Lists* 
   + store a variable amount of 1 of the 3 data types
   + there are currently no lists in Dungine that store *attributes*
+ *Objects*
   + the most complex data type, storing a set number of all 3 types
   + during interpretation, if a definition for a particular element is not provided, the created object will instead have the *default* value for that element. We will talk more about this later
##### Syntax
There are basic syntax rules for each type:  
+ each consists of a key (case-sensitive String with no whitespace)
+ keys and values may be separated by a **:** (colon). For *attributes* this is mandatory
+ *objects* and *lists* may start and end with either **{}** or **[]**
   + by convention, I use **{}** for objects and **[]** for lists
+ data types may be separated by a **,** (comma)

The most important thing to realize about game file syntax is that **all non-String whitespace is removed before interpretation**. This means that, if you want your Dungine code to be compact, you must use **,** and **:** wherever you can.

##### Object Example
```
player {
 visual {
  x: 1
  y: 0
  symbol: $
 }
 stats {
  name: "Player 1"
  atk: 5
  def: 5
  spd: 5
 }
 hp: 5
 gold: 100
}
```  
This takes up more space than it needs because it doesn't use commas to separate values. Let's write it more compactly.
```
player{visual{x:1,y:0,symbol:$},stats{name:"Player 1",atk:5,def:5,spd:5},hp:5,gold:100}
```
This might be a bit too compact, but it is easy to see how making syntactical decisions to fit your preferences allow you to format your Dungine code in many ways while providing the same end result.

That being said, let's do a line-by-line of this **player** definition:  
+ ```player {```
   + opens a **player** object
   + this only works because **player** is a [TLO](https://github.com/Hayden-Allen/Dungine/blob/master/README.md#top-level-objects "Top Level Objects")
+ ```visual {```
   + opens a **visual** object. This object works a bit differently for **player**s than other objects, which we'll talk about later
+ ```x: 1, y: 0, symbol: $```
   + this **player** starts at the 2nd room in the 1st row of the world, and is displayed with the '$' character
+ ```}```
   + close **visual** object
+ ```stats {```
   + opens a **stat** object, which stores generic information and is used in many different kinds of objects
+ ```name: "Player 1", atk: 5, def: 5, spd: 5```
   + this **player**'s name is "Player 1". Note the use of quotations to delimit the String and the ability to include whitespace
   + this **player** has base attack, defense, and speed stats of 5. We'll talk about what these mean in detail later
+ ```}```
   + close **stat** object
+ ```hp: 5, gold: 100```
   + this **player** has a base health of 5 and starts with 100 gold pieces
   + the name of the currency in-game can be customized through the registry, but you can't change the name of the Dungine attribute
   
While this example doesn't show any lists, I'm sure you can imagine their syntax. Regardless, I will show a more in-depth example later on that will include lists.

##### Top Level Objects
The main restriction on Dungine game file syntax is the fact that only certain types of objects may be defined outside of any other object. These are called Top Level Objects, or TLOs. There are currently just 2 TLOs in Dungine, **player** and **world**.  

##### TLO Trees
Here are the object trees for **player** and **world**.  
Keys are in **bold** and default values are *italicized*.

+ **player**
   + **stats**
      + **name** : *NONAME*
