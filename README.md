# Another JDK based programming language
This documentation describes the core syntax and features of the language.

## Classes
To declare a `class`, use the class keyword:

```java
class Main {
  ...
}
```
Classes are the main containers for functions.

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
