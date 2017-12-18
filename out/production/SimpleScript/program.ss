//SimpleScript v1.7.2:
require "io"
require "lists"
require "sequences"

let seq = lists.of(1, 2, 3, 4, 5).sequence()

io.println(seq.map((e) -> (e % 2 == 0)).add(6, 7, 8, 9).array())