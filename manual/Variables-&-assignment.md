# Variables & assignment:
## Variables:
In SS, you can define variables and constatns  
to store different values.  
Syntax: `let <word> [= <expression>]`
Syntax: `let const <word> = <expression>`
Example:
```
    let x               //x = null
    let y = 10          //y = 10
    let const z = #ff   //z = 255
    let const other     //error
```
You can also use `let` as an expression that returns value of defined variable.  
Example:
```
    let x = let y = let z = true        //x = true, y = true, z = true
    //here is how it works:
    let x = (let y = (let z = (true)))  //this statement is correct
```
## Assignment:
You can change value of the variable through assignment:  
Syntax: `<accessible> = <expression>`
Example:
```
    let x = 10          //x = 10
    let const y = 20    //y = 20
    x = true            //x = true
    y = null            //error
```
Since assignment is an expression too,  
you can assign value to a few variables in a row:  
Example:
```
    let x = let y = let z       //x, y, z = null
    x = y = z = true            //x, y, z = true
    x = (y = (z = (false)))     //x, y, z = false
```