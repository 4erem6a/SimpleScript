//SimpleScript'StandardLibrary: sequences
//Version: 1.6
/*Dependencies:
 *  lists
 */
class Sequence {
    new(base = null) {
        let lists = require("lists")
        this.list = (base == null ? new lists.List() : base.list)
    }

    locked count() -> this.list.size()
    locked toList() -> this.list
    locked toArray() -> this.list.array

    locked head() -> this.list.first()
    locked tail() {
        let tail = this.skip(1)
        return (tail == null ? new require("lists").List() : tail)
    }
    locked headTail(callback) {
        if (!(callback is type("function")))
            return undefined
        callback(this.head(), this.tail())
    }

    locked skip(count) {
        if (!(count is type("number")))
            return undefined
        if (count < 0 || count >= this.list.size())
            return null
        let lists = require("lists")
        let result = new lists.List()
        for (let i = count; i < this.list.size(); i++)
            result.add(this.list.get(i))
        return result.sequence()
    }

    locked limit(count) {
        if (!(count is type("number")))
            return undefined
        if (count <= 0 || count > this.list.size())
            return null
        let lists = require("lists")
        let result = new lists.List()
        for (let i = 0; i < count; i++)
            result.add(this.list.get(i))
        return result.sequence()
    }

    locked map(callback) {
        if (!(callback is type("function")))
            return undefined
        let lists = require("lists")
        let result = new lists.List()
        foreach (let item in this.toArray())
            result.add(callback(item))
        return require("sequences").fromList(result)
    }

    locked flatMap(callback) {
        if (!(callback is type("function")))
            return undefined
        let lists = require("lists")
        let result = new lists.List()
        foreach (let item in this.toArray())
            result.addAll(callback(item))
        return require("sequences").fromList(result)
    }

    locked peek(callback) {
        if (!(callback is type("function")))
            return undefined
        let result = @this
        foreach (let item in result.toArray())
            callback(item)
        return require("sequences").fromList(result)
    }

    locked forEach(callback) {
        if (!(callback is type("function")))
            return undefined
        foreach (let item in this.toArray())
            callback(item)
    }

    locked filter(callback) {
        if (!(callback is type("function")))
            return undefined
        let lists = require("lists")
        let result = new lists.List()
        foreach (let item in this.toArray())
            if (callback(item))
                result.add(item)
        return require("sequences").fromList(result)
    }

    locked orderBy(callback) {
        if (!(callback is type("function")))
            return undefined
        let result = this.list
        result.sortBy(callback)
        return result.sequence()
    }

    locked sort(callback) {
        if (!(callback is type("function")))
            return undefined
        let result = this.list
        result.sort(callback)
        return result.sequence()
    }

    locked dropWhile(callback) {
        if (!(callback is type("function")))
            return undefined
        let list = this.list
        let index = 0
        while (callback(list.get(index))) {
            list.removeAt(index)
            index++
        }
        return list.sequence()
    }

    locked takeWhile(callback) {
        if (!(callback is type("function")))
            return undefined
        let list = new require("lists").List()
        let index = 0
        while (callback(this.list.get(index))) {
            list.add(this.list.get(index))
            index++
        }
        return list.sequence()
    }

    locked reduce(callback) {
        if (!(callback is type("function")))
            return undefined
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

    locked anyMatch(callback) {
        if (!(callback is type("function")))
            return undefined
        foreach (let item in this.toArray())
            if (callback(item)) return true
        return false
    }

    locked noneMatch(callback) {
        if (!(callback is type("function")))
            return undefined
        foreach (let item in this.toArray())
            if (callback(item)) return false
        return true
    }

    locked allMatch(callback) {
        if (!(callback is type("function")))
            return undefined
        let result = true
        foreach (let item in this.toArray())
            result = result && callback(item)
        return result
    }

    locked distinct() {
        for (let i = 0; i < this.count(); i++)
            for (let j = 0; j < this.count(); j++)
                if (this.list.get(i) == this.list.get(j) && i != j)
                    this.list.removeAt(j)
        return @this
    }

    static locked of(items...) {
        let sequences = require("sequences")
        let lists = require("lists")
        let result = new sequences.Sequence()
        result.list = new lists.List(items)
        return result
    }

    static locked fromList(list) -> require("sequences").Sequence.of(list.toArray())
}

exports {
    Sequence:   Sequence
}