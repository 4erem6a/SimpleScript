# SS Language features:

## Comments:

```
//Sigle-line comment
/* Multi-Line comment */
```

## Variables:

```
//let <name> [= <expression>]
//let const <name> = <expression>

let x
let y = 10
let const Pi = 3.14
```

## Conditional statements:

```
//If statement:
//if (<expression>) <stmt/block> [else <stmt/block>]
if (x == 10)
    io.println(x)
else
    io.println(-x)

if (y <= 21) {
    //...
} else if (/**/) //...

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

## Loops:

```
//While/doWhile loop:
//while (<expression>) <stmt/block>
//do <stmt/block> while (<expression>)
while (true) {
    //...
    break
}
//For loop:
//for ([expressio
```



