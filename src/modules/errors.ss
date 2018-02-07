//SimpleScript'StandardLibrary: errors
//Version: 1.1
/*Dependencies:
 *  --- none ---
 */
class Error {
    new(message, args...) {
        this.message = message as type("string")
        this.args = args
        this.stackTrace = require("interpreter").requireStackTrace()
    }
}

exports {
    Error: Error
}