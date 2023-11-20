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