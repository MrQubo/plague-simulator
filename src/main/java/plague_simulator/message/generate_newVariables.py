def print_generic_impl(n):
    types = []
    for i in range(1, n+1):
            types.append(f'T{i}')

    print('static public <{}> Set<Variable> newVariables('.format(', '.join(types)))

    for i in range(1, n+1):
            print('    String name{0}, {1} value{0}, Class<? extends {1}> type{0}'.format(i, types[i-1]) + (',' if i < n else ''))

    print(') {')
    print('    return newVariables(')

    for i in range(1, n+1):
            print('        newVariable(name{0}, value{0}, type{0})'.format(i) + (',' if i < n else ''))

    print('    );')
    print('}')


def print_type_impl(type):
    print('static public Variable new{0}Variable(String name, {0} value) {{'.format(type))
    print('    return newVariable(name, value, {0}.class);'.format(type))
    print('}')


def print_typed_impl(type, n):
    print('static public Set<Variable> new{0}Variables('.format(type))

    for i in range(1, n+1):
            print('    String name{0}, {1} value{0}'.format(i, type) + (',' if i < n else ''))

    print(') {')
    print('    return newVariables(')

    for i in range(1, n+1):
            print('        new{1}Variable(name{0}, value{0})'.format(i, type) + (',' if i < n else ''))

    print('    );')
    print('}')



count = 10
types = ['String', 'Integer', 'Long', 'Double', 'Float', 'Boolean']

for i in range(1, count + 1):
    print_generic_impl(i)

print()

for type in types:
    print_type_impl(type, )
    for i in range(1, count + 1):
        print_typed_impl(type, i)
    print()
