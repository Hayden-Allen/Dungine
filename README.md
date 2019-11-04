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
   + there are currently no *lists* in Dungine that store *attributes*
+ *Objects*
   + the most complex data type, storing a set number of all 3 types
   + during interpretation, if a definition for a particular element is not provided, the created object will instead have the *default* value for that element. We will talk more about this [later](https://github.com/Hayden-Allen/Dungine/blob/master/README.md#tlo-trees "TLO Trees")
##### Syntax
There are basic syntax rules for each type:  
+ each consists of a key (case-sensitive String with no whitespace)
+ keys and values may be separated by a **:** (colon). For *attributes* this is mandatory
+ *objects* and *lists* must start and end with either **{}** or **[]**. By convention, I use **{}** for *objects* and **[]** for *lists*
+ data types may be separated by a **,** (comma)

The most important thing to realize about game file syntax is that **all non-String whitespace (tabs and spaces) is removed before interpretation**. This means that, if you want your Dungine code to be compact, you must use **,** and **:** wherever you can.

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
This takes up more space than it needs to because it doesn't use commas to separate values. Let's write it more compactly.
```
player{visual{x:1,y:0,symbol:$},stats{name:"Player 1",atk:5,def:5,spd:5},hp:5,gold:100}
```
This might be a bit too compact, but it is easy to see how making syntactical decisions to fit your preferences allows you to format your Dungine code in many ways while providing the same end result.

Let's do a line-by-line of this **player** definition:  
+ ```player {```
   + opens a **player** object
   + this only works because **player** is a [TLO](https://github.com/Hayden-Allen/Dungine/blob/master/README.md#top-level-objects "Top Level Objects")
+ ```visual {```
   + opens a **visual** object. This object works a bit differently for **player**s than for other objects, which we'll talk about [later](https://github.com/Hayden-Allen/Dungine/blob/master/README.md#player-tlo-tree-breakdown "Player TLO Tree Breakdown")
+ ```x: 1, y: 0, symbol: $```
   + this **player** starts at the 2nd room in the 1st row of the world, and is displayed with the '$' character
+ ```}```
   + close **visual** object
+ ```stats {```
   + opens a **stat** object, which stores generic information and is used in many different kinds of objects
+ ```name: "Player 1", atk: 5, def: 5, spd: 5```
   + this **player**'s name is "Player 1". Note the use of quotations to delimit the String and the ability to include whitespace
   + this **player** has base attack, defense, and speed stats of 5. We'll talk about what these mean in detail [later](https://github.com/Hayden-Allen/Dungine/blob/master/README.md#player-tlo-tree-breakdown "Player TLO Tree Breakdown")
+ ```}```
   + close **stat** object
+ ```hp: 5, gold: 100```
   + this **player** has a base health of 5 and starts with 100 gold pieces
   + the name of the currency in-game can be customized through the registry, but you can't change the name of the Dungine attribute
   
While this example doesn't show any lists, I'm sure you can imagine their syntax. Regardless, I will show a more in-depth example later on that will include lists.

##### Top Level Objects
The main restriction on Dungine game file syntax is the fact that only certain types of objects may be defined outside of any other object. These are called top level objects, or TLOs. There are currently just 2 TLOs in Dungine, **player** and **world**.  

##### TLO Trees
Here are the object trees for **player** and **world**.  
Keys are in **bold** and default values are *italicized*.  
All possible values that a list may contain are shown, but the default is an empty list.

##### Player TLO Tree
+ **player**
   + **hp** : *0*
   + **maxhp**: *5*
   + **gold** : *0*
   + **stats**
      + **name** : *NONAME*
      + **atk** : *0*
      + **def** : *0*
      + **spd** : *0*
   + **visual**
      + **x** : *0*
      + **y** : *0*
      + **symbol** : *X*
   + **inventory**
      + **size** : *5*
      + **items** : *[]*
         + **weapon**
            + **stats**
               + **name** : *NONAME*
               + **atk** : *0*
               + **def** : *0*
               + **spd** : *0*
            + **desc** : *NODESC*
            + **rarity** : *0*
            + **value** : *0*
         + **armor**
            + **stats**
               + **name** : *NONAME*
               + **atk** : *0*
               + **def** : *0*
               + **spd** : *0*
            + **desc** : *NODESC*
            + **rarity** : *0*
            + **value** : *0*
            + **floor** : *0*
         + **consumable**
            + **stats**
               + **name** : *NONAME*
               + **atk** : *0*
               + **def** : *0*
               + **spd** : *0*
            + **desc** : *NODESC*
            + **rarity** : *0*
            + **value** : *0*
            + **duration** : *1*
            + **hp** : *0*
            + **self** : *false*

#### Player TLO Tree Breakdown
For brevity, I will use **character** to mean either **player** or **enemy**, **item** to mean either **weapon**, **armor**, or **consumable**, and **room object** to mean **enemies** and **gold chests** (shown in-depth [here](https://github.com/Hayden-Allen/Dungine/blob/master/README.md#world-tlo-tree-breakdown "World TLO Tree Breakdown")).

**stats**
+ **name**
   + String name of a **character** or **item**
+ **atk**
   + attack stat used in damage calculations
   + a **character**'s equipped **item**'s values are added to their own during those calculations
   + so if a **player** has an **atk** of 10 and their equipped **weapon** has an **atk** of 2 and their equipped **armor** has an **atk** of 1, the resulting **atk** used for calculation is 10 + 2 + 1 = 13.
+ **def**
   + defense stat used in damage calculations
   + same properties as **atk**
+ **spd**
   + speed stat used to determine the turn order in a fight
   + similar to **atk** and **def** in that a **character**'s equipped **item**'s values are added to its own

**items**
+ **desc**
   + String description of an **item**
+ **value**
   + the monetary value of an **item**
   + used in calculating a **character**'s net worth
+ **rarity**
   + number representing how rare an **item** is
   + intended for use in determining both **value** and price at shops (*not implemented yet*)
+ **floor**
   + maximum amount of negative damage that a **character** with this **armor** equipped can receive
   + if an **enemy** has 2 atk and a **player** has 10 def, then the resulting calculation is 2 - 10 = -8 damage
   + if that **player**'s armor has a **floor** of 3, then that -8 damage turns into -3 damage for a result of +3 hp to the **player**
   + it is important to realize that this is the *magnitude* of the max negative damage
   + positive values will result in the wearer being healed if a small enough amount of damage is done
   + zero will result in no healing
   + negative values will result in the wearer always being damaged, even if the attacker's **atk** is less than the wearer's **def**
+ **hp**
   + number of hitpoints
   + for **character**s this is the current amount of hit points they have
   + for **consumable**s this is the number of hit points they restore to the target (*not implemented yet*)
+ **duration**
   + number of turns that a **consumable**'s effects last for
   + *not implemented yet*
+ **self**
   + whether or not a **consumable** targets the user
   + *not implemented yet*
+ **weapon**
   + has **stats**, **rarity**, **value** and **desc**
   + **character**s can equip one at a time
+ **armor**
   + has **stats**, **rarity**, **value**, **desc**, and **floor**
   + **character**s can equip one at a time
+ **consumable**
   + has **stats**, **rarity**, **value**, **hp**, **duration**, and **self**
   + not equippable
   + *not implemented yet*
   
**inventory**  
+ **size**
   + number of **item**s that an **inventory** can store
+ **items**
   + list containing a variable amount of **weapon**s, **armor**s and **consumable**s
+ **inventory**
   + has a **size** and **items**
   + the size of **items** must be less than or equal to **size**
   
**visual**  
+ **x**
   + for **enemies** and other **room objects** this represents the x coordinate within a room that an object is located
   + for **player**s this is the world x coordinate of the room that the **player** is in
+ **y**
   + for **room objects** this represents the y coordinate within a room that an object is located
   + for **player**s this is the world y coordinate of the room that the **player** is in
+ **symbol**
   + single character used to display an object
   + for **room objects** this will be displayed in the room at the given **x** and **y** coordinates
   + for **player**s, this will always be displayed in the center of the room that they are currently in

**player**
+ **maxhp**
   + maximum number of hitpoints that a **character** can have
+ **gold**
   + amount of money a **character** has
+ **player**
   + has **stats**, **visual**, **hp**, **maxhp**, **gold**, and **inventory**

##### World TLO Tree
+ **world**
   + **name** : *NONAME*
   + **rooms** : *[]*
      + **row** : *[]*
         + **room**
            + **doors** : *0*
            + **text**
               + **onenter** : ** (empty)
               + **onexit** : ** (empty)
            + **objects** : *[]*
               + **gchest**
                  + **gold** : *0*
                  + **visual**
                     + **x** : *0*
                     + **y** : *0*
                     + **symbol** : *X*
               + **enemy**
                  + **hp** : *0*
                  + **maxhp** : *5*
                  + **gold** : *0*
                  + **dcgold** : *0*
                  + **dcarmor** : *0*
                  + **dcweapon** : *0*
                  + **visual**
                     + **x** : *0*
                     + **y** : *0*
                     + **symbol** : *X*
                  + **stats**
                     + **name** : *NONAME*
                     + **atk** : *0*
                     + **def** : *0*
                     + **spd** : *0*
                  + **inventory**
                     + **size** : *5*
                     + **items** : *[]*
                        + **weapon**
                           + **stats**
                              + **name** : *NONAME*
                              + **atk** : *0*
                              + **def** : *0*
                              + **spd** : *0*
                           + **desc** : *NODESC*
                           + **rarity** : *0*
                           + **value** : *0*
                        + **armor**
                           + **stats**
                              + **name** : *NONAME*
                              + **atk** : *0*
                              + **def** : *0*
                              + **spd** : *0*
                           + **desc** : *NODESC*
                           + **rarity** : *0*
                           + **value** : *0*
                           + **floor** : *0*
                        + **consumable**
                           + **stats**
                              + **name** : *NONAME*
                              + **atk** : *0*
                              + **def** : *0*
                              + **spd** : *0*
                           + **desc** : *NODESC*
                           + **rarity** : *0*
                           + **value** : *0*
                           + **duration** : *1*
                           + **hp** : *0*
                           + **self** : *false*
            
##### World TLO Tree Breakdown
Elements that require no further explantation are not listed here. Explanations of **gold**, **stats**, **hp**, **maxhp**, **inventory**, **visual** can be found [above](https://github.com/Hayden-Allen/Dungine/blob/master/README.md#player-tlo-tree-breakdown "Player TLO Tree Breakdown").

**gchest**
+ object containing a given amount of gold
+ the **player** can interact with this to take the gold
+ when the gold has been taken, this object deletes itself

**enemy**
+ **dcgold**
   + value from 0 to 100
   + the chance (out of 100) that this **enemy** will drop its gold upon death
+ **dcweapon**
   + value from 0 to 100
   + the chance (out of 100) that this **enemy** will drop its equipped **weapon** upon death
+ **dcarmor**
   + value from 0 to 100
   + the chance (out of 100) that this **enemy** will drop its equipped **armor** upon death

**room**
   + **doors**
      + this value determines from which directions the **player** can enter and exit the room
      + a 4-bit value representing the state of the four doors of the room
      + 8 (1000) is top door, 4 (0100) is left door, 2 (0010) is bottom door, and 1 (0001) is right door
      + 15 (1111) is all open and 0 (0000) is all closed
      + to write in binary, preface with "0b"
      + for example, to have top and bottom doors open: ```doors: 0b1010```
   + **text**
      + **onenter**
         + text displayed when the **player** enters this room
         + nothing will display if empty (which is also the default value)
       + **onexit**
         + text displayed when the **player** exits this room
         + nothing will display if empty (which is also the default value)
   + **objects**
      + list of **gchest**s and **enemies**

**row**
   + contains a variable number of **room**s
**rooms**
   + contains a variable number of **row**s
