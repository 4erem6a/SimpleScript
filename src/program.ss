//SimpleScript v1.9.1:
require "async"
require "io"
require "errors"
require "lists"
require "sequences"
require "environment" as "env"

let list = new lists.List(1, 2, 3, 4)

io.println(env.variables().CURRENT_LANG_VERSION)

let obj = new Object()

class Object {}