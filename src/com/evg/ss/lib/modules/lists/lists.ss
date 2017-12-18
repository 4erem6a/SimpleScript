//SimpleScript'StdLibrary: lists
//Implemented in SS v1.7.2:
//Version: 1.1
function List(size : number = 0) {                  //List constructor

    require "arrays"
    import arrays.length
    import arrays.create

    this.array = (size > 0 ? create(size) : [])

    this.toArray = function() -> this.array
    this.get = function(idx : number) -> this.array[idx]
    this.size = () -> require("arrays").length(this.array)

    this.sequence = function() {

        require "sequences"

        let result = new sequences.Sequence()
        result.list = this
        return result

    }

    this.add = function(value) {                    //Add element to the list

        require "arrays"
        import arrays.length
        import arrays.resize

        this.array = resize(this.array, length(this.array) + 1)
        this.array[length(this.array) - 1] = value

    }

    this.addAll = function(array : array) {         //Add elements from array to the list

        require "arrays"
        import arrays.length
        import arrays.resize

        let oldLength = length(this.array)
        this.array = resize(this.array, oldLength + length(array))

        for (let i = 0; i < length(array); i++)
            this.array[i + oldLength] = array[i]

    }

    this.remove = function(value) {                 //Remove element from the list by value

        require "arrays"
        import arrays.length
        import arrays.copy

        for (let i = 0; i < length(this.array); i++)
            if (this.array[i] == value) {
                this.removeAt(i)
                break
            }

    }

    this.removeAt = function(idx : number) {        //Remove element from the list by index

        require "arrays"
        import arrays.length
        import arrays.resize
        import arrays.copy

        copy(this.array, idx + 1, this.array, idx, length(this.array) - (idx + 1))
        this.array = resize(this.array, length(this.array) - 1)

    }

    this.insert = function(value, idx : number) {   //Insert element to the list

        require "arrays"
        import arrays.length
        import arrays.resize
        import arrays.copy

        let oldLength = length(this.array)
        this.array = resize(this.array, oldLength + 1)
        copy(this.array, idx, this.array, idx + 1, oldLength - idx)
        this.array[idx] = value

    }

}

function fromArray(array : array) {                 //Create new list from array elements

    let list = new List()

    foreach (let item in array)
        list.add(item)

    return list

}

function of(params args) {                          //Create new list from sequence of elements

    return fromArray(args)

}

exports {

    List:       ::List,
    fromArray:  ::fromArray,
    of:         ::of

}