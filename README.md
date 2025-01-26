# AnnotationParser

AnnotationParser is a Java-based tool designed to analyze `.jar` files and report all usages of annotations. It scans for annotations within classes and generates a report of which classes implement which annotations.

## Features

- Scans a given JAR file for annotation usages.
- Reports which classes use which annotations (class level, field level, method level, paramenter level annotations).
- Uses ASM library for efficient bytecode analysis.
- Modular and extensible design.

## Project Structure

The project consists of the following main components:

- **`MainApplication.java`**: The entry point of the application that coordinates the execution.
- **`algorithms` package**: Contains `JarAnnotationParser` and its implementation using ASM (`JarAnnotationParserASM`) to parse annotations.
- **`util` package**: Utility classes such as `JarFilePathUtil` and `AnnotationCollector`, offering essential helper methods.
- **`test` package**: JUnit test cases to ensure the parser works as expected.

### Key Files

- **`MainApplication.java`**: Executes the parsing process.
- **`JarAnnotationParserASM.java`**: Core class implementing annotation parsing using the ASM library.
- **`JarAnnotationParserASMTest.java`**: Unit tests that validate the functionality of the `JarAnnotationParserASM` class.

## Prerequisites

- **Java 17 or later**: The project uses Java SDK 17.
- **Maven**: Used to manage dependencies and build the project.

---

## Getting Started

### 1. Clone the Project

To get started, clone the repository to your local machine:

```bash
git clone https://github.com/kartikMahendru/AnnotationParser
cd AnnotationParser/parser
```

### 2. Build the Project

Use Maven to build the project and download the required dependencies:

```bash
mvn clean install
```

### 3. Run the Application

Once the project is built, run the application:

```bash
java -cp target/parser-1.0.jar org.homeassignment.MainApplication <path_to_jar_file>
```

Replace `<path_to_jar_file>` with the path to the JAR file you want to analyze.
One can also run the MainApplication class using intellij UI for debugging purposes.

### Example

```bash
java -cp target/parser-1.0.jar org.homeassignment.MainApplication /path/to/sample.jar
```

This will generate a report listing all annotations found and their corresponding classes.

---

## Running Tests

JUnit tests are included to validate the functionality. Use Maven to execute the test suite:

```bash
mvn test
```

Test files are located in the `src/test/java` directory.
The sample jar files used by test cases can also be used by someone who wants to run the MainApplication class.
The sample jar files are located under `src > test > resources`

### Example Test Files

- `JarAnnotationParserASMTest.java`: Tests the parsing logic to ensure accurate detection of annotations.
- `TestUtils.java`: Helper utilities for testing.

---

## Dependencies

The following libraries are used in the project:

- **ASM (org.ow2.asm:asm)**: Handles JVM bytecode parsing.
- **JUnit 5**: For writing and running test cases.

Dependency details can be found in the `pom.xml` file.

---

## Contribution Guidelines

Contributions are welcome! Please follow the guidelines for submitting a pull request.

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Open a pull request.

---

## Acknowledgments

- This project uses ASM for efficient bytecode manipulation.
- Inspired by the need to analyze annotation usage within complex Java applications.
