# bikeshare-rental

## Running the microservice

Create docker network `bikeshare`:

```bash
docker network create bikeshare
```

Start the database with `docker-compose`:

```bash
docker-compose up
```

Build the image and start the microservice:

```bash
mvn clean package
docker build -t bikeshare-rental:snapshot .
docker run  --name bikeshare-rental \
            -p 8081:8080 \
            --network bikeshare \
            -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://bikeshare-rental.db.bikeshare:5433/bikeshare-rental \
            --rm \
            bikeshare-rental:snapshot
```
