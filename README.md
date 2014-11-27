# Syntactic Analyzer

> The treatment of languages is an essential part of computer science. As such, writing a syntactic analyzer 
as a way to better understand the behaviour of common compilers is quite interesting from the academic point 
of view.

This tool can help students understand how the process of syntactic analysis is performed, it includes two distinct parsers:

  - A Top-Down parser: LL(1) (Left-to-right, Leftmost derivation)
  - A Bottom-Up parser: SLR(1) (Simple Left-to-right, Rightmost derivation)

Also, this implementation shows the internal AT (Action / Transition) table for the SLR parser, list of items generated from the grammar's rules, director's symbols for the LL parser, as well as the derivation sequence from an input string.

### Requeriments

* Java JDK 6 (or higher)
* Git
* Ant (not mandatory)

### Usage

**Download**:
```sh
$ git clone https://github.com/wynkth/SyntacticAnalyzer.git
$ cd SyntacticAnalyzer
```

**Compile**:
```sh
$ ant compile
$ ant jar
```

**Run**:
```
$ java -jar Analyzer.jar
```

(You can also compile and run with javac and java, respectively)

### Grammar formats
* *GRA grammar*
* *DAT grammar*

(You can check the examples on the 'grammars' folder)

### Implementation notes
* SLR Analysis: In case of conflict, reduction is applied.

