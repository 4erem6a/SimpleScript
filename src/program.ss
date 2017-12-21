//SimpleScript v1.7.2:
require "io"
require "lists"

import lists.of

io.println(of(1, 2, 3, 4).sequence().filter((e) -> e % 2 == 0).array())

io.println(of([1, 2], [3, 4]).sequence().flatMap((e) -> [e[0], e[1]]).array())

of(1, 2, 3, 4).sequence().map((e) -> ++e).forEach(io.println)