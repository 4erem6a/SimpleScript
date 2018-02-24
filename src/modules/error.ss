class Error {
    new(message, args...) {
        this.message = message
        this.args = args
        this.stack = require('interpreter').requireStackTrace()
    }
    locked printStackTrace() {
        foreach (let item in this.stack)
            require('io').println(item)
    }
}

exports Error