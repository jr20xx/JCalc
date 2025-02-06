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

> [!WARNING]
>
> This repo is under active development yet. Changes can occur without warning, so use it at your own risk

### Table of Contents
- [Description of the project](#description-of-the-project)
- [Getting the library](#getting-the-library)
    - [Building the library locally](#building-the-library-locally)
    - [Getting the library from JitPack](#getting-the-library-from-jitpack)
- [Usage](#usage)
    - [Basic usage](#basic-usage)
    - [Exceptions handling](#exceptions-handling)
- [About the docs](#about-the-docs)
- [Contribution](#contribution)

## Description of the project

This repo is especially made for those who, for any reason, want to allow users to input basic Math expressions in Java apps and quickly solve them during runtime (like, for example, when building a basic calculator). To make all that possible, a powerful and simple combination of algorithms were used. 

The first algorithm that comes into action is the [Shunting Yard algorithm](https://en.wikipedia.org/wiki/Shunting_yard_algorithm), used to quickly parse the given Math expressions and later process them with the second algorithm used in this library: the [Reverse Polish Notation algorithm](https://en.wikipedia.org/wiki/Reverse_Polish_notation); which is in charge of evaluating the parsed expressions and return their results.

## Getting the library

### Building the library locally

To build the library locally, the first thing you must do is to make sure that you have a valid installation of the [Java Development Kit (JDK)](https://en.wikipedia.org/wiki/Java_Development_Kit). That can be easily checked by executing the following command in a terminal:
```bash
java --version
```

> [!INFO]
>
> The minimum Java version required to build this library is [Java 8](https://en.m.wikipedia.org/wiki/Java_version_history#Java_8).

If the output of that command is similar to the following, then your installation of the JDK should be correct:
```
openjdk 21.0.5 2024-10-15
OpenJDK Runtime Environment (build 21.0.5+11)
OpenJDK 64-Bit Server VM (build 21.0.5+11, mixed mode, sharing)
```

After checking the JDK installation, then you must create a local copy of this repo in your device. In order to do that, launch a terminal and execute the following command:
```bash
git clone https://github.com/jr20xx/JCalc
```

Once the execution of that command is finished, a new directory containing a copy of the files of this project will be created. You can open that folder with a Java IDE with [Gradle](https://gradle.org/) support like [IntelliJ IDEA](https://www.jetbrains.com/idea/), [Android Studio](https://developer.android.com/studio) or [VSCode](https://code.visualstudio.com/) (with the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) installed) and then build the library making use of their integrated tools. If you need help doing that, you can check [this website](https://docs.gradle.org/current/userguide/gradle_ides.html) to get references of how to do that in your prefered IDE.

If you decide to build the library without using any IDE, then open the newly created directory containing the files from this repo and start a terminal session from there. Once you've opened a terminal, run any of the following commands:

_From Linux terminal:_
```bash
./gradlew build
```

_From Windows command line:_
```bash
gradlew.bat build
```

Once the execution of that command is done, open the `jcalc` directory, then open the `builds` directory and, once you are watching its content, you'll see a folder named `libs`. In that last folder you'll find compiled files of the library, the [Javadocs](https://en.wikipedia.org/wiki/Javadoc) and the source code compressed as single JAR files that you can use as you wish.

### Getting the library from JitPack

To ease the process of getting a compiled version of the library that you can use directly in your projects, you can use JitPack. In order to do that, follow the official guide [provided here](https://jitpack.io/#jr20xx/JCalc) by JitPack.

## Usage

### Basic usage

To make use of this library, there's a single method you need to call. When calling that method, you must pass a String containing the Math expression that you want to get solved and that method will return another String with the result of solving the given Math expression. Any whitespace in the Math expression will be automatically removed and if the expression is empty, `null` will be returned instead of any result.

Here's a clear code example for you to get an idea of how to work with the library and to allow you to know which one is the method that must be called to get the work done:
```java
String expression = "3 + 4 * 2 / (1 - 5)^2^3";
String result = JCalc.performMathOperation(expression);
System.out.print(result); // Prints "3.0001220703125"
```
> [!WARNING]
>
> Besides parentheses, the only valid Math symbols that can be used in the expression are *+*, *-*, *\**, *×*, */*, *÷* and *^*. Using any other symbol, will cause an error.

### Exceptions handling

This library contains a small set of custom exceptions that should be controlled to guarantee that the execution of the program doesn't get interupted or glitched. Here's a Java snippet showing all of them with added comments explaining when they are expected to happen:
```java
try {
    String expression = "2 * 3 + 5 * 2^3";
    String result = JCalc.performMathOperation(expression);
} catch (UnbalancedParenthesesException exception) {
    // This exception occurs when parentheses are not placed correctly
} catch (NotNumericResultException exception) {
    // This exception occurs when a not numeric (NaN) value is obtained
} catch (InfiniteResultException exception) {
    // This exception occurs when an Infinite result is obtained
} catch (UnregisteredOperationException exception) {
    // This exception occurs when trying to perform an undefined operation
} catch (Exception exception) {
    // This is recommended in case that an unexpected exception arises
}
```

## About the docs

The code included in this library includes [Javadocs](https://en.wikipedia.org/wiki/Javadoc) comments nearly everywhere and the rest of the code will be documented in the same way soon. Thanks to JitPack, you can read the online version of the Javadocs by visiting [this website](https://jitpack.io/com/github/jr20xx/JCalc/latest/javadoc/).

## Contribution

We value a lot any kind of contribution and we encourage you to submit pull requests and to provide tutorials or other relevant content that might help to improve this library or extend its functionalities. 

You can also contribute to this repo by adding a star to it and/or sharing the link if you find it helpful in some way. Any form of help will be highly appreciated.