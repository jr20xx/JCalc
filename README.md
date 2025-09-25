<div align="center">
    <img src="https://raw.githubusercontent.com/jr20xx/JCalc/main/.github/resources/banner.webp" width="100%" alt="Banner">
</div>

#### Status

<p>
    <a href="https://github.com/jr20xx/JCalc/actions/workflows/perform_testing.yml">
        <img src="https://img.shields.io/github/actions/workflow/status/jr20xx/JCalc/perform_testing.yml?branch=main&label=Testing" alt="Testing">
    </a>
    <a href="https://github.com/jr20xx/JCalc/blob/main/LICENSE">
        <img src="https://img.shields.io/github/license/jr20xx/JCalc?label=License" alt="License">
    </a>
    <a href="https://github.com/jr20xx/JCalc">
        <img src="https://img.shields.io/github/repo-size/jr20xx/JCalc?label=Repository+Size" alt="Repository Size">
    </a>
    <a href="https://jitpack.io/#jr20xx/JCalc">
        <img src="https://jitpack.io/v/jr20xx/JCalc.svg" alt="JitPack">
    </a>
</p>

### Table of Contents

- [Description of the project](#description-of-the-project)
- [Getting the library](#getting-the-library)
  - [Building the library locally](#building-the-library-locally)
  - [Getting the library from JitPack](#getting-the-library-from-jitpack)
- [Usage](#usage)
  - [Supported operators](#supported-operators)
  - [Basic usage](#basic-usage)
  - [Advanced usage](#advanced-usage)
  - [Exceptions handling](#exceptions-handling)
- [Related project (W.I.P.)](#related-project-wip)
- [About the docs](#about-the-docs)
- [Contribution](#contribution)
- [Testing](#testing)

## Description of the project

This repo holds the code of a [Java library](https://en.wikipedia.org/wiki/Java_Class_Library) named JCalc made for parsing Math expressions as strings and quickly solve them. In order to make that possible, this library makes use of a custom implementation of the [Shunting Yard algorithm](https://en.wikipedia.org/wiki/Shunting_yard_algorithm) that allows it to quickly parse and solve Math expressions written in the notation commonly used to write Math expressions; also known as [infix notation](https://en.wikipedia.org/wiki/Infix_notation).

You can use this library to solve Math expressions like, for example:

- **((25\*3-9)/(4+2)+5^3)-(48/8)\*(7+2)+14** (which is equals to **96**)
- **3 + 4 * 2 / (1 - 5)^2^3** (which is equals to **3.000122070313**)
- **1000 / (2^5) + (3!)^4 - 500 * (2 + 3)** (which is equals to **-1172.75**)
- **(8^2 + 15 \* 4 - 7) / (3 + 5)\*(12 - 9) + 6^2 - (18 /3) + 11** (which is equals to **84.875**)
- **(2^10)! / (2^(5!))** (which is equals to **4.076447993302E2603**)
- **((100 + 200) * (300 - 150)) / (2^(3!)) + (7!)^25** (which is equals to **3.637168415833E92**)
- **(-2^3) * (-(3! + 4) / 2) + 5** (which is equals to **45**)
- **2 + -3! * (-4^2) / (5 - 3)** (which is equals to **50**)

All the expressions previously shown are just a few examples. The results were obtained with JCalc and checked using an online calculator app provided by [Desmos](https://www.desmos.com/about) in [this website](https://www.desmos.com/scientific).

## Getting the library

### Building the library locally

To build the library locally, the first thing you must do is to make sure that you have a valid installation of the [Java Development Kit (JDK)](https://en.wikipedia.org/wiki/Java_Development_Kit). That can be easily checked by executing the following command in a terminal:

```bash
java --version
```

> [!TIP]
>
> The minimum Java version required to build this library is [Java 8](https://en.m.wikipedia.org/wiki/Java_version_history#Java_8).

If the execution of that command generates any output instead of an error message, then your installation of the JDK should be correct. After effectively checking the JDK installation, you must create a local copy of this repo in your device and, in order to do that, launch a terminal and execute the following command:

```bash
git clone https://github.com/jr20xx/JCalc
```

Once the execution of that command is finished, a new directory containing a copy of the files of this project will be created. You can open that folder with a Java IDE with [Gradle](https://gradle.org/) support like [IntelliJ IDEA](https://www.jetbrains.com/idea/), [Android Studio](https://developer.android.com/studio) or [VSCode](https://code.visualstudio.com/) (with the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) installed) and then build the library making use of their integrated tools. If you need help doing that, you can check [this website](https://docs.gradle.org/current/userguide/gradle_ides.html) to get references of how to do that in your preferred IDE.

If you decide to build the library without using any IDE, then open the newly created directory containing the files from this repo and start a terminal session from there. Once you've opened a terminal, run any of the following commands:

*From Linux terminal:*

```bash
./gradlew build
```

*From Windows command line:*

```bash
gradlew.bat build
```

Once the execution of that command is done, open the `jcalc` directory, then open the `builds` directory and, once you are watching its content, you'll see a folder named `libs`. In that last folder you'll find compiled files of the library, the [Javadocs](https://en.wikipedia.org/wiki/Javadoc) and the source code compressed as independent JAR files that you can use as you wish. Here's a representation of the portion of the directories tree mentioned before, in plaintext format:

```txt
JCalc (parent folder)
├── jcalc
│   ├── build
│   │   ├── libs
│   │   │   ├── jcalc-X.Y.Z.jar
│   │   │   ├── jcalc-X.Y.Z-javadoc.jar
│   │   │   └── jcalc-X.Y.Z-sources.jar
```

> [!TIP]
>
> Please notice that **"X"**, **"Y"** and **"Z"** are used as placeholders in the names of the JAR files. They take the place of the version numbers of the library and are not part of the actual names of the compiled files.

### Getting the library from JitPack

To ease the process of getting a compiled version of the library that you can use directly in your projects, you can use JitPack. In order to do that, follow the official guide [provided here](https://jitpack.io/#jr20xx/JCalc) by JitPack.

## Usage

### Supported operators

JCalc comes with support for numbers written using a variant of the [Scientific notation](https://en.wikipedia.org/wiki/Scientific_notation) named [E notation](https://en.wikipedia.org/wiki/Scientific_notation#E_notation) and it also supports the usage of the Math constants "**e**" and "**π**" directly in the input expressions. Besides that, you can also make use of parentheses ("(" and ")").

The following tables summarize the rest of the symbols or tokens currently supported as operators. You can use any of those operators when writing the Math expressions and any other operator or value not included in those tables is currently not supported. If you use any symbol not included in any of the given tables, an exception will be thrown.

<table>
    <tr>
        <th colspan="4">Basic operators</th>
    </tr>
    <tr>
        <td>Addition</td> <td><b>+</b></td>
        <td>Subtraction</td> <td><b>-</b></td>
    </tr>
    <tr>
        <td>Multiplication</td> <td><b>*</b> or <b>×</b></td>
        <td>Division</td> <td><b>/</b> or <b>÷</b></td>
    </tr>
    <tr>
        <td>Exponentiation</td> <td><b>^</b></td>
        <td>Factorial</td> <td><b>!</b></td>
    </tr>
</table>
<table>
    <tr>
        <th colspan="4">Advanced operators</th>
    </tr>
    <tr>
        <td>Sine</td> <td><b>sin</b></td>
        <td>Cosine</td> <td><b>cos</b></td>
    </tr>
    <tr>
        <td>Tangent</td> <td><b>tan</b></td>
        <td>Arcsine</td> <td><b>asin</b> or <b>arcsin</b></td>
    </tr>
    <tr>
        <td>Arccosine</td> <td><b>acos</b> or <b>arccos</b></td>
        <td>Arctangent</td> <td><b>atan</b> or <b>arctan</b></td>
    </tr>
    <tr>
        <td>Cosecant</td> <td><b>csc</b></td>
        <td>Secant</td> <td><b>sec</b></td>
    </tr>
    <tr>
        <td>Cotangent</td> <td><b>cot</b></td>
        <td>Natural logarithm</td> <td><b>ln</b></td>
    </tr>
    <tr>
        <td>Base 2 logarithm</td> <td><b>log2</b></td>
        <td>Base 10 logarithm</td> <td><b>log</b></td>
    </tr>
    <tr>
        <td>Square root</td> <td><b>√</b> or <b>sqrt</b></td>
        <td>Cube root</td> <td><b>cbrt</b></td>
    </tr>
</table>

> [!TIP]
>
> It is strongly recommended to use parentheses in Math expressions to avoid parsing inaccuracies or ambiguity. That's because, for example, `log28` will be parsed as `log2(8)` if you don't use parentheses before the number **"2"** in **"28"**; so, if what you actually want is to get the base 10 logarithm, you have to use `log(28)` instead.

> [!WARNING]
>
>- The support for the advanced operators is still under development.
>- The library is case sensitive and that means that, for example, "E" and "e" are treated differently based on their casing.

### Basic usage

In order to solve Math expressions, you just have to call the `JCalc.solveMathExpression(...)` method, passing a `String` to it containing the Math expression that you want to get solved. When called, that method will return another `String` with the result of solving the given Math expression and any whitespace in the Math expression will be ignored. If the given expression is either empty, `null` or contains only whitespaces, `null` will be returned as result. To help you get a better idea of what was previously described, here's a code example:

```java
String expression = "3 + 4 * 2 / (1 - 5)^2^3";
String result = JCalc.solveMathExpression(expression);
System.out.print(result); // Prints "3.000122070313"
```

### Advanced usage

One of the main ideas behind the creation of JCalc is to allow a certain level of customization in the way it works; and that's precisely why it has an alternative `JCalc.solveMathExpression(...)` method that, in addition to taking the Math expression as first parameter, also receives an instance of the `ConfigurationBuilder` class as second parameter. This alternative method works nearly in the same way as it was described in the [basic usage](#basic-usage) section, returning the same type of data and only taking an additional parameter of the `ConfigurationBuilder` type to customize certain settings of the library.

The `ConfigurationBuilder` class, introduced in the third version of the library, includes methods to customize how the library behaves. It has a method to control the precision of the final result obtained when the Math expression is completely solved and, in addition to that, there's also a method that allows you to toggle the functionality to make an attempt to balance the parentheses in a given Math expression. Finally, it also contains another method that lets you control whether to use radians or degrees when dealing with trigonometric functions. Here's an example of how to create a new instance of the `ConfigurationBuilder` class and how to set up all the parameters previously mentioned one by one to later solve a Math expression:

```java
ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
configurationBuilder.setPrecision(10);
configurationBuilder.setBalanceParentheses(true);
configurationBuilder.setUseRadians(false);
String expression = "3 + 4 * 2 / (1 - 5)^2^3";
String result = JCalc.solveMathExpression(expression, configurationBuilder);
System.out.print(result); // Prints "3.0001220703"
```

Alternatively, you can set up all those parameters all at once as it follows:

```java
ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().setPrecision(10).setBalanceParentheses(true).setUseRadians(false);
String expression = "3 + 4 * 2 / (1 - 5)^2^3";
String result = JCalc.solveMathExpression(expression, configurationBuilder);
System.out.print(result); // Prints "3.0001220703"
```

If you forget to customize any parameter or just create a new instance of `ConfigurationBuilder` without setting up any parameter, the default values will be used instead. The default value for precision is 12 and the minimum accepted value for that setting is 3; so if you try to set it to a lower number, it will default to 3. Besides all that, the library defaults to radians when dealing with trigonometric functions and, by default, it disables the process to balance the parentheses in a Math expression.

> [!WARNING]
>
> If you pass `null` instead of a valid instance of the `ConfigurationBuilder`, you'll get an `IllegalArgumentException`.

### Exceptions handling

This library contains a small set of custom exceptions that should be controlled to guarantee that the execution of the program doesn't get interrupted or glitched. Here's a Java snippet showing all of them with added comments explaining when they are expected to happen:

```java
try {
    String expression = "2 * 3 + 5 * 2^3)";
    String result = JCalc.solveMathExpression(expression);
}
catch (UnbalancedParenthesesException exception) {
    // This exception occurs when the parentheses were not placed correctly and `false` is provided as second parameter
}
catch (NotNumericResultException exception) {
    // This exception occurs when a not numeric (NaN) value is obtained
}
catch (InfiniteResultException exception) {
    // This exception occurs when an Infinite result is obtained
}
catch (SyntaxErrorException exception) {
    // This exception occurs when an error is detected in the writing of the Math expression
}
catch (NumericalDomainErrorException exception) {
    // This exception occurs when trying to obtain the factorial of a number when it's negative or not an integer
}
catch (Exception exception) {
    // This is recommended in case that an unexpected exception arises
}
```

## Related project (W.I.P.)

- [Android Calculator](https://github.com/jr20xx/android-calculator)

## Testing

In order to provide a more reliable mechanism that also ensures that JCalc works as expected, [JUnit](https://junit.org/) tests were implemented. Thanks to [this GitHub workflow](https://github.com/jr20xx/JCalc/actions/workflows/perform_testing.yml), they are run on each commit or pull request to also follow some [Continuous Integration](https://en.wikipedia.org/wiki/Continuous_integration) principles.

So far there are just a few tests cases written and they still don't cover all the functionalities of the library. Feel free to write your own when you clone or fork this repo and then suggest some of your own test cases (the more complicated, the best).

Test cases are located under [`./jcalc/src/test/resources`](https://github.com/jr20xx/JCalc/tree/main/jcalc/src/test/resources) as CSV files you can edit as you please without embedding them in the testing class directly. When you suggest new tests, please follow that principle: use standalone CSV files and later add references to them in the testing class instead of embedding test cases in the code of the testing class.

## About the docs

The code included in this library includes [Javadocs](https://en.wikipedia.org/wiki/Javadoc) comments nearly everywhere and the rest of the code will be documented in the same way soon. Thanks to JitPack, you can read the online version of the Javadocs by visiting [this website](https://jitpack.io/com/github/jr20xx/JCalc/latest/javadoc/).

## Contribution

We value a lot any kind of contribution and we encourage you to submit pull requests and provide tutorials or other relevant content that might help to improve this library or extend its functionalities.

You can also contribute to this repo by adding a star to it and/or sharing the link if you find it helpful in some way. Any form of help will be highly appreciated.