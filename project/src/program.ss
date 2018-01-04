//SimpleScript v1.8.3:
require "io"
require "utils"

let $ = utils.hashCode

function a(x...) -> 10
function b(x) -> 10

io.println($(::a) == $(::b))