//SimpleScript:
require "io"
require local "module"

function say(msg : string)
    -> io.println(msg)

say("Hello, world!")