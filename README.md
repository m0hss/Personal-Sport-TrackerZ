# Personal-Sport-TrackerZ
![pst](https://github.com/m0hss/Personal-Sport-TrackerZ/assets/60576085/cfb53ed9-08db-4015-98f4-3a6f229c7cf0)

 [![JDK Version](https://img.shields.io/badge/jdk-v17.0.9-green)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) [![Springboot3 Version](https://img.shields.io/badge/springboot-v3.1.6-green)](https://docs.spring.io/spring-boot/docs/current/reference/html/index.html)  [![springdocopenapi Version](https://img.shields.io/badge/springdoc--openapi-v2.2.0-green)](https://springdoc.org/) [![PostgreSQL Version](https://img.shields.io/badge/PostgreSQL-v15.1-green)](https://www.postgresql.org/download/)
- Personal-Sport-Trackerz REST API (Java17+SpringBoot3+OpenAPI3)
  
## API Reference
#### Add New User

```http
  POST /v1/users
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `user` | `User` | Add new user (Role: USER or COACH) |

#### Available Courses ( COACH Already registred )

```http
  GET /v1/courses
```

| Parameter | Type     | 
| :-------- | :------- | 
| `id`      | `integer` |

#### User Register

```http
  Put /v1/courses/{course_id}/register
```

| Parameter | Type     |
| :-------- | :------- | 
| `courseId`| `@PathVariable integer` |
| `userId`| ` @RequestParam integer` |

#### Unregister User

```http
  GET /v1/users/courses/{courseId}/unregister
```

| Parameter | Type     | 
| :-------- | :------- | 
| `userId`      | `@RequestParam Integer` |  
| `courseId`      | `@PathVariable Integer ` |

#### User All Courses (with state: OPEN or ENDED)

```http
  GET v1/users/{userId}/courses
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `integer` | Fetch user by id |


## Usage/Examples

```java
$ mvnw -v             
Apache Maven 3.9.5 (57804ffe001d7215b5e7bcb531cf83df38f93546)
Maven home: C:\Users\ss\.m2\wrapper\dists\apache-maven-3.9.5-bin\32db9c34\apache-maven-3.9.5
Java version: 17.0.9, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-17  

$ mvn spring-boot:run
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::  (v3.1.6)
....... . . .
....... . . . (log output here)
....... . . .
........ Started MyApplication in 0.906 seconds (process running for 6.514)   

```

## Screenshots

![trackerz](https://github.com/m0hss/Personal-Sport-TrackerZ/assets/60576085/de077242-36f5-4f59-9168-4c1081fa4753)





## License

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
