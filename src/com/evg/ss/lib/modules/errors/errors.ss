//SimpleScript'StandardLibrary: errors
//Version: 1.0
/*Dependencies:
 *  --- none ---
 */
locked function Error(message : string, args...) {

    this.message = message
    this.args = args
    this.stackTrace = require("interpreter").requireStackTrace()

}

exports {
    Error: Error
}