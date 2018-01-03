//SimpleScript v1.8.1:
require "io"
require "lists"
require "sequences"
require "functions"

let sequence = (new lists.List(1, 2, 4, 6, 1, 3, 6, 2, 7, -1, -6, 17)).sequence()

io.println(sequence.sort(functions.NEG_COMPARISON).toArray())