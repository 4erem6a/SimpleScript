//SimpleScript v1.7:
require "io"
require "lists"
import io.println

let list = new lists.List()

list.add(1)
list.add(2)
list.add(true)
list.insert("msg", 2)
list.remove(2)

println(list.toArray())