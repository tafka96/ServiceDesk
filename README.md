# ServiceDesk
A simple service tesk application. The backend is done with Java and Spring Boot, with H2 database. The front end is in React.

## Run Application on Development server

Run `gradlew bootrun` after cloning the repository. The first time setup might take a while to download all the neccessary node modules. 
After that the application can be accessed at `http://localhost:8080/`.

## Running unit tests
Backend unit tests:
Run `gradlew test` to run unit tests in the backend

Frontend:
Move to webapp directory: `cd webapp`
Run `npm install` if you have not run the `gradlew bootrun` yet.
Run `npm test` to run tests. 

## Most difficult parts
The most difficult part about this application must be the front end with React and unit tests with React. I have chosen to use React since it might benefit me in the future but this was the first time for me using it. I found it to be quite confusing at times.
