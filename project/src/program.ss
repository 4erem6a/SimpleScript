//SimpleScript v1.8.4.3:
locked function getError() throw new require("errors").Error("Sample error")

function main(args) {
    require "io"
    import io.println

        $(io)

        locked function $(x) > require("io").println(x)
}