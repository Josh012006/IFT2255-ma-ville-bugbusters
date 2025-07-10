[![Compiler et tester](https://github.com/IFT-2255/ift2255-ma-ville-bugbusters/actions/workflows/test.yml/badge.svg)](https://github.com/IFT-2255/ift2255-ma-ville-bugbusters/actions/workflows/test.yml)

# MAVILLE Application

Requires Java 21 and Maven.

## Project description

MaVille is an app built to help the STPM agents manage correctly and easily public works. It's made in other to be a quick and simple
way for agents, city residents and providers to communicate and keep updated regarding what happens in the city of Montreal.
It includes features such as :

- For residents
    - Problem reporting
    - Real-time updates on public works projects
- For service providers
    - Real-time access to reported problems
    - Ease of proposing projects to help the community

## Project structure

- `README.md`  -> This file ;)
- `pom.xml`    -> Maven project information
- `.gitignore` -> Specifies which files are ignored by git 
- `src/main`   -> Code for the app
- `src/test`   -> Code for the tests
- `rapport/`   -> The engineering report written in HTML
- `visual_paradigm/` -> All our visual paradigm files

## Install, Run & Test 

These are the instructions to compile, test and run the app in command line.

1. First you will need to clone the repository using git (see Code button above) and then move to the created folder

```bash
  git clone https://github.com/IFT-2255/ift2255-ma-ville-bugbusters.git
  cd ift2255-ma-ville-bugbusters
```


2. To Install & Run the app

```bash
  mvn clean package
  java -jar target/maVille-2.0-SNAPSHOT-jar-with-dependencies.jar
```


4. To run the JUnit Tests

```bash
  mvn test
```
