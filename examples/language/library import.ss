//In ss, libraries can be required by require statement or require expression:
//Both require statement and expression can be divided into 3 types:
//Module: require[(]<string:name>[)]
    //Requires module from ss standard library.
    //Statement/Expression:
    require "io"                    //[Statement]Defines variable by module name
    require "io" as "inputOutput"   //[Statement]Defines variable "inputOutput"
    let io = require "io"           //[Expression]Returns module as an expression result
//Local: require local[(]<string:name>[)]
    //If "EXECUTABLE_DIR" environment variable is set, you can
    //import local .ss files by require local statement/expression.
    //Stament/Expression:
    let module = require local "module" //[Expression]Required file: %EXECURABLE_DIR%/module.ss
    require local "module"              //[Statement]
    require local "module" as "mdl"     //[Statement]
//External: require external[(]<string:path>[)]
    //You can require external .ss files by their path by require external statement/expression.
    //Statement/Expression:
    require @"C:\Users\Default\Desktop\SS\lib.ss" as "lib"  //[Statement]Requires C:\...\lib.ss into lib variable
    let lib = require @"C:\Users\Default\Desktop\SS\lib.ss" //[Expression]