import <io>
import <utils>

let object = {}

let key = (a, b) -> a + b

object.(key) = 10

io.println(object.((a, b) -> a + b))