# Wallet server #

gRPC-based wallet server implementation

There are 3 methods supported for this server:
* **Deposit** 
* **Withdraw**
* **Balance**

For the purpose of simplicity, there is no user management involved and new
 "users" get created automatically when the server receives a request with
  unknown user id
  
See [Protocol Documentation](protobuf.md) for detailed information on request
 and response format
 
# Setup #

**Requirements**
* MySQL database (tested on version 5.7.24)

First you need to point the server to an existing MySQL database. Edit 
`src/main/resources/application.properties` to set proper database connection parameters if 
necessary. Default settings point to a test database that will only be available until September 
1st, 2019. One of the easier ways to start a MySQL instance if you don't have one would be using 
docker
```
docker pull mysql/mysql-server:5.7.24

docker run --name mysql -p 3306:3306 -e MYSQL_USER=wallet -e MYSQL_PASSWORD=12345 -e 
MYSQL_DATABASE=wallet mysql/mysql-server:5.7.24
```

To start a server just run
```
./gradlew bootRun
```
Or you can run
```
./gradlew bootJar
```
to package the program into JAR file and run it on another machine using
```
java -jar wallet-server-0.0.1.jar
```

There is an instance of the server running on `walletserver.buzenets.dev:6565` so you don't need to set it up if you only want to test that the client works

# Notes #
[Client implementation](https://github.com/ybuzenets/wallet-client)
