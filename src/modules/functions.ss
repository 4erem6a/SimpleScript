exports {
    locked IDENTITY(x) -> x

    locked ADD(a, b) -> a + b
    locked SUB(a, b) -> a - b
    locked DIV(a, b) -> a / b
    locked MLT(a, b) -> a * b
    locked MOD(a, b) -> a % b

    locked CONCAT(a, b) -> `${a}${b}`
    locked CONCATLN(a, b) -> `${a}\n${b}`

    locked BOOLEAN_NEGATION(x) -> !x
    locked BITWISE_NEGATION(x) -> ~x
    locked NUMERIC_NEGATION(x) -> -x

    locked COMPARISON(a, b) -> a =? b
    locked NEG_COMPARISON(a, b) -> -(a =? b)
}