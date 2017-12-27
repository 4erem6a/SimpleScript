//SimpleScript'StandardLibrary: lists
//Version: 1.3
/*Dependencies:
 *  arrays
 */
function List(array : array = []) {

    this.array = array

    this.size = () -> require("arrays").length(this.array)
    this.toArray = () -> this.array
    this.sequence = () -> require("sequences").fromList(this)

    this.get = function(index : number) {
        require "arrays"
        if (index < 0 || index >= arrays.length(this.array))
            return null
        return this.array[index]
    }

    this.set = function(index : number, value) {
        require "arrays"
        if (index >= 0 && index < arrays.length(this.array))
            this.array[index] = value
    }

    this.add = function(value) {
        require "arrays"
        let oldLength = arrays.length(this.array)
        this.array = arrays.resize(this.array, oldLength + 1)
        this.array[oldLength] = value
    }

    this.addAll = function(values : array) {
        foreach (let item in values)
            this.add(item)
    }

    this.removeFirst = function(value) {
        require "arrays"
        for (let i = 0; i < arrays.length(this.array); i++) {
            if (this.array[i] == value) {
                this.removeAt(i)
                break
            }
        }
    }

    this.removeLast = function(value) {
        require "arrays"
        for (let i = arrays.length(this.array) - 1; i >= 0; i--) {
            if (this.array[i] == value) {
                this.removeAt(i)
                break
            }
        }
    }

    this.removeAll = function(value) {
        require "arrays"
        for (let i = 0; i < arrays.length(this.array); i++)
            if (this.array[i] == value)
                this.removeAt(i)
    }

    this.removeAt = function(index : number) {
        require "arrays"
        if (index < 0 || index >= arrays.length(this.array))
            return null
        let tailLength = arrays.length(this.array) - (index + 1)
        if (tailLength > 0)
            arrays.copy(this.array, index + 1, this.array, index, tailLength)
        this.array = arrays.resize(this.array, arrays.length(this.array) - 1)
    }

    this.insert = function(index : number, value) {
        require "arrays"
        if (index < 0 || index >= arrays.length(this.array))
            return null
        this.array = arrays.resize(this.array, arrays.length(this.array) + 1)
        let tailLength = arrays.length(this.array) - (index + 1)
        if (tailLength > 0)
            arrays.copy(this.array, index, this.array, index + 1, tailLength)
        this.array[index] = value
    }

    this.firstIndexOf = function(value) {
        require "arrays"
        for (let i = 0; i < arrays.length(this.array); i++)
            if (this.array[i] == value)
                return i
        return -1
    }

    this.lastIndexOf = function(value) {
        require "arrays"
        for (let i = arrays.length(this.array) - 1; i >= 0; i--)
            if (this.array[i] == value)
                return i
        return -1
    }

    this.first = function() {
        return (this.size() >= 1 ? this.get(0) : null)
    }

    this.firstOrDefault = function(default) {
        return (this.size() >= 1 ? this.get(0) : default)
    }

}

function of(params items) {
    return new List(items)
}

exports {
    List: ::List,
    of:   ::of
}