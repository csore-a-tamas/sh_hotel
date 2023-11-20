# Overview
The following repo is my solution to a java challenge. <br>
The challenge itself is confidential hence it is not presented in the README. <br>
I will refer to parts without revealing too much from the task itself. <br>

# Task
You can find it in your mailbox: _"Project presentation and next steps ..."_

# Thoughts
Before I would start to work on the task, in a real word scenario I would have questions towards the client.

- Are there any other requirements we should be aware of in the near future?
  - Possible answer:
    - The client is unsure about the current way of offer handling, but they can't add any more information to it, at the moment
    - They are mentioning the need of possible new room categories in the future._
  - Decision:
    - While the offer handling is clearly not well-formed, without further information from the client, it is not advisable to invest extra effort in it
    - Category handling could be a crucial part of the implementation. Since the client mentioned changes, if possible the implementation should be flexible here.

- Are they planning to use other currencies?
  - Possible answer:
    - The clients already has a well build system which handles transactions, and it is unlikely to be updated to other currencies
  - Decision:
    - The current implementation should not care about currencies. Only work with numbers

- Are they planning to use fractions for payment offers?
  - Possible answer:
    - No
  - Decision:
    - Only whole numbers will be accepted

- The given test data is not matches the requirements
  - The description says: "_... customer willing to pay **over** EUR 100 ..._", but from the test data output the functionality is **at least** instead of **over**
     (The output values simply not add up)
    - Decision: use **at least** functionality instead of **over**. This is the least amount of change to have the closest results

  - The Test 4 has an impossible state. 10 premium + 1 economy room could cover all the 10 potential quests instead of the 7-1 coverage the test implies.
    - Decision: Ignore the case in the minimum coverage as there are too many questionable variables

# Configuration

| Env variable                           | Default                                                 | Description                                         |
|:---------------------------------------|:--------------------------------------------------------|:----------------------------------------------------|
| PORT                                   | See [here](/src/main/resources/application.properties)  | The service will listen on this port for HTTP calls |
| HOTEL-MINIMUM-PAYMENT-FOR-PREMIUM-ROOM | See [here](/src/main/resources/application.properties)  | Determinate the room category by payment offer      |

# Usage

## Requirements
- Java 11+
- Maven 3.8.6+

## Run the application

### The IDE of your choice
Just like any other application, you can run this too in your IDE.

### Maven + Java
From the root directory

<details>
<summary>To run the tests:</summary>

```
mvn clean verify -P with-tests
```

You should see something like

```
...
...
...
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 10.65 s -- in com.sh.controllers.HotelControllerIT
2023-11-20 20:04:07,271 INFO  [io.quarkus] (main) sh_hotel stopped in 0.063s
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- maven-failsafe-plugin:3.1.2:verify (default) @ sh_hotel ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  27.939 s
[INFO] Finished at: 2023-11-20T20:04:07+01:00
[INFO] ------------------------------------------------------------------------

```
</details>

<details>
<summary>To build the application</summary>

```
mvn clean package
```

You should see something like

```
...
...
...
[INFO] --- quarkus-maven-plugin:3.5.2:build (default) @ sh_hotel ---
[INFO] [io.quarkus.deployment.pkg.steps.JarResultBuildStep] Building uber jar: ***\sh_hotel\target\sh_hotel-0.0.1-runner.jar
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 4960ms
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  15.451 s
[INFO] Finished at: 2023-11-20T20:07:08+01:00
[INFO] ------------------------------------------------------------------------
```
</details>

<details>
<summary>To run the application after one of the previous steps</summary>

```
java -jar target/sh_hotel-0.0.1-runner.jar
```

You should see something like

```
__  ____  __  _____   ___  __ ____  ______
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/
2023-11-20 20:25:27,332 INFO  [io.quarkus] (main) sh_hotel 0.0.1 on JVM (powered by Quarkus 3.5.2) started in 1.204s. Listening on: http://0.0.0.0:8080
2023-11-20 20:25:27,338 INFO  [io.quarkus] (main) Profile prod activated.
2023-11-20 20:25:27,338 INFO  [io.quarkus] (main) Installed features: [cdi, hibernate-validator, resteasy-reactive, resteasy-reactive-jackson, smallrye-context-propagation, smallrye-health, vertx]
```
</details>


### Docker
From the root directory

<details>
<summary>Build the images</summary>

```
docker build -t sh/hotel_demo_test:0.0.1 -f test.Dockerfile .
docker build -t sh/hotel_demo:0.0.1 -f Dockerfile .
```
</details>

<details>
<summary>Start the containers</summary>

```
docker run -d --name hotel_demo_test -p 8081:8080 sh/hotel_demo_test:0.0.1
docker run -d --name hotel_demo -p 8080:8080 sh/hotel_demo:0.0.1 
```
</details>

<details>
<summary>Check the logs</summary>

```
docker logs hotel_demo_test -f
docker logs hotel_demo -f
```

The tests are running for a while. Eventually you should see something like this:
```
...
...
...
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.504 s -- in com.sh.controllers.HotelControllerIT 2023-11-20 19:30:30,617 INFO  [io.quarkus] (main) sh_hotel stopped in 0.040s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] 
[INFO] --- maven-failsafe-plugin:3.1.2:verify (default) @ sh_hotel ---
[INFO] Failsafe report directory: /target/failsafe-reports
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  47.055 s
[INFO] Finished at: 2023-11-20T19:30:30Z
[INFO] ------------------------------------------------------------------------
```

The application starts very quickly. You should see something like this:
```
__  ____  __  _____   ___  __ ____  ______ 
--/ __ \/ / / / _ | _ \/ //_/ / / / __/ 
-/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2023-11-20 19:29:48,277 INFO  [io.quarkus] (main) sh_hotel 0.0.1 on JVM (powered by Quarkus 3.5.2) started in 1.325s. Listening on: http://0.0.0.0:8080
2023-11-20 19:29:48,281 INFO  [io.quarkus] (main) Profile prod activated. 
2023-11-20 19:29:48,282 INFO  [io.quarkus] (main) Installed features: [cdi, hibernate-validator, resteasy-reactive, resteasy-reactive-jackson, smallrye-context-propagation, smallrye-health, vertx]
```

</details>

<details>
<summary>Stop the container</summary>

```
docker stop hotel_demo
```

the other one stops itself

</details>

## Postman Collection
All the endpoints are presented in the attached [postman collection](misc/postman_collection.json)

# Endpoints

## Health
For both of them we are using [Smallrye-health](https://quarkus.io/guides/smallrye-health)

### Readiness

|                                     |                                                                                                                                                    |
|-------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| Request type                        | `GET`                                                                                                                                              |
| Path                                | `/readiness`                                                                                                                                       |
| Response                            | A JSON with a `status` property [`UP`, `DOWN`] which comes from the `check` entries which is currenly empty as there are no dependencies to check. |

<details>
<summary>Response</summary>

```json
{
    "status": "UP",
    "checks": []
}
```

</details>

### 0.2. Liveness
|                                     |                                                |
|-------------------------------------|------------------------------------------------|
| Request type                        | `GET`                                          |
| Path                                | `/liveness`                                    |
| Response                            | A similar JSON like the `/readiness` endpoint  |

## Hotel

### Occupy

Calculate the best coverage for the rooms from the possible payments.

|              |                                                                         |
|--------------|-------------------------------------------------------------------------|
| Request type | `POST`                                                                  |
| Path         | `/hotel/occupy`                                                         |
| Content type | `application/json`                                                      |
| Request body | [HotelOccupyRequest](src/main/java/com/sh/dtos/HotelOccupyRequest.java) |
| Response     | List of [RoomGroup](src/main/java/com/sh/dtos/RoomGroup.java)           |

<details>
<summary>Request body</summary>

```json
{
  "roomCounts": {
    "PREMIUM": 1,
    "ECONOMY": 2
  },
  "paymentOffers": [
    123,
    321,
    21,
    12
  ]
}
```

</details>
<br>

<details>
<summary>Response</summary>

```json
[
  {
    "category": "PREMIUM",
    "occupiedRoomCount": 1,
    "income": 321
  },
  {
    "category": "ECONOMY",
    "occupiedRoomCount": 2,
    "income": 33
  }
]
```

</details>
<br>