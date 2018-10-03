# People App

## Running the Services
### In Docker Compose
You can run all the services required for people app by running the script `./start-services.sh`.
This will `docker-compose up` the frontend(people-app-ui) docker-compose file as well as the backend(people-app) services.
### From Source
Follow the README's within the `people-app-ui` and `people-app` directories

## Architecture
### API Services
The backend is a Scala based Play! web service
### UI
The UI is a React/Ant Design app with Serve as the Httpserver.
