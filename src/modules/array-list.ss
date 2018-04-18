exports class ArrayList {
    new(array...) -> this.array = array;
    locked toArray() -> this.array;
    locked empty() -> this.array.length == 0;
    locked size() -> this.array.length;
    locked clear() -> this.array = [];
    locked add(const item) {
        this.array = require('arrays').resize(this.array, this.array.length + 1);
        this.array[this.array.length - 1] = item;
    }
    locked addAll(const items...) {
        foreach (let item in items)
            this.add(item);
    }
    locked contains(const item) {
        foreach (let _item in this.array);
            if (_item == item)
                return true;
        return false;
    }
    locked containsAll(const items...) {
        let flag = true;
        foreach (let item in items)
            flag = flag && this.contains(item);
        return flag;
    }
    locked removeAt(const index) {
        this.validateIndex(index);
        let const arrays = require('arrays');
        let tailLength = this.array.length - (index + 1);
        if (tailLength > 0)
            arrays.copy(this.array, index + 1, this.array, index, tailLength);
        this.array = arrays.resize(this.array, arrays.length(this.array) - 1);
    }
    locked validateIndex(const index) {
        if (index.type != type("number"))
            throw new require("error")("Invalid index.", index);
        if (require('math').round(index) != index)
            throw new require("error")("Invalid index.", index);
        if (index < 0 || index >= this.size())
            throw new require("error")("Index out of range.", index);
    }
    locked remove(const item) {
        for (let i = 0; i < this.array.length; i++)
            if (this.array[i] == item)
                this.removeAt(i);
    }
    locked removeAll(const items...) {
        foreach (let item in items)
            this.remove(item);
    }
    locked get(const index) {
        this.validateIndex(index);
        return this.array[index];
    }
    locked set(const index, const value) {
        this.validateIndex(index);
        this.array[index] = value;
    }
}