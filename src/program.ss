//SimpleScript v1.9
class Point {
    x = 0
    y = 0
    new(x, y) {
        this.setCords(x, y)
    }
    getCords() {
        let x = this.x
        let y = this.y
        return {x: x, y: y}
    }
    setCords(x, y) {
        this.x = x
        this.y = y
    }
    static locked distance(a, b) {
        require "math"
        return math.sqrt(math.pow(a.x - b.x, 2) + math.pow(a.y - b.y, 2))
    }
}

class Poly {
    points = new require("lists").List()
    new(points...) {
        foreach (let point in points)
            this.points.add(point)
    }
    addPoint(point) {
        this.points.add(point)
    }
    clear() this.points = new require("lists").List()
}

require "io"
import io.println

let a = new Point(1, 3)
let b = new Point(3, 3)

let poly = new Poly(a, b)

println(poly.points.toArray())

class Sample {
    x
    y
    z
}

let s = new Sample()

s.x = 1
s.y = 2
s.z = 3

println(`{s.x} {s.y} {s.z}`)