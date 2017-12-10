require "io"
require "interpreter"
require local "module"

io.println(`You're using SimpleScript v{interpreter.version}.`)
io.println(`Local module named {nameof(module)}.ss has been loaded successfully.`)
io.println(`Environment variable: "CURRENT_LANG_VERSION" = {module.version}`)
io.println(`Pi = {module.pi}`)

let expression = "2 + 2"

io.println(`{expression} = {interpreter.eval(expression)}`)