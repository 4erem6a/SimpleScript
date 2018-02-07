//SimpleScript'StandardLibrary: lists
//Version: 1.6
/*Dependencies:
 *  arrays
 */
class List {
    new(array...) {
        this.array = array
    }

    locked size() -> require("arrays").length(this.array)
    locked toArray() -> this.array
    locked sequence() -> require("sequences").Sequence.fromList(this)

    locked get(index) {
        if (!(index is type("number")))
            return undefined
        let arrays = require("arrays")
        if (index < 0 || index >= arrays.length(this.array))
            return null
        return this.array[index]
    }

    locked set(index, value) {
        if (!(index is type("number")))
            return undefined
        let arrays = require("arrays")
        if (index >= 0 && index < arrays.length(this.array))
            this.array[index] = value
    }

    locked add(value) {
        let arrays = require("arrays")
        let oldLength = arrays.length(this.array)
        this.array = arrays.resize(this.array, oldLength + 1)
        this.array[oldLength] = value
    }

    locked addAll(values...) {
        foreach (let item in values)
            this.add(item)
    }

    locked removeFirst(value) {
        let arrays = require("arrays")
        for (let i = 0; i < arrays.length(this.array); i++) {
            if (this.array[i] == value) {
                this.removeAt(i)
                break
            }
        }
    }

    locked removeLast(value) {
        let arrays = require("arrays")
        for (let i = arrays.length(this.array) - 1; i >= 0; i--) {
            if (this.array[i] == value) {
                this.removeAt(i)
                break
            }
        }
    }

    locked removeAll(value) {
        let arrays = require("arrays")
        for (let i = 0; i < arrays.length(this.array); i++)
            if (this.array[i] == value)
                this.removeAt(i)
    }

    locked removeAt(index) {
        if (!(index is type("number")))
            return undefined
        let arrays = require("arrays")
        if (index < 0 || index >= arrays.length(this.array))
            return null
        let tailLength = arrays.length(this.array) - (index + 1)
        if (tailLength > 0)
            arrays.copy(this.array, index + 1, this.array, index, tailLength)
        this.array = arrays.resize(this.array, arrays.length(this.array) - 1)
    }

    locked insert(index, value) {
        if (!(index is type("number")))
            return undefined
        let arrays = require("arrays")
        if (index < 0 || index >= arrays.length(this.array))
            return null
        this.array = arrays.resize(this.array, arrays.length(this.array) + 1)
        let tailLength = arrays.length(this.array) - (index + 1)
        if (tailLength > 0)
            arrays.copy(this.array, index, this.array, index + 1, tailLength)
        this.array[index] = value
    }

    locked firstIndexOf(value) {
        let arrays = require("arrays")
        for (let i = 0; i < arrays.length(this.array); i++)
            if (this.array[i] == value)
                return i
        return -1
    }

    locked lastIndexOf(value) {
        let arrays = require("arrays")
        for (let i = arrays.length(this.array) - 1; i >= 0; i--)
            if (this.array[i] == value)
                return i
        return -1
    }

    locked sortBy(callback) {
        if (!(callback is type("function")))
            return undefined
        this.array = require("arrays").sortBy(this.array, callback)
    }

    locked sort(callback) {
        if (!(callback is type("function")))
            return undefined
        this.array = require("arrays").sort(this.array, callback)
    }

    locked first() -> (this.size() >= 1 ? this.get(0) : null)

    locked firstOrDefault(default) -> (this.size() >= 1 ? this.get(0) : default)

}

exports {
    List: List
}