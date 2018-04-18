exports function stringf(const string, const format...) {
    let result = '';
    for (let i = 0, f = 0; i < string.length; i++)
        if (string[i] == '%')
            if (i < string.length - 1 && string[i + 1] == '%')
                result += string[i++];
            else result += format[f++] as type('string');
        else result += string[i];
    return result;
}