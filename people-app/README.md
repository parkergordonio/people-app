# people-app
This is a Scala/Play/Akka API application to view people data!

## Running
When running the backend API service, you must have the environment variable "SALESLOFT_API_KEY" set in your current session.

### From Source
Run the people-app from source with the prepackaged [sbt](http://www.scala-sbt.org/) (Simple build tool).

```bash
./sbt run
```

And then go to <http://localhost:9000> to see the running web application.

## Tests
To run unit and functional tests, run:

```sh
$ ./sbt test
```