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

## Install & Run

These are the instructions to compile and run the app in command line.
You can also use whatever is available from your favorite IDE.

1. Clone the repository using git (see Code button above), move to the created folder
2. Build an executable: `mvn package`
3. Run: `java -jar target/maVille-1.0-SNAPSHOT.jar`

You can also run tests: `mvn test`
