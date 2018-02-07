# SS Language features:
## Misc features:
### Comments:
```
    //Sigle-line comment
    /* Multi-Line comment */
```
### Semicolons:
```
    //Semicolons are supported but unnecessary
    //You can use semicolons along with > operator to avoid syntactic ambiguity
    //Every statement in a block can be ended with semicolon
    //Examples:
    while (true) {
        doSomething();
    };                  //Valid
    for (;;) doSomething(); //Valid
    if (true) doSomething(); else doSomethingElse(); //Invalid
    //                     ^ Semicolons can be placed only in the end of the statement
    
```
## Literals & value types:
### Numbers:
```
    //In SS, all numbers are stored as doubles
    //Number definition:
    100         //Integer value
    100.1       //Floating point value
    #FF00FF     //Hexademical value
    0x101010    //Binary value
    nan         //NaN value
    //You can separate digits in decimal numbers with _:
    1_000_000
    1.999_999_999
```
### Const types:
```
    //Booleans:
    true
    false
    //Null:
    null
    //Undefined:
    undefined
```
### Arrays:
```
    //Defining array:
    []
    [<expressiom>]
    //Accessing array:
    array[0]
    array[1]
    array[0][1]
```
### Maps:
```
    //Defining map:
    {
        
    }
```
## Control structures:
### Variables:
```
    //let <name> [= <expression>]
    //let const <name> = <expression>
    let x
    let y = 10
    let const Pi = 3.14     //Constant must be initialized
```
### Conditional statements:
```
    //If statement:
    //if (<expression>) <stmt/block> [else <stmt/block>]
    if (x == 10)
        //...
    else
        //...

    if (y <= 21) {
        //...
    } else {
        //...
    }

    //Switch statement:
    //switch (<expression>) { [<case>...] }
    //case <expression>: <stmt/block>
    switch (x) {
        case 10:
            return 20
            break
        case 20:
            return 30
            break
}
```
### Loops:
```
    //While/doWhile loop:
    //while (<expression>) <stmt/block>
    //do <stmt/block> while (<expression>)
    while (true) {
        //...
        break
    }
    do {
        //...
    } while (true)
    //For loop:
    //for ([stmt...];[expression];[stmt...]) <stmt/block>
    for (let x = 0; x < 10; x++) //...
    for (;;) { /*...*/ }
    for (let i = 0, let j = 10; i < 10 && j > 0; i++, j--) {
        //...
    }
    //Foreach loop:
    //foreach (<let> in <expession>) <stmt/block>
    foreach (let item in [1, 2, 3, 4]) {
        //...
    }
```
###