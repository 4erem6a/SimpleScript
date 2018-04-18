# SimpleScript
## Description:
SimpleScript is a general-purpose scripting programming language created as hobby project.
SS suports imperative, functional & object-oriented programmig paradigms.
## Latest changelog: (1.12.1.0)
* Added `|>` operator.
* Added `!is` operator.
* Added `&&=` and `||=` operators.
* Added named anonymous functions and classes.
* Removed `import` statement.
* Removed `collection` module.
* Added `sequence` module.
* Added `stringf` module.
* Added `;` support between `if` and `else`.
* Fixed string interpolation.
* Fixed `jcanvas` module.
* Fixed map value cloning.
* Added `SelfReferencedAssignmentException`.
## Roadmap:
* Optimization.
* JAR module loading.
* SS modules.
## Installation:
1. Install Java 8+.
2. Download latest version: [releases](https://github.com/4erem6a/SimpleScript/releases).
3. Extract SimpleScript.jar manually or use an installer.
## Usage:
You can run SS code from command line.
Use `ss.cmd` script or run `SimpleScript.jar` manually.
List of supported flags:
```
    -f <file>       Specifies source file
    -s <source>     Specifies source
    -r              Compiles program to SimpleScript
    -e              Executes program
    -mp <path(s)>   Specifies folder(s) to load modules from
    -m <mode>       Specifies execution mode:
        program (Default)   Executes source as a sequence of staements
        main                Executes `main(args)` function
        expression          Evaluates source as an expression and prints result
    -c <charset>    Specifies source charset (UTF-8 by default)
    -l              Enables log
    -d              Debug mode
    -li             Enables linter (Static code analyzer)
    -a <args...>    Specifies program args
```
## Hello, world!
Code:
```js
let const io = require("io") //Require "io" module from standard library.
io.println("Hello, world!")  //Print line "Hello, world" to STDOUT.
```
Command: `ss -f program.ss -e -li`  
Result:
```js
CommandLine> ss -f C:\SS\program.ss -e -li
Hello, world!
```
## Expression mode:
With `-m expression` flag, you can evaluate your code as an expresson.
Example:
```js
CommandLine> ss -s "(2 + 2) * 2" -e -m expression -li
>8.00000
```
## Syntax highlighting:
[VS Code highlighting plugin](https://marketplace.visualstudio.com/items?itemName=4erem6a.ss)(Updated to 1.2.1 for SS v1.9.3+)
## Documentation:
[Coming Soon]()