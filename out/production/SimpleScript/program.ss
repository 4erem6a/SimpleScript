//SimpleScript v1.7.10:
require "io"
require "lists"
require "sequences"
require "interpreter" as "$"

import io.println

println(new Constructor())

function Constructor() {

    $.callContext.get().x = 10

}