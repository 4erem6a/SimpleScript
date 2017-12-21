//SimpleScript'StandardLibrary: lists
//Version: 1.2
/*Dependencies:
 *  arrays
 *  sequences
 */
require "arrays"

import arrays.length
import arrays.create
import arrays.resize
import arrays.copy

function List(size : number = 0) {

    this.array = (size > 0 ? create(size) : [])

    this.toArray = function() -> this.array
    this.get = function(idx : number) -> this.array[idx]
    this.size = () -> length(this.array)

    this.sequence = function() {

        require "sequences"

        let result = new sequences.Sequence()
        result.list = this
        return result

    }

    this.add = function(value) {

        this.array = resize(this.array, length(this.array) + 1)
        this.array[length(this.array) - 1] = value

    }

    this.addAll = function(array : array) {

        let oldLength = length(this.array)
        this.array = resize(this.array, oldLength + length(array))

        for (let i = 0; i < length(array); i++)
            this.array[i + oldLength] = array[i]

    }

    this.remove = function(value) {

        for (let i = 0; i < length(this.array); i++)
            if (this.array[i] == value) {
                this.removeAt(i)
                break
            }

    }

    this.removeAt = function(idx : number) {

        copy(this.array, idx + 1, this.array, idx, length(this.array) - (idx + 1))
        this.array = resize(this.array, length(this.array) - 1)

    }

    this.insert = function(value, idx : number) {

        let oldLength = length(this.array)
        this.array = resize(this.array, oldLength + 1)
        copy(this.array, idx, this.array, idx + 1, oldLength - idx)
        this.array[idx] = value

    }

}

function fromArray(array : array) {

    let list = new List()

    foreach (let item in array)
        list.add(item)

    return list

}

function of(params args) {

    return fromArray(args)

}

exports {

    List:       ::List,
    fromArray:  ::fromArray,
    of:         ::of

}