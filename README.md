# CarCostAPI

<!-- TOC -->

* [CarCostAPI](#carcostapi)
    * [Introduction](#introduction)
    * [Goals](#goals)
    * [Maven Dependencies](#maven-dependencies)
    * [Configuration for `application.properties` File](#configuration-for-applicationproperties-file)
    * [Design](#design)
    * [Features](#features)
        * [OpenAI ChatGPT Integration](#openai-chatgpt-integration)
        * [Find a Specific Make](#find-a-specific-make)
        * [Return a Specific Make and Model](#return-a-specific-make-and-model)
        * [Recommend a Specific Type and Model](#recommend-a-specific-type-and-model)
    * [Future Goals](#future-goals)

<!-- TOC -->

## Introduction

CarCost is a RESTful API that aggregates used car data and information about these cars via the OpenAPI ChatGPT 3.5
AI model. It is built using Java, Spring Boot, Maven, MySQL and of course OpenAI's own RESTful API for ChatGPT. It obeys
standard REST principles, is designed using the Spring MVC design pattern, and returns data in an easily accessible JSON
format. Currently, this is just an API, but in the future I would like to add multiple webpages to actually view the
data, creating a complete used car listings website!

## Goals

I have always been into cars, and as I approach the age where I will buy a car with my own money, I thought about
combining my interest in cars with my interest in programming. Thus, the CarCostAPI was born! My first car was
definitely going to be a used one (new cars provide terrible ROI), so I figured why not create an API that aggregates
used car data and provides some insight on which car to buy? Additionally, this API was a great opportunity to apply and
improve my Spring Boot, MySQL, REST API and Java skills. Additionally, ChatGPT had always interested me, so it was also
my first time using its API! Unfortunately, the ChatGPT API was not free, but it was a well spent $5!

## Maven Dependencies

CarCost utilizes the following maven dependencies:

<div align="center">

| Dependency                     | Description                                                  |
|--------------------------------|--------------------------------------------------------------|
| `spring-boot-starter-data-jpa` | Used to connect to the MySQL database                        |
| `spring-boot-starter-web`      | Used to provide a Tomcat server and the Spring MVC Framework |
| `spring-boot-starter-webflux`  | Used to consume the ChatGPT REST API                         |
| `mysql-connector-j`            | JDBC MySQL driver, works with Spring JPA                     |

</div>

Note I used Spring WebFlux, which is part of the Spring Reactive Web for REST API calls. Therefore, I used `WebClient()`
instead of `RestTemplate()`. This is because `WebClient()` is the newer and preferred way to consume REST APIs in
Spring. However, many programs have not moved onto `WebClient()` and are still stuck with the older
deprecated `RestTemplate()`, which will not be supported in future Spring releases.

## Configuration for `application.properties` File

A sample configuration for an `application.properties` file to run this program is:

```properties
# OpenAI key
openai.api.key=your_open_AI_key
# Configuration for MySQL data access
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/your_database_name
spring.datasource.username=your_sql_username
spring.datasource.password=your_sql_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

You must provide your own OpenAI key and MySQL database. I have tested the API with a MySQL database preloaded with a
table containing used car data
from [here](https://www.kaggle.com/datasets/rupeshraundal/marketcheck-automotive-data-us-canada). However, the API will
work with any database with car data, albeit with a bit of modification (changing column names in the Spring `@Entity`
class). Ideally, the database would be large (the one I used has 7 million entries) and should have aggregated across
lots of different websites.

## Design

The API is designed with the Spring MVC pattern to align with industry standard REST principles. It includes a
controller, service, repository, entity, and configuration classes. The organization of the API can be illustrated in
the following flow diagram:

## Features

CarCostAPI has common features for an auto listing API, with the addition of ChatGPT! This means using CarCostAPI,
someone could easily setup a website with no extra backend work; All that is needed is to provide a front end (View)!
Actually, I am planning to create an auto listings website using CarCostAPI when I get the time!

### OpenAI ChatGPT Integration

OpenAI has ChatGPT integration in all it's functions. The idea is, ChatGPT could help users make a decision by providing
them with information formatted in the way a friend would talk to you. For example, assume you are searching for a used
Lexus IS (one of my personal favourite cars), ChatGPT can tell you about the car, what a good used price is, common
problems with the car, and how it is reviewed. Or if you were looking for a new SUV, then ChatGPT could recommend you 5
good SUVs based on, say Car and Driver (one of the most trusted car review companies) reviews. Thus, ChatGPT inherently
improves your decision-making capability when buying a used car.

### Find a Specific Make

You can easily find used car listings for a specific make using the URL
extension: [api/v1/search_make?make=make_name](api/v1/search_make?make=make_name). You must provide the `String`
parameter `make`. For example,
assuming you want to find listings from Ferrari, you would go to the
URL: https://localhost:8080/api/v1/search_make?make=Ferrari.
This would then return the following information:

```json

```

You can also provide another `boolean` parameter called `low_price` to sort the listings by the lowest price. For
example, to find the cheapest Ferraris (relatively speaking, of course), you can go to the
URL: https://localhost:8080/api/v1/search_make?make=Ferrari&low_price=true.

### Return a Specific Make and Model

You can easily find used car listings for a specific make and model using the URL
extension: [api/v1/search_model?make=make_name&model=model_name](api/v1/search_model?make=make_name&model=model_name).
You must provide the `String` parameters `make` and `model`.
For example, assuming you wanted to
find listings for a Ferrari 458 (the last Ferrari with a naturally aspirated engine), you can go to the
URL: https://localhost:8080/api/v1/search_model?make=Ferrari&model=458.
This would then return the following information:

```json

```

You can also provide another `boolean` parameter called `low_price` to sort the listings by the lowest price. For
example, to find the cheapest Ferrari 458 (relatively speaking, of course), you can go to the
URL: https://localhost:8080/api/v1/search_model?make=Ferrari&model=458&low_price=true.

### Recommend a Specific Type and Model

You can easily find used car listings and get a recommendation for a specific type of car using the URL
extension: [api/v1/recommendation?type=car_type](api/v1/recommendation?type=car_type). You must provide the`String`
parameter `type`. For example, assuming you want to find SUVs to buy, you can go to the url: https://localhost:8080/api/v1/recommendation?type=SUV. This would return the following information:

```json

```

You can also find a recommendation for a specific type of car from a specific make as well via the `String` parameter `make`. For example, to find listings for SUVs from Mercedes-Benz, you can go to the URL: https://localhost:8080/api/v1/recommendation?type=SUV&make=Mercedes-Benz. 

## Future Goals