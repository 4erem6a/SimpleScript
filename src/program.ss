//SimpleScript v1.9 - dev build 2:
//SS v1.9 GOALS:
/*
    Instant functions:  DONE
    Identifiers:        DONE
    Anonymous classes:  DONE
    Named classes:      X
    Class inheritance:  X
    is(instanceof):     DONE
    Constructors:       DONE
    Methods:            DONE
    Fields:             DONE
    Static:             DONE
    CallContext:        X
    Locked members:     X
*/

require "io"

let array = [0, 1, 2, 3, 4, 5]

let Object = class {
    new(x, y) {
        this.x = x
        this.y = y
    }
    f() {
        this.x = 10
    }
}

let obj = new Object(1, 2)

io.println(obj)

obj.f()

io.println(obj is Object)