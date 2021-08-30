# Implementation Checklist
- [x] API Code
- [x] Services Code
- [x] Unit-tests
- [x] Dockerfile
- [x] It Compiles
- [x] It runs

# Api Services
- [/login](http://localhost:8000/login): Receives a valid username and password and returns a JWT.
- [/protected](http://localhost:8000/protected): Returns protected data with a valid token, otherwise returns unauthenticated.

# How to run
Open a terminal and enter to the java directory, then run the following commands:
- docker build --tag wize-salvador-montiel ./
- docker run -p 8000:8000 --rm -it wize-salvador-montiel:latest

Now, go to: http://localhost:8000/