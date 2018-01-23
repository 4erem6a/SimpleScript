//SimpleScript'StandardLibrary: sequences
//Version: 1.5
/*Dependencies:
 *  lists
 */
locked function Sequence(base = null) {
    require "lists"

    this.list = (base == null ? new lists.List() : base.list)

    this.count = locked function() -> this.list.size()
    this.toList = locked function() -> this.list
    this.toArray = locked function() -> this.list.array

    this.head = locked function() -> this.list.first()
    this.tail = locked function() {
        let tail = this.skip(1)
        return (tail == null ? new require("lists").List() : tail)
    }
    this.headTail = locked function(callback : function) -> callback(this.head(), this.tail())

    this.skip = locked function(count : number) {
        if (count < 0 || count >= this.list.size())
            return null
        require "lists"
        let result = new lists.List()
        for (let i = count; i < this.list.size(); i++)
            result.add(this.list.get(i))
        return result.sequence()
    }

    this.limit = locked function(count : number) {
        if (count <= 0 || count > this.list.size())
            return null
        require "lists"
        let result = new lists.List()
        for (let i = 0; i < count; i++)
            result.add(this.list.get(i))
        return result.sequence()
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
        let result = @this
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

    this.orderBy = locked function(callback : function) {
        let result = this.list
        result.sortBy(callback)
        return result.sequence()
    }

    this.sort = locked function(callback : function) {
        let result = this.list
        result.sort(callback)
        return result.sequence()
    }

    this.dropWhile = locked function(callback : function) {
        let list = this.list
        let index = 0
        while (callback(list.get(index))) {
            list.removeAt(index)
            index++
        }
        return list.sequence()
    }

    this.takeWhile = locked function(callback : function) {
        let list = new require("lists").List()
        let index = 0
        while (callback(this.list.get(index))) {
            list.add(this.list.get(index))
            index++
        }
        return list.sequence()
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
        return @this
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
    Sequence:   Sequence,
    of:         of,
    fromList:   fromList
}