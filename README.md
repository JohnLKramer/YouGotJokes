# YouGotJokes
Joke of the Day CRUD Service

## Running the service

1. Download, install, and run [Docker Desktop](https://www.docker.com/products/docker-desktop/)
2. Install Java 21+ and verify it using `java -version` (I like https://sdkman.io/ to manage my JDKs)
3. Run unit tests `./bin/run_tests.sh`
or
4. Start the server `./bin/start_server.sh`
5. Use swagger to hit the endpoints at http://localhost:8080/swagger-ui/index.html or whatever client you please

If you make changes, you may need to reformat your code `./bin/format_code.sh` in order to pass linting
