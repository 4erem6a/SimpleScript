# SimpleScript
## Desсription:
SimpleScript is a scripting programming language created as hobby project.
## Installation:
1. Install Java 8+.
2. Download latest version: [1.8.4.4](https://github.com/4erem6a/SimpleScript/releases/tag/1.8.4.4)
3. Extract SimpleScript.jar
## Usage:
You can run SS code from command line.
Use `java -jar %SimpleScript.jar path%` command with following flags:
```
    -f <file>       Specifies source file
    -s <source>     Specifies source
    -r              Compiles program to SimpleScript
    -e              Executes program
    -m <mode>       Specifies execution mode:
        file (Default)  Executes source from first line
        main            Executes `main(args)` function
        expression      Executes source as an expression and prints result
    -c <charset>    Specifies source charset (UTF-8 by default)
    -l              Enables log
    -d              Debug mode
    -li             Enables linter (Static code analyzer)
    -a <args...>    Specifies program args
```
## Hello, world!
Code:
```
require "io"                //Require "io" module from standard library.
io.println("Hello, world!") //Print line "Hello, world" to command line.
```
For example, `SimpleScript.jar` and `program.ss` are located in `C:\SS`
Command: `java -jar C:\SS\SimpleScript.jar -f C:\SS\program.ss -e -li`
Result:
```
CommandLine> java -jar C:\SS\SimpleScript.jar -f C:\SS\program.ss -e -li
Hello, world!
```
## Expression mode:
With `-m expression` flag, you can evaluate your code as an expresson.
Example:
```
CommandLine> java -jar C:\SS\SimpleScript.jar -s "(2 + 2) * 2" -e -m expression -li
>8.00000
```
## Syntax highlighting:
[VS Code highlighting plugin](https://marketplace.visualstudio.com/items?itemName=4erem6a.ss)
## Documentation:
[Coming Soon]()