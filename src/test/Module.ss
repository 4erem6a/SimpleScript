let module {
    add: (a, b) -> a + b,
    sub: (a, b) -> a - b,
    mlt: (a, b) -> a * b,
    div: (a, b) -> a / b,
    mod: (a, b) -> a % b,
    $: (arg) -> require(io).println(arg)
}

exports module