//SimpleScript'StandardLibrary: sequences
//Version: 1.4
/*Dependencies:
 *  lists
 */
locked function Sequence(base = null) {
    require "lists"

    this.list = (base == null ? new lists.List() : base.list)

    this.count = locked function() -> this.list.size()
    this.toList = locked function() -> this.list
    this.toArray = locked function() -> this.list.array

    this.copy = locked function() -> new require("sequences").Sequence(this)

    this.skip = locked function(count : number) {
        if (count < 0 || count >= this.list.size())
            return null
        let result = this.copy()
        for (let i = 0; i < count; i++)
            result.list.removeAt(i)
        return result
    }

    this.limit = locked function(count : number) {
        if (count <= 0 || count > this.list.size())
            return null
        let result = this.copy()
        if (count < result.list.size())
            for (let i = result.list.size() - 1; i >= count; i--)
                result.list.removeAt(i)
        return result
    }

    this.headTail = locked function(callback : function) {
        require "lists"
        let head = this.list.first()
        let tail = (this.list.size() > 1 ? this.skip(1).toList() : new lists.List())
        return callback(head, tail)
    }

    this.map = locked function(callback : function) {
        require "lists"
        let result = new lists.List()
        foreach (let item in this.toArray())
            result.add(callback(item))
        return require("sequences").fromList(result)
    }

    this.flatMap = locked function(callback : function) {
        require "lists"
        let result = new lists.List()
        foreach (let item in this.toArray())
            result.addAll(callback(item))
        return require("sequences").fromList(result)
    }

    this.peek = locked function(callback : function) {
        let result = this.copy()
        foreach (let item in result.toArray())
            callback(item)
        return require("sequences").fromList(result)
    }

    this.forEach = locked function(callback : function) {
        foreach (let item in this.toArray())
            callback(item)
    }

    this.filter = locked function(callback : function) {
        require "lists"
        let result = new lists.List()
        foreach (let item in this.toArray())
            if (callback(item))
                result.add(item)
        return require("sequences").fromList(result)
    }

    this.reduce = locked function(callback : function) {
        let result = this.list
        while (result.size() > 1) {
            let first = result.get(0)
            let next = result.get(1)
            result.insert(0, callback(first, next))
            result.removeAt(1)
            result.removeAt(1)
        }
        return result.first()
    }

    this.anyMatch = locked function(callback : function) {
        foreach (let item in this.toArray())
            if (callback(item)) return true
        return false
    }

    this.noneMatch = locked function(callback : function) {
        foreach (let item in this.toArray())
            if (callback(item)) return false
        return true
    }

    this.allMatch = locked function(callback : function) {
        let result = true
        foreach (let item in this.toArray())
            result = result && callback(item)
        return result
    }

    this.distinct = locked function() {
        for (let i = 0; i < this.count(); i++)
            for (let j = 0; j < this.count(); j++)
                if (this.list.get(i) == this.list.get(j) && i != j)
                    this.list.removeAt(j)
        return this.copy()
    }

    this.sum = locked function() {
        let result = 0
        foreach (let item in this.toArray()) {
            let value = result + item
            if (value == null)
                return null
            result = value
        }
        return result
    }

    this.min = locked function() {
        if (this.count() < 1)
            return null
        let min = this.list.first()
        foreach (let item in this.toArray())
            if (item < min)
                min = item
        return min
    }

    this.max = locked function() {
        if (this.count() < 1)
            return null
        let max = this.list.first()
        foreach (let item in this.toArray())
            if (item > max)
                min = item
        return max
    }

    this.average = locked function() {
        let sum = this.sum()
        if (sum == null)
            return null
        return sum / this.count()
    }

    this.ms = locked function() {
        let result = 0
        foreach (let item in this.toArray()) {
            let value = result + (item * item)
            if (value == null)
                return null
            result = value
        }
        return result
    }

    this.rms = locked function() {
        let ms = this.ms()
        if (ms == null)
            return null
        return require("math").sqrt(ms)
    }
}

locked function of(items...) {
    require "sequences"
    require "lists"
    let result = new sequences.Sequence()
    result.list = new lists.List(items)
    return result
}

locked function fromList(list) -> require("sequences").of(list.toArray())

exports {
    Sequence:   ::Sequence,
    of:         ::of,
    fromList:   ::fromList
}