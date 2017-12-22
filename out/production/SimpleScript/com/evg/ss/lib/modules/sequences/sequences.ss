//SimpleScript'StandardLibrary: sequences
//Version: 1.3
/*Dependencies:
 *  arrays
 *  sequences
 */
require "lists"

import lists.List
import lists.fromArray
import lists.of

function Sequence() {

    this.list = new List()

    this.count = () -> list.size()

    this.setList = function(list) this.list = list

    this.array = () -> this.list.array

    this.add = function(params items) {

        foreach (let item in items)
            this.list.add(item)

        return this

    }

    this.skip = function(count : number) {

        let result = new List()
        for (let i = count; i < this.list.size(); i++)
            result.add(this.list.get(i))

        this.list = result

        return this

    }

    this.limit = function(count : number) {

        let result = new List()
        for (let i = 0; i < count; i++)
            result.add(this.list.get(i))

        this.list = result

        return this

    }

    this.headTail = function(callback : function) {

        let head = (this.list.size() > 0 ? this.list.get(0) : null)
        let tail = (this.list.size() > 1 ? this.skip(1) : null)

        callback(head, tail)

        return this

    }

    this.map = function(callback : function) {

        let list = new List()

        foreach (let item in this.list.array)
            list.add(callback(item))

        this.list = list

        return this

    }

    this.flatMap = function(callback : function) {

        let list = new List()

        foreach (let item in this.list.array)
            list.addAll(callback(item))

        this.list = list

        return this

    }

    this.filter = function(callback : function) {

        let list = new List()

        foreach (let item in this.list.array)
            if (callback(item))
                list.add(item)

        this.list = list

        return this

    }

    this.reduce = function(callback : function) {

        while (this.count() > 1) {

            let first = this.list.get(0)
            let second = this.list.get(1)

            this.list.removeAt(0)
            this.list.removeAt(1)

            this.list.insert(callback(first, second), 0)

        }

        return this

    }

    this.anyMatch = function(callback : function) {

        foreach (let item in this.list.array)
            if (callback(item)) return true

        return false

    }

    this.noMatch = function(callback : function) {

        foreach (let item in this.list.array)
            if (callback(item)) return false

        return true

    }

    this.distinct = function(callback : function) {

        for (let i = 0; i < this.count(); i++)
            for (let j = 0; j < this.count(); j++)
                if (this.list.get(i) == this.list.get(j) && i != j)
                    this.list.removeAt(j)

        return this

    }

    this.forEach = peek(callback : function) {

        foreach (let item in this.list.array)
            callback(item)

        return this

    }

    this.forEach = function(callback : function) {

        foreach (let item in this.list.array)
            callback(item)

    }

}

exports {
    Sequence: ::Sequence
}