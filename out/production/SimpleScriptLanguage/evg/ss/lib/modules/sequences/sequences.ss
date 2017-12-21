//SimpleScript'StandardLibrary: sequences
//Version: 1.2
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

    this.forEach = function(callback : function) {

        foreach (let item in this.list.array)
            callback(item)

        return this

    }

}

exports {
    Sequence: ::Sequence
}