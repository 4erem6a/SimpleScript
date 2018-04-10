exports {
    toString() -> this as type('string'),
    remove(key) -> require('maps').remove(this, key),
    size() -> require('maps').size(this),
    matches(map) -> require('maps').match(this, map)
}