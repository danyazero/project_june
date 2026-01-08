# Another JVM based programming language
This is a small experimental JVM-based programming language created for learning and fun purposes. The language is implemented in Kotlin, uses ANTLR for parsing, and compiles directly into Java bytecode, which can be executed on any standard JVM.

## Get Started
This section describes the minimal steps required to build and run programs written in June language.

### Prerequisites
To work with the project, you need:
- JDK 21+ (any recent JVM should work)
- Git

### Building the Compiler
Clone the repository and build the project:
```shell
git clone https://github.com/danyazero/project_june.git
cd project_june
./gradlew build
```

### Compiling a Program
You can compile your code from a source file in two ways:
```shell
java -jar build/libs/june-all.jar <filename>
```
or
```shell
bash june.sh <filename>
```
This will parse the source file and generate a `.class` file.

### Running the program
After compilation, run the generated class using the JVM:
```shell
java Main
```
Make sure the class name (`Main` in example) matches the entry class defined in your source code.

### Minimal Example
```Rust
class Main {
  pub fn* main(args []string) {
    print!("Hello, world!")
    return
  }
}
```
After compilation and execution, the output will be:
```text
Hello, world!
```

## Classes
To declare a `class`, use the class keyword:

```java
class Main {
  ...
}
```
Classes are the main containers for functions.

### Constructors
You can define constructors using the `constructor` keyword and providing their signature.
Multiple constructors (overloading) are supported.
```java
class Main {
  constructor() {}

  constructor(v string) {
    print!("Hello, " + v + "!")
  }

  pub fn* main(args []string) {
    main := Main { "Jhon" }
    return
  }
}
```
After compilation and execution, the program outputs:
```text
Hello, Jhon!
```
Object creation uses the `{ ... }` syntax, which selects the appropriate constructor based on the arguments.

### Virtual (Instance) Methods
By default, functions declared inside a class are virtual (non-static) unless marked as static using fn*.
```java
class Main {
  constructor() {}

  pub fn sum(a int, b int) int {
    return a + b
  }

  pub fn* main(args []string) {
    main := Main { }
    print!(main.sum(7, 3))
    return
  }
}
```
In this example:
- sum is a virtual method
- It is called on a class instance

## Functions

Functions (or methods) are declared using the `fn` keyword.

### Static functions
If you want to make a function static, put a * character immediately after the fn keyword:
```rust
fn* staticMethod() {
  ...
}
```
### Access modifiers
All functions are private by `default`.
To make a function public, prepend the `pub` modifier:
```rust
pub fn* main(args []string) {
  ...
}
```

### Function parameters, return types, and behavior
Example:
```rust
fn sum(x int, y int) int {
    return x + y
}
```
In this example:
- The function is virtual (non-static) and private.
- `x` and `y` are parameters.
- Both parameters have type `int`.
- The function returns an `int`.
- The returned value is the sum of `x` and `y`.

## Variables & Constants
The language distinguishes between mutable and read-only values.

### Constants
Use const to declare read-only values:
```go
const pi = 3.14
const greeting = "Hello"
```
### Mutable variables
You can declare mutable variables using either the var keyword or the short := syntax.
Using var
```go
var a int = 3
var b bool = true
var c string = "Hello World!"
```
Using `:=` (short declaration)
Inside functions, instead of writing var with an explicit type, you can use the short assignment operator :=.
The type is inferred automatically:
```go
a := 3
b := true
c := "Hello World!"
```

## Loops
The language provides several looping constructs, ranging from array iteration to numeric ranges and condition-based loops.

### Array Iteration
This version automatically iterates through all elements in the array, binding each to item.

You can iterate over arrays in two ways: with an index or without an index.
With index and item
```rust
for (index, item) in array {
  ...
}
```
- `index` — the element index (starting at 0)
- `item` — the element value
- `array` — any iterable array

Example:
```rust
for (index, value) in numbers {
  print!(index + value)
}
```
#### Without index (item only)
If the index is not needed, you may omit it:
```rust
for item in array {
  ...
}
```
Example:
```rust
for value in numbers {
  print!(value)
}
```

### Range Loop

#### Exclusive Upper Bound
Use `..<` to loop from a start value up to (but not including) the end value.
```rust
for i in 0..<4 {
  ...
}
```
This runs for 0, 1, 2, 3.

#### Inclusive Upper Bound
Use `..` to loop from a start value including the end value.
```rust
for i in 0..4 {
  ...
}
```
This runs for 0, 1, 2, 3, 4.

### Infinite Loop
Creates a loop that runs forever unless manually broken:
```rust
loop {
  ...
}
```

### Conditional Loop
Acts as a while loop.
The loop continues as long as the condition is true.
```
index := 0
loop index < 4 {
  ...
  index++
}
```
