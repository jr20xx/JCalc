## JCalc

<p>
    <a href="https://github.com/jr20xx/JCalc/blob/main/LICENSE">
        <img src="https://img.shields.io/github/license/jr20xx/JCalc?label=License" alt="License">
    </a>
    <a href="https://github.com/jr20xx/JCalc">
        <img src="https://img.shields.io/github/repo-size/jr20xx/JCalc?label=Repository+Size" alt="Repository Size">
    </a>
    <a href="https://jitpack.io/#jr20xx/JCalc">
        <img src="https://jitpack.io/v/jr20xx/JCalc.svg" alt="License">
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
  - [Exceptions handling](#exceptions-handling)
- [Related project (W.I.P.)](#related-project-wip)
- [About the docs](#about-the-docs)
- [Contribution](#contribution)

## Description of the project

This repo holds the code of a [Java library](https://en.wikipedia.org/wiki/Java_Class_Library) named JCalc that is especially made for those who, for any reason, want to allow users to input basic Math expressions in Java apps and quickly solve them during runtime (like, for example, when building a basic calculator). In order to make that possible, this library makes use of a custom implementation of the [Shunting Yard algorithm](https://en.wikipedia.org/wiki/Shunting_yard_algorithm) that allows it to quickly parse and solve Math expressions written in the notation commonly used to write Math expressions; also known as [infix notation](https://en.wikipedia.org/wiki/Infix_notation).

You can use this library to solve statements like, for example:

- **((25\*3-9)/(4+2)+5^3)-(48/8)\*(7+2)+14** (which is equals to **96**)
- **2 \* 3 + 5 \* 2^3** (which is equals to **46**)
- **2 * -(3 + 4! / 2) + 5^2** (which is equals to **-5**)
- **3 + 4 * 2 / (1 - 5)^2^3** (which is equals to **3.000122070313**)
- **1000 / (2^5) + (3!)^4 - 500 * (2 + 3)** (which is equals to **-1172.75**)
- **(8^2 + 15 \* 4 - 7) / (3 + 5)\*(12 - 9) + 6^2 - (18 /3) + 11** (which is equals to **84.875**)
- **(2^10)! / (2^(5!))** (which is equals to **4.076447993302E2603**)
- **((100 + 200) * (300 - 150)) / (2^(3!)) + (7!)^25** (which is equals to **3.637168415833E92**)
- **(-2^3) * (-(3! + 4) / 2) + 5** (which is equals to **45**)
- **2 + -3! * (-4^2) / (5 - 3)** (which is equals to **50**)

All the results previously shown were obtained with JCalc and checked using an online calculator app provided by [Desmos](https://www.desmos.com/about) in [this website](https://www.desmos.com/scientific).

## Getting the library

### Building the library locally

To build the library locally, the first thing you must do is to make sure that you have a valid installation of the [Java Development Kit (JDK)](https://en.wikipedia.org/wiki/Java_Development_Kit). That can be easily checked by executing the following command in a terminal:

```bash
java --version
```

> [!TIP]
>
> The minimum Java version required to build this library is [Java 8](https://en.m.wikipedia.org/wiki/Java_version_history#Java_8).

If the execution of that command generates any output instead of an error message, then your installation of the JDK should be correct; and after effectively checking the JDK installation, you must create a local copy of this repo in your device. In order to do that, launch a terminal and execute the following command:

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

Once the execution of that command is done, open the `jcalc` directory, then open the `builds` directory and, once you are watching its content, you'll see a folder named `libs`. In that last folder you'll find compiled files of the library, the [Javadocs](https://en.wikipedia.org/wiki/Javadoc) and the source code compressed as single JAR files that you can use as you wish.

### Getting the library from JitPack

To ease the process of getting a compiled version of the library that you can use directly in your projects, you can use JitPack. In order to do that, follow the official guide [provided here](https://jitpack.io/#jr20xx/JCalc) by JitPack.

## Usage

### Supported operators

JCalc comes with support for numbers written using a variant of the [Scientific notation](https://en.wikipedia.org/wiki/Scientific_notation) named [E notation](https://en.wikipedia.org/wiki/Scientific_notation#E_notation). Besides that, you can also make use of parentheses ("(" and ")") and the following table shows the set of symbols or tokens currently supported. You can use any of those operators when writing the Math expressions and any other operator or value not included in the table is currently not supported. If you use any symbol not included in the table, an exception will be thrown.

<table>
    <tr>
        <th colspan="2">Basic operators</th> <th colspan="2">Advanced operators</th>
    </tr>
    <tr>
        <td>Addition</td> <td>+</td>
        <td>Sine</td> <td>"sin"</td>
    </tr>
    <tr>
        <td>Subtraction</td> <td>-</td>
        <td>Cosine</td> <td>"cos"</td>
    </tr>
    <tr>
        <td>Multiplication</td> <td>* or ×</td>
        <td>Tangent</td> <td>"tan"</td>
    </tr>
    <tr>
        <td>Division</td> <td>/ or ÷</td>
        <td>Arcsine</td> <td>"asin" or "arcsin"</td>
    </tr>
    <tr>
        <td>Exponentiation</td> <td>^</td>
        <td>Arccosine</td> <td>"acos" or "arccos"</td>
    </tr>
    <tr>
        <td>Square root</td> <td>√</td>
        <td>Arctangent</td> <td>"atan" or "arctan"</td>
    </tr>
    <tr>
        <td>Factorial</td> <td>!</td>
        <td>Natural logarithm</td> <td>"ln"</td>
    </tr>
    <tr>
        <td></td> <td></td>
        <td>Base 10 logarithm</td> <td>"log"</td>
    </tr>
</table>

> [!WARNING]
>
>- The support for the advanced operators is still under development.
>- Trigonometric functions are made to work with radians.

### Basic usage

To make use of this library, you simply have to call the method `JCalc.solveMathExpression(...)` providing it with a String containing the Math expression that you want to get solved and a boolean value used to specify when to automatically attempt to balance the parentheses in the given Math expression. When called, that method will return another String with the result of solving the given Math expression; but if the given expression is empty or contains only whitespaces, `null` will be returned instead of any result. In addition to all that, if the Math expression contains any whitespace, they'll be automatically removed.

Here's a clear code example for you to get an idea of how to work with the library and to allow you to know which is the method that must be called to get the work done:

```java
String expression = "3 + 4 * 2 / (1 - 5)^2^3";
String result = JCalc.solveMathExpression(expression, true);
System.out.print(result); // Prints "3.000122070313"
```

### Exceptions handling

This library contains a small set of custom exceptions that should be controlled to guarantee that the execution of the program doesn't get interrupted or glitched. Here's a Java snippet showing all of them with added comments explaining when they are expected to happen:

```java
try {
    String expression = "2 * 3 + 5 * 2^3)";
    String result = JCalc.solveMathExpression(expression, true);
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

## About the docs

The code included in this library includes [Javadocs](https://en.wikipedia.org/wiki/Javadoc) comments nearly everywhere and the rest of the code will be documented in the same way soon. Thanks to JitPack, you can read the online version of the Javadocs by visiting [this website](https://jitpack.io/com/github/jr20xx/JCalc/latest/javadoc/).

## Contribution

We value a lot any kind of contribution and we encourage you to submit pull requests and provide tutorials or other relevant content that might help to improve this library or extend its functionalities.

You can also contribute to this repo by adding a star to it and/or sharing the link if you find it helpful in some way. Any form of help will be highly appreciated.