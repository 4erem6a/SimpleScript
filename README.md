# SimpleScript
## Description:
SimpleScript is a general-purpose scripting programming language created as hobby project.
SS suports imperative, functional & object-oriented programmig paradigms.
## Latest changelog: (1.12.1.0)
* Added `|>` (pipeline) operator
* Added `!is` (is not) operator
* Added `&&=` (augmented assignment (boolean and)) operator
* Added `||=` (augmented assignment (boolean or)) operator
* Added named anonymous functions and classes
* Removed `import` statement
* Removed `collection` module
* Added `sequence` module
* Added `stringf` module
* Added `;` support between `if` and `else`
* Fixed string interpolation
* Fixed `jcanvas` module
* Fixed map value cloning
* Added `SelfReferencedAssignmentException`
* Added self referenced assignment checking
## Roadmap:
* Optimization
* JAR module loading
* Modules
## Requirements:
+ Java 8+
## Examples:
Hello world!:  
```js
    const io = require('io');
    io.println('Hello, world!');
    //OUTPUT: Hello world!
```
Factorial:  
```js
    const io = require('io');
    io.println(factorial(10));
    function factorial(n)
        -> n == 1 ? n : n * factorial(n - 1);
    //OUTPUT: 3628800
```
Person:  
```js
    const io = require('io');
    class Person(name, age = 0);
    io.println(new Person('Sample', 21));
    //OUTPUT: {name:Sample,age:21.0}
```
## Usage:
You can run SS programs through command line using `ss.cmd` (included in release archive) or `java -jar SimpleScript.jar`.
You can use following flags to specify source or configure interpreter itself:
```
    -f <file>       Specifies source file
    -s <source>     Specifies source
    -r              Translates AST back to SimpleScript and prints the result
    -e              Executes program
    -mp <path(s)>   Specifies folder(s) to load modules from
                    You can specify several folders by separating them with ';' (semicolon)
                    Example:    ./foler1;./folder2
    -m <mode>       Specifies execution mode, can be one of theese values:
        program/p   (Default)   Executes source as a sequence of staements
        main/m                  Executes `main(args)` function (must be top-level)
        expression/e            Evaluates source as an expression and prints result
    -c <charset>    Specifies source charset (UTF-8 by default)
    -l              Enables log
    -d              Debug mode  (Prints java stack trace if an exception occurs)
    -li             Enables linter (Static source code analyzer)
    -a <args...>    Specifies program args
```
## Expression mode:
With `-m expression` or `-m e` flag enabled, you can evaluate your code as an expresson.
Example:
```js
    CommandLine>: ss -s "(2 + 2) * 2" -e -m expression -li
    > 8.00000
```
## ss.cmd:
Usage of `ss.cmd` instead of `java` gives you following benefits:
* `ss <file>` or `ss run <file>` to execute file  
    * This also means that you can execute source files by dragging
* `ss eval <expression>` to evaluate expression
* `ss version` to display interpreter version installed
    * Same as `ss eval "require('interpreter').version"
* `ss module <command>` to word with `/modules/` directory in SimpleScript folder:
    * `ss module list` to display list of existing modules
    * `ss module exists <name>` to check module existance
    * `ss module remove <name>` to remove specified module
    * `ss module install <file>` to copy specified file into modules directory
## Syntax highlighting:
[VS Code highlighting plugin](https://marketplace.visualstudio.com/items?itemName=4erem6a.ss)
## Documentation:
[Coming Soon]()