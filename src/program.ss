//SimpleScript v1.12.1.0:
//!##LinterIgnore
const io = require('io');
const Sequence = require('sequence');
const ArrayList = require('array-list');


let sq = Sequence.of(1, 2, 3, 4, 5);

function f(x) -> x * x;
function g(x) -> x + 10;
/*
'Hello, world' |> io.println |> io.println

io.println(io.println('Hello, world!'))

io.println(io.println)('Hello, world!')
*/

let x;

[1, 2, 3, 4]... |> io.println