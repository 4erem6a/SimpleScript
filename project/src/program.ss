//SimpleScript v1.8:
require "io"
require "lists"
require "sequences"

let list = new lists.List(1, 2, 3, 4)

list.toArray()

io.println(list.sequence().rms())