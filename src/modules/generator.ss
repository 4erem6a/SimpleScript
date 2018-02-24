class Generator {
    new(generator) {
        if (!(generator is type("function")))
            throw new require('error')("Invalid generator.")
        this.generator = generator
        this.list = new require('arraylist')()
    }
    locked $call(args...) {
        this.generator.apply({
            yield: that.list.addAll
        }, args)
        return this.list
    }
}

exports Generator