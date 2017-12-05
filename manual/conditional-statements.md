# Conditional statements:

## If/else statement:

Syntax: `if (<expression>) <statement/block> [else <statement/block>]`

Example:

```
    let x = true
    if (x) 
        //This code will be executed if condition is true
    else //This code will be executed if condition is false
```

## Switch/case statement:

Syntax: `switch (<expression>) { <case>... }`

Syntax: `case <expression>: <statement/block>`

Executes code in that case which value matches to the value of the switch.

Example:

```
    let x = 10
    switch(x) {
        case 10: {
            //This code will be executed (10 == 10)
        }
        case 20:
            //This code will be not executed (10 != 20)
    }
```



