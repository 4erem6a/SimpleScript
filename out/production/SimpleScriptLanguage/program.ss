//SimpleScript v1.7:
//Quick SS implementation of `lists` module:
import require("io").println    //SS v1.7: Field import
require local "lists"
println(lists)
function List() {

    import require("arrays").resize
    import require("arrays").length

    this.array = []             //SS v1.7: this (Function call context)

    this.size = () -> length(this.array)
    this.toArray = () -> this.array

    this.add = function(e) {

        import require("arrays").resize
        import require("arrays").length

        this.array = resize(this.array, length(this.array) + 1)
        this.array[length(this.array) - 1] = e

    }

}

let list = new List()           //SS v1.7: new (Returns function call context)
list.add(1)
list.add(2)
list.add(true)
list.add("msg")

println(list.toArray())