//SimpleScript v1.9.1.0 REPL example:
require "io"
require "interpreter" as "ss"

while (true) {                  //Loop
    let input = io.input(">: ") //Read
    if (input == "#exit")
        break;
    io.println(ss.eval(input))  //Print->Eval
}