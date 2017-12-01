import <io>

let object {
    lambda: (a, b) -> a + b,
    method: function(a, b) = a + b,
    field:  #FF,
    static constant: 0x11111111
}

println(object.lambda(1, 2));
println(object.method(3, 4));
println(object.field);
println(object.constant);