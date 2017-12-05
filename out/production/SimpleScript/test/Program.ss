require external("src/test/Module.ss") as "module"
require(io)

2 + 2

io.println(module.add(1, 2))