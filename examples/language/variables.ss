//In ss, variables can be defined by let statement or let expression:
//Let statement syntax: let [const] <name> [= <expression>]
let x               //x = null
let x = 10          //x = 10
let const x = 10    //const x = 10
let const y = x     //error: const variable can only be assigned with language literal or language constant
//Let expression syntax: let <name> [= <expression>]
//Let expression will return assigned value or null
//See let statement /\