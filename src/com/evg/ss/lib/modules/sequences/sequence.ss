//SimpleScript'StdLibrary: sequences\sequence
function Sequence() {

    require "lists"
    import lists.List
    import lists.fromArray
    import lists.of

    this.list = new List()

    this.count = () -> list.size()

    this.setList = function(list) {

        this.list = list

    }

    this.array = () -> this.list.array

    this.add = function(params items) {

        foreach (let item in items)
            this.list.add(item)


        return self(this)

    }

    this.skip = function(count : number) {

        require "lists"
        import lists.List

        let result = new List()
        for (let i = count; i < this.list.size(); i++)
            result.add(this.list.get(i))

        this.list = result

        return self(this)

    }

    this.limit = function(count : number) {

        require "lists"
        import lists.List

        let result = new List()
        for (let i = 0; i < count; i++)
            result.add(this.list.get(i))

        this.list = result

        return self(this)

    }

    this.headTail = function(callback : function) {

        let head = (this.list.size() > 0 ? this.list.get(0) : null)
        let tail = (this.list.size() > 1 ? this.skip(1) : null)

        callback(head, tail)

        return self(this)

    }

    this.map = function(callback : function) {

        let result = new Sequence()

        foreach (let item in this.list.array)
            result.list.add(callback(item))

        return result;

    }

    this.flatMap = function(callback : function) {

        let result = new Sequence()

        foreach (let item in this.list.array)
            result.list.addAll(callback(item))

        return result;

    }

    this.forEach = function(callback : function) {

        let result = new Sequence()

        foreach (let item in this.list.array)
            callback(item)

    }

}

function self(sequence) {

    let result = new Sequence()

    result.list = sequence.list

    return result

}

exports {
    Sequence: ::Sequence
}