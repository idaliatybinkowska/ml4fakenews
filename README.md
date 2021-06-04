# ML4FakeNews project

## Running project

### On localhost
1. Download and install rabbitmq
2. Start rabbitmq server 
   - `export PATH=$PATH:/usr/local/sbin`
   - `rabbitmq-server`
3. Run container with database
    - build: `docker build -t ml4fakenews/database:version0.1 .`
    - run: `docker run -p 3307:3306 ml4fakenews/database:version0.1`

4. Run container with ELK or comment logback.xml and pom.xml with logstash
    - Command: `docker-compose up`

5. Run discovery-service
6. Run account/subscribe/email - service
7. Run gateway-service


### On containers
1. Run `docker-compose build` in directory with `docker-compose.yml`
2. Run `docker-compose up` in directory with `docker-compose.yml`


### Links
- Application works on http://localhost:80
    - /accounts
    - /subscriptions
    - /emails
- You can monitor RabbitMQ messages on http://localhost:15672
    - Login _guest_, Password _guest_
- You can monitor logs on http://localhost:5601 > Discover > Add index pattern (logstash-*, timestamp)
    - Login _elastic_, Password _changeme_
    - filter by message:

## Additional commands:
- discovery-service
`docker build -t ml4fakenews/discovery-service:version0.1 .`
`docker run -p 3000:3000 ml4fakenews/discovery-service:version0.1`

- accounts-service
`docker build -t ml4fakenews/accounts:version0.1 .`
`docker run -p 3001:3001 ml4fakenews/accounts:version0.1`

- subscriptions-service
`docker build -t ml4fakenews/subscriptions:version0.1 .`
`docker run -p 3002:3002 ml4fakenews/subscriptions:version0.1`

- emails-service
`docker build -t ml4fakenews/emails:version0.1 .`
`docker run -p 3001:3001 ml4fakenews/emails:version0.1`

- gateway-service
`docker build -t ml4fakenews/gateway-service:version0.1 .`
`docker run -p 80:80 ml4fakenews/gateway-service:version0.1`