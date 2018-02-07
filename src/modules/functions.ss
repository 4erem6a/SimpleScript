//SimpleScript'StandardLibrary: functions
//Version: 1.1
/*Dependencies:
 *  --- none ---
 */
exports {

    IDENTITY: locked function(x) -> x,

    ADD: locked function(a, b) -> a + b,
    SUB: locked function(a, b) -> a - b,
    DIV: locked function(a, b) -> a / b,
    MLT: locked function(a, b) -> a * b,
    MOD: locked function(a, b) -> a % b,

    CONCAT: locked function(a, b) -> a + "" + b,
    CONCATLN: locked function(a, b) -> a + "\n" + b,

    BOOLEAN_NEGATION: locked function(x) -> !x,
    BITWISE_NEGATION: locked function(x) -> ~x,
    NUMERIC_NEGATION: locked function(x) -> -x,

    COMPARISON: locked function(a, b) -> a =? b,
    NEG_COMPARISON: locked function(a, b) -> -(a =? b)

}