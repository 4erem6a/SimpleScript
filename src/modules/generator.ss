class Generator {
    new(generator, converter = undefined) {
        if (!(generator is type("function")))
            throw new require('error')("Invalid generator.")
        if (!(converter is type("function")) && converter != undefined)
            throw new require('error')("Invalid converter.")
        this.generator = generator
        this.list = new require('arraylist')()
        this.converter = converter
    }
    locked $call(args...) {
        this.generator.apply({
            yield: that.list.addAll
        }, args)
        if (this.converter == undefined)
            return this.list
        return this.converter(this.list)
    }
}

exports Generator