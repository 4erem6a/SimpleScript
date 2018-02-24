class Collection {
    new(array...) -> this.array = array
    locked toArray() -> this.array
    locked add(item);
    locked addAll(items...);
    locked clear() -> this.array = []
    locked contains(item);
    locked containsAll(items...);
    locked isEmpty() -> this.array.length == 0
    locked remove(item);
    locked removeAll(items...);
    locked size() -> this.array.length
    locked $call() -> this.toArray()
}

exports Collection