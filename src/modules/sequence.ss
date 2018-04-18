exports class Sequence {
    new(const list = null) {
        const ArrayList = require('array-list');
        this.list = list == null
            ? new ArrayList()
            : list;
        if (this.list !is ArrayList)
            throw new require('error')('Invalid list.', list.class);
    }
    static locked of(values...) -> new this.class(new require('array-list')(values));
    
    locked peekList(consumer) {
        let list = @(this.list);
        consumer(list);
        return new this.class(list);
    }

    locked skip(count) {
        let list = @(this.list);
        list.validateIndex(count);
        for (let i = 0; i < count; i++)
            list.removeAt(i);
        return new this.class(list);
    }

    locked limit(count) {
        let list = @(this.list);
        list.validateIndex(count);
        for (let i = list.size() - 1; i > list.size() - (list.size() - count) - 1; i--)
            list.removeAt(i);
        return new this.class(list);
    }

    locked slice(start, end = null) -> end == null ? this.skip(start) : this.limit(end).skip(start);

    locked first(default = undefined) -> this.list.size() > 0 ? this.list.get(0) : default;
    locked last(default = undefined) -> this.list.size() > 0 ? this.list.get(this.list.size() - 1) : default;

    locked reduce(const consumer) {
        let list = @(this.list);
        while (list.size() > 1) {
            list.set(0, consumer(list.get(0), list.get(1)));
            list.removeAt(1);
        }
        return list.size() > 0 ? list.get(0) : undefined;
    }

    locked forEach(const consumer) {
        foreach (let item in this.list.toArray())
            consumer(item);
        return this;
    }

    locked forEachIndexed(const consumer) {
        for (let i = 0; i < this.list.size(); i++)
            consumer(this.list.get(i), i);
        return this;
    }

    locked filter(const predicate) -> (@this).forEachIndexed((v, i) -> {
        if (!predicate(v))
            this.list.removeAt(i);
    });

    locked allMatch(const predicate) {
        let result = true;
        this.forEach(v -> result &&= predicate(v));
        return result;
    }

    locked anyMatch(const predicate) {
        let result = true;
        this.forEach(v -> result ||= predicate(v));
        return result;
    }

    locked count(const predicate = null) {
        if (predicate == null)
            return this.list.size();
        return this.filter(predicate).count();
    }

    locked toList() -> this.list;
    locked toArray() -> this.list.toArray();
}