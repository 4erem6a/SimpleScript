//SimpleScript'StandardLibrary: lists
//Version: 1.4
/*Dependencies:
 *  arrays
 */
locked function List(array...) {

    this.array = array

    this.size = locked function() -> require("arrays").length(this.array)
    this.toArray = locked function() -> this.array
    this.sequence = locked function() -> require("sequences").fromList(this)

    this.get = locked function(index : number) {
        require "arrays"
        if (index < 0 || index >= arrays.length(this.array))
            return null
        return this.array[index]
    }

    this.set = locked function(index : number, value) {
        require "arrays"
        if (index >= 0 && index < arrays.length(this.array))
            this.array[index] = value
    }

    this.add = locked function(value) {
        require "arrays"
        let oldLength = arrays.length(this.array)
        this.array = arrays.resize(this.array, oldLength + 1)
        this.array[oldLength] = value
    }

    this.addAll = locked function(values : array) {
        foreach (let item in values)
            this.add(item)
    }

    this.removeFirst = locked function(value) {
        require "arrays"
        for (let i = 0; i < arrays.length(this.array); i++) {
            if (this.array[i] == value) {
                this.removeAt(i)
                break
            }
        }
    }

    this.removeLast = locked function(value) {
        require "arrays"
        for (let i = arrays.length(this.array) - 1; i >= 0; i--) {
            if (this.array[i] == value) {
                this.removeAt(i)
                break
            }
        }
    }

    this.removeAll = locked function(value) {
        require "arrays"
        for (let i = 0; i < arrays.length(this.array); i++)
            if (this.array[i] == value)
                this.removeAt(i)
    }

    this.removeAt = locked function(index : number) {
        require "arrays"
        if (index < 0 || index >= arrays.length(this.array))
            return null
        let tailLength = arrays.length(this.array) - (index + 1)
        if (tailLength > 0)
            arrays.copy(this.array, index + 1, this.array, index, tailLength)
        this.array = arrays.resize(this.array, arrays.length(this.array) - 1)
    }

    this.insert = locked function(index : number, value) {
        require "arrays"
        if (index < 0 || index >= arrays.length(this.array))
            return null
        this.array = arrays.resize(this.array, arrays.length(this.array) + 1)
        let tailLength = arrays.length(this.array) - (index + 1)
        if (tailLength > 0)
            arrays.copy(this.array, index, this.array, index + 1, tailLength)
        this.array[index] = value
    }

    this.firstIndexOf = locked function(value) {
        require "arrays"
        for (let i = 0; i < arrays.length(this.array); i++)
            if (this.array[i] == value)
                return i
        return -1
    }

    this.lastIndexOf = locked function(value) {
        require "arrays"
        for (let i = arrays.length(this.array) - 1; i >= 0; i--)
            if (this.array[i] == value)
                return i
        return -1
    }

    this.first = locked function() -> (this.size() >= 1 ? this.get(0) : null)

    this.firstOrDefault = locked function(default) -> (this.size() >= 1 ? this.get(0) : default)

}

exports {
    List: ::List
}