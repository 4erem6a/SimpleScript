//SimpleScript v1.7.10:
require "io"
require "lists"
require "sequences"

import io.println

let list = new lists.List([1, 2, 3, [1, 2, 3]])
let sequence = sequences.fromList(list)


println(sequence.distinct().noMatch((e) -> e is array))