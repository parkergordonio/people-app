# People App
A front-end React/AntD and back-end Scala API Service allowing users to interact with people data.
![](https://docs.google.com/drawings/d/e/2PACX-1vTAa_83dKvZWfRBGcBkF56Axp-8cPZkdax_9xnHRzuQ8al8cmNspITcUOzWGrCY6Au3G0bKhccRG6MD/pub?w=935&h=596)

## Running the Services
When running the backend API service, you must have the environment variable "SALESLOFT_API_KEY" set in your current session.

### Prod Mode (Docker/docker-compose)
#### Running
For now the API service needs to be built before hand in order to run the apps with docker-compose. To do this:

```sh
$ cd people-app
$ ./build.sh
```

You can run all the services required for people app by running the script 

```sh
 ./start-services.sh
```

This will `docker-compose up` the frontend(people-app-ui) docker-compose file as well as the backend API(people-app).

#### Stopping
``` sh
./stop-services.sh
```

### From Source
When doing development on servies you should run them from source. To do this follow the README's within the `people-app-ui` and `people-app` directories.

## Architecture
### API Services
The backend is a Scala based Play! web service
### UI
The UI is a React/Ant Design app with Serve as the Httpserver.

## Running Tests
Follow the instructions in each app README
