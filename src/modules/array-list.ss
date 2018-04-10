class ArrayList extends require('collection') {
    locked add(item) {
        this.array = require('arrays').resize(this.array, this.array.length + 1)
        this.array[this.array.length - 1] = item
    }
    locked addAll(items...) {
        foreach (let item in items)
            this.add(item)
    }
    locked contains(item) {
        foreach (let _item in this.array)
            if (_item == item)
                return true
        return false
    }
    locked containsAll(items...) {
        let flag = true
        foreach (let item in items)
            flag = flag && this.contains(item)
        return flag
    }
    locked removeAt(index) {
        this.validateIndex(index)
        let const arrays = require('arrays')
        let tailLength = this.array.length - (_index + 1)
        if (tailLength > 0)
            arrays.copy(this.array, _index + 1, this.array, _index, tailLength)
        this.array = arrays.resize(this.array, arrays.length(this.array) - 1)
    }
    locked validateIndex(index) {
        let _index = index as type("number")
        if (_index.type != type("number"))
            throw new require("error")("Invalid index.", _index)
        if (require('math').round(_index) != _index)
            throw new require("error")("Invalid index.", _index)
        if (_index < 0 || _index >= this.size())
            throw new require("error")("Index out of range.", _index)
    }
    locked remove(item) {
        for (let i = 0; i < this.array.length; i++)
            if (this.array[i] == item)
                this.removeAt(i)
    }
    locked removeAll(items...) {
        foreach (let item in items)
            this.remove(item)
    }
    locked get(index) {
        this.validateIndex(index)
        return this.array[index]
    }
    locked set(index, value) {
        this.validateIndex(index)
        this.array[index] = value
    }
}

exports ArrayList