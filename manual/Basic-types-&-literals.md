# Basic types & literals:
## Types:
There are 7 basic data-types in ss:
```
    //Basic types:
    Null        //Nothing
    Number      //Stores any number values
    String      //Stores string values
    Boolean     //Stores true/false values
    //Advanced types:
    Array       //Stores arrays
    Function    //Stores functions
    Map         //Stores maps | associative arrays | tables | dictionaries
```
You cannot define your own data-type, but you can  
use maps as objects (as like as in JavaSctipt).
## Basic literals:
To define values you need to use literals,  
here are examples of basic ss literals:  
```
    |    Type:    |    Example:    |    RegExp:    |
    > Null:         null            null
    > Number:       123             [0-9]+
                    3.14            [0-9]+\.[0-9]+
                    #ff1            #[a-fA-F0-9]+
                    0x111010        0x[0-1]+
    > String:       'hello, world!' '[\w\s]*'
                    "hello, world!" "[\w\s]*"
                    `hello, world!` `[\w\s]*`
    > Boolean:      true            true
                    false           false
```
Literals of advanced types will be discussed later.