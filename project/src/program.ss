//SimpleScript v1.8.4:


function main(args) {

    require "io"

    foreach (let arg in args)
        io.println(arg, ':', typeof(arg))

}