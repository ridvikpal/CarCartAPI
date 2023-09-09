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
            * [Example](#example)
        * [Return a Specific Make and Model](#return-a-specific-make-and-model)
            * [Example](#example-1)
        * [Recommend a Specific Type and Model](#recommend-a-specific-type-and-model)
            * [Example](#example-2)
    * [Features to Implement in the Future](#features-to-implement-in-the-future)

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
from [here](http://www.kaggle.com/datasets/rupeshraundal/marketcheck-automotive-data-us-canada). However, the API will
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
parameter `make`. You can also provide another optional `boolean` parameter called `low_price` to sort the listings by
the lowest price.

#### Example

For example, assuming you want to find listings from Ferrari, you would go to the
URL: http://localhost:8080/api/v1/search_make?make=Ferrari. This would then return the following information:

```json
"databaseListings": [
    {
    "id": 127,
    "guid": "0658ae22-42ab",
    "vin": "ZFF83CLA8K0243317",
    "price": "395990",
    "miles": "4892",
    "year": "2019",
    "make": "Ferrari",
    "model": "812",
    "trim": "Base",
    "drivetrain": "RWD",
    "stockNo": "PN164",
    "bodyType": "Coupe",
    "vehicleType": "Car",
    "fuelType": "Premium Unleaded",
    "engineSize": "6.5",
    "engineBlock": "V",
    "sellerName": "wide world ferrari",
    "transmission": "Automatic",
    "street": "101 Route 59",
    "city": "Spring Valley",
    "state": "NY",
    "zip": "10977"
    },
    {
    "id": 195,
    "guid": "aaf0584c-f27d",
    "vin": "ZFF78VHA0F0213610",
    "price": "679985",
    "miles": "1299",
    "year": "2015",
    "make": "Ferrari",
    "model": "458 Speciale A",
    "trim": "Base",
    "drivetrain": "RWD",
    "stockNo": "T213610",
    "bodyType": "Convertible",
    "vehicleType": "Car",
    "fuelType": "Unleaded",
    "engineSize": "4.5",
    "engineBlock": "V",
    "sellerName": "maserati of central florida",
    "transmission": "Automatic",
    "street": "4891 Vineland Rd",
    "city": "Orlando",
    "state": "FL",
    "zip": "32811"
    },
...
```

### Return a Specific Make and Model

You can easily find used car listings for a specific make and model using the URL
extension: [api/v1/search_model?make=make_name&model=model_name](api/v1/search_model?make=make_name&model=model_name).
You must provide the `String` parameters `make` and `model`. You can also provide another optional `boolean` parameter
called `low_price` to sort the listings by the lowest price.

#### Example

For example, assuming you wanted to find listings for a Ferrari 458 (the last Ferrari with a naturally aspirated engine)
sorted by the lowest price, you can go to the
URL: http://localhost:8080/api/v1/search_model?make=Ferrari&model=458&low_price=true.
This would then return the following information:

```json
"databaseListings": [
    {
    "id": 5312,
    "guid": "f241ab8b-eb67",
    "vin": "ZFF67NFAXE0198307",
    "price": "",
    "miles": "",
    "year": "2014",
    "make": "Ferrari",
    "model": "458 Italia",
    "trim": "Base",
    "drivetrain": "RWD",
    "stockNo": "",
    "bodyType": "Coupe",
    "vehicleType": "Car",
    "fuelType": "Premium Unleaded",
    "engineSize": "4.5",
    "engineBlock": "V",
    "sellerName": "reynolds auto group",
    "transmission": "Automatic",
    "street": "",
    "city": "Dayton",
    "state": "OH",
    "zip": "45430"
    },
    {
    "id": 1356,
    "guid": "cd8edc52-3b17",
    "vin": "ZFF67NFAXF0203975",
    "price": "",
    "miles": "12551",
    "year": "2015",
    "make": "Ferrari",
    "model": "458 Italia",
    "trim": "Base",
    "drivetrain": "RWD",
    "stockNo": "",
    "bodyType": "Coupe",
    "vehicleType": "Car",
    "fuelType": "Premium Unleaded",
    "engineSize": "4.5",
    "engineBlock": "V",
    "sellerName": "0 to 60 motorsports",
    "transmission": "Automatic",
    "street": "1068 Main St",
    "city": "Willimantic",
    "state": "CT",
    "zip": "06226"
    },
...
```

Notice that the entries with no price are listed first, since we are sorting by the lowest price.

### Recommend a Specific Type and Model

You can easily find used car listings and get a recommendation for a specific type of car using the URL
extension: [api/v1/recommendation?type=car_type](api/v1/recommendation?type=car_type). You must provide the`String`
parameter `type`. You can also find a recommendation for a specific type of car from a specific make as well via
the `String` parameter `make`.

#### Example

For example, to find listings for SUVs from Mercedes-Benz, you can go to the
URL: http://localhost:8080/api/v1/recommendation?type=SUV&make=Mercedes-Benz. This would return the following
information:

```json
"databaseListings": [
    {
    "id": 43,
    "guid": "f454a84d-ced8",
    "vin": "4JGBF7BE4BA647986",
    "price": "16055",
    "miles": "159645",
    "year": "2011",
    "make": "Mercedes-Benz",
    "model": "GL-Class",
    "trim": "GL450",
    "drivetrain": "4WD",
    "stockNo": "B117986",
    "bodyType": "SUV",
    "vehicleType": "Truck",
    "fuelType": "Premium Unleaded",
    "engineSize": "4.7",
    "engineBlock": "V",
    "sellerName": "ciocca chevrolet of west chester",
    "transmission": "Automatic",
    "street": "715 Autopark Blvd",
    "city": "West Chester",
    "state": "PA",
    "zip": "19382"
    },
    {
    "id": 87,
    "guid": "3592b0c4-64b8",
    "vin": "4JGDF7DE3FA579286",
    "price": "28595",
    "miles": "93805",
    "year": "2015",
    "make": "Mercedes-Benz",
    "model": "GL-Class",
    "trim": "GL550",
    "drivetrain": "4WD",
    "stockNo": "J21205-2",
    "bodyType": "SUV",
    "vehicleType": "Truck",
    "fuelType": "Premium Unleaded",
    "engineSize": "4.7",
    "engineBlock": "V",
    "sellerName": "jaguar dallas",
    "transmission": "Automatic",
    "street": "11400 North Central Expy",
    "city": "Dallas",
    "state": "TX",
    "zip": "75243"
    },
...
```

## Features to Implement in the Future

There are lots of features I would like to implement once I get the time:

* Filtering entries by date. For example, the user may only like to see listings between a specific range, or sort by
  the newest listings first.
* Sorting by car trim. For example, the user may only want the Touring Version of the Honda Civic.
* Sorting by car specifications, including engine size, drivetrain, transmission, and fuel type. For example, a user may
  only want a car that is 4WD or AWD.
* Sorting by mileage. The user may want a low mileage car and prioritize those listings.
* Creating a website using this API by adding a front-end view.
