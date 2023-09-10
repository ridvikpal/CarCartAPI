# CarCartAPI

<!-- TOC -->
* [CarCartAPI](#carcartapi)
  * [Introduction](#introduction)
  * [Goals](#goals)
  * [Maven Dependencies](#maven-dependencies)
  * [Configuration for Application Properties File](#configuration-for-application-properties-file)
  * [Design](#design)
  * [Features](#features)
    * [GET Used Car Listings](#get-used-car-listings)
      * [Find a Specific Make](#find-a-specific-make)
        * [Example](#example)
      * [Return a Specific Make and Model](#return-a-specific-make-and-model)
        * [Example](#example-1)
      * [Return a Specific Type and Make](#return-a-specific-type-and-make)
        * [Example](#example-2)
    * [GET OpenAI ChatGPT Integration](#get-openai-chatgpt-integration)
      * [Example for Make Search](#example-for-make-search)
      * [Example for Model Search](#example-for-model-search)
      * [Example for Car Type Recommendation](#example-for-car-type-recommendation)
    * [POST Adding a Car Listing](#post-adding-a-car-listing)
      * [Example](#example-3)
    * [PUT Updating a Car Listing](#put-updating-a-car-listing)
      * [Example](#example-4)
    * [DELETE Car Listing](#delete-car-listing)
      * [Example](#example-5)
    * [Multithreading](#multithreading)
  * [Example of a Full GET API Call](#example-of-a-full-get-api-call)
  * [Features to Implement in the Future](#features-to-implement-in-the-future)
<!-- TOC -->

## Introduction

![Browser API Call](C:\Users\ridvikpal\OneDrive - University of Toronto\Documents\Java Programming\CarCartAPI\src\main\resources\images\browser_api_call.png)

CarCartAPI is a RESTful API that aggregates and manages used car data and information about these cars via a MySQL database and the OpenAPI ChatGPT 3.5
AI model. It is built using Java, Spring Boot, Maven, MySQL and of course OpenAI's own RESTful API for ChatGPT. It obeys
standard REST principles, is designed using the Spring MVC design pattern, and returns data in an easily accessible JSON
format. Currently, this is just an API, but in the future I would like to add multiple webpages to actually view the
data, creating a complete used car listings website!

## Goals

I have always been into cars, and as I approach the age where I will buy my first car with my own money, I thought about
combining my interest in cars with my interest in programming. Thus, the CarCartAPI was born! My first car was
definitely going to be a used one (new cars provide terrible ROI), so I figured why not create an API that aggregates
used car data and provides some insight on which car to buy? Additionally, this API was a great opportunity to apply and
improve my Spring Boot, MySQL, REST API and Java skills. Additionally, ChatGPT had always interested me, so it was also
my first time using its API! Unfortunately, the ChatGPT API was not free, but it was a well spent $5!

## Maven Dependencies

CarCartAPI utilizes the following maven dependencies:

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

## Configuration for Application Properties File

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
# Configuration for Multithreading
spring.mvc.async.request-timeout=30m
```

You must provide your own OpenAI key and MySQL database. I have tested the API with a MySQL database preloaded with a
table containing used car data
from [here](http://www.kaggle.com/datasets/rupeshraundal/marketcheck-automotive-data-us-canada). However, the API will
work with any MySQL database with car data, albeit with a bit of modification (changing column names in the
Spring `@Entity`
class and modifying function names in the `@Repository` class). Ideally, the database would be large (the one I used has
7 million entries) and should have aggregated across lots of different websites.

## Design

The API is designed with the Spring MVC pattern to align with industry standard REST principles. It includes a
controller, service, repository, entity, and configuration classes. The organization of the API and by its various classes can be illustrated in the following flow diagram:

![CarCartAPI Class Design Diagram](src/main/resources/images/CarCartAPI%20Design.jpg)

Each class serves the following purpose:

<div align="center">

| Class                          | Description                                                                          |
|--------------------------------|--------------------------------------------------------------------------------------|
| `CarCartAPIApplication`        | The main class for running the Spring Boot Application                               |
| `CarCartAPIConfiguration`      | This class holds the configuration for the multithreading with Spring Boot `@Async`. |
| `CarCartAPIController`         | This class handles the API requests directly (controller class),                     |
| `CarCartAPIService`            | This class connects to services, such as the MySQL database and ChatGPT.             |
| `CarCartAPIRepository`         | The implementation of `JpaRepository` for the MySQL database.                         |
| `CarCartAPIRepositoryThreaded` | A wrapper around `CarCartAPIRepository` providing multithreading support and logging | 
| `CarData`                      | A data class containing the `@Entity` for the MySQL database table.                  |
| `APIDataReturn`                | A data class used for returning both `ChatGPTResponse` and the `CarData`             |
| `ChatGPTConnection`            | This class handles all direct connections to ChatGPT.                                |
| `ChatGPTRequest`               | A data class used in sending requests to the ChatGPT REST API.                       |
| `ChatGPTResponse`              | A data class used in receiving requests from the ChatGPT REST API.                   |
| `Choice`                       | A data class used as a component of `ChatGPTResponse`                                |
| `Message`                      | A data class used as a component of both `ChatGPTResponse` and `ChatGPTRequest`      |
| `Usage`                        | A data class used as a component of `ChatGPTResponse`                                |

</div>

## Features

CarCartAPI has common features for an auto listing API, with the addition of ChatGPT! This means using CarCartAPI,
someone could easily setup a website with no extra backend work; All that is needed is to provide a front end (View)!
Actually, I am planning to create an auto listings website using CarCartAPI when I get the time! All items are returned
with as industry standard JSON objects

### GET Used Car Listings

Used car listings can be returned in a `databaseListings` array made up of `CarData` objects via a `GET` request. You
can find specific makes, models and car types. Note this data comes from a MySQL database. Therefore, you must have such
a database and set it up in the `application.properties` file. See the `application.properties` section for more
information

#### Find a Specific Make

You can easily find used car listings for a specific make by sending a `GET` request to the URL
extension `carcartapi/search_make`. You must provide the `String`
parameter `make`. You can also provide another optional `boolean` parameter called `low_price` to sort the listings by
the lowest price.

##### Example

For example, assuming you want to find listings from Ferrari, you would go to the
URL: http://localhost:8080/carcartapi/search_make?make=Ferrari. This would then return the following information:

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

Note it is not sorted by the lowest price because we have not specified the `low_price=true` boolean value in the URL.

#### Return a Specific Make and Model

You can easily find used car listings for a specific make and model by sending a `GET` request the URL
extension `carcartapi/search_model`. You must provide the `String` parameters `make` and `model`. You can also provide
another optional `boolean` parameter
called `low_price` to sort the listings by the lowest price.

##### Example

For example, assuming you wanted to find listings for a Ferrari 458 (the last Ferrari with a naturally aspirated engine)
sorted by the lowest price, you can go to the
URL: http://localhost:8080/carcartapi/search_model?make=Ferrari&model=458&low_price=true.
This would then return the following information:

```json
"databaseListings": [
    {
    "id": 23146,
    "guid": "0b876cf0-2b4a",
    "vin": "ZFF75VFA4E0203463",
    "price": "",
    "miles": "6585",
    "year": "2014",
    "make": "Ferrari",
    "model": "458 Speciale",
    "trim": "Base",
    "drivetrain": "RWD",
    "stockNo": "C19165",
    "bodyType": "Coupe",
    "vehicleType": "Car",
    "fuelType": "Premium Unleaded",
    "engineSize": "4.5",
    "engineBlock": "V",
    "sellerName": "the collection maserati",
    "transmission": "Automatic",
    "street": "200 Bird Rd",
    "city": "Coral Gables",
    "state": "FL",
    "zip": "33146"
    },
    {
    "id": 7892,
    "guid": "95e151bc-9626",
    "vin": "ZFF67NFA9A0175322",
    "price": "142998",
    "miles": "61179",
    "year": "2010",
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
    "sellerName": "carrio motor cars",
    "transmission": "Automatic",
    "street": "2300 North State Road 7",
    "city": "Lauderdale Lakes",
    "state": "FL",
    "zip": "33313"
    },
...
```

Notice that the entries with no price are listed first, since we are sorting by the lowest price.

#### Return a Specific Type and Make

You can easily find used car listings and for a specific type of car by sending a `GET` request to the URL
extension: `carcartapi/recommendation`. You must provide the`String`
parameter `type`. You can also provide an optional `String` parameter `make` to find listings for a particular type
of car from a specific manufacturer.

##### Example

For example, to find listings for SUVs from Mercedes-Benz, you can go to the
URL: http://localhost:8080/carcartapi/recommendation?type=SUV&make=Mercedes-Benz. This would return the following
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

### GET OpenAI ChatGPT Integration

OpenAI has ChatGPT integration in all it's functions. The idea is, ChatGPT could help users make a decision by providing
them with information formatted in the way a friend would talk to you. For example, assume you are searching for a used
Lexus ES (one of my personal favourite cars), ChatGPT can tell you about the car, what a good used price is, common
problems with the car, and how it is reviewed by other people. Or if you were looking for a new SUV, then ChatGPT could
recommend you 5 good SUVs based on, say Car and Driver (one of the most trusted car review companies) reviews. Thus,
ChatGPT inherently improves your decision-making capability when buying a used car. The ChatGPT information is returned
in
the `chatGptInfo` JSON object.

#### Example for Make Search

For example, assume you are searching for cars from Ferrari using the following
URL: http://localhost:8080/carcartapi/search_make?make=Ferrari. Then ChatGPT would recommend you:

```json
"chatGptInfo": "Ferrari is an Italian luxury car manufacturer known for its high-performance sports cars and iconic brand image. Reviews of Ferrari vehicles often highlight their exceptional performance, precise handling, and striking design. However, some reviewers also note that Ferrari cars can be expensive to purchase and maintain, making them less practical for everyday use. In terms of reliability, Ferrari models are generally well-built, but their maintenance costs can be high due to the specialized nature of the brand. \n\nUser experience with Ferrari cars is often described as thrilling and exhilarating, with powerful engines and a sense of exclusivity. However, some users may find the firm suspension and limited interior space less comfortable for longer drives. Overall, Ferrari cars are admired for their performance and heritage, but they may not be the most practical choice for everyone.\n\nHere are five cars offered by Ferrari:\n\n1. Name: Ferrari 488 GTB\n   Engine: 3.9-liter V8\n   Type: Coupe\n   Features: Turbocharged, rear-wheel drive, advanced aerodynamics\n   MSRP: Starting at $262,647\n\n2. Name: Ferrari F8 Tributo\n   Engine: 3.9-liter V8\n   Type: Coupe\n   Features: Turbocharged, rear-wheel drive, advanced driver-assistance systems\n   MSRP: Starting at $276,550\n\n3. Name: Ferrari Portofino\n   Engine: 3.9-liter V8\n   Type: Convertible\n   Features: Retractable hardtop, rear-wheel drive, luxurious interior\n   MSRP: Starting at $215,000\n\n4. Name: Ferrari Roma\n   Engine: 3.9-liter V8\n   Type: Coupe\n   Features: Sleek design, advanced infotainment system, rear-wheel drive\n   MSRP: Starting at $222,420\n\n5. Name: Ferrari SF90 Stradale\n   Engine: 4.0-liter V8 combined with electric motors\n   Type: Hybrid\n   Features: All-wheel drive, high-performance hybrid powertrain, advanced technology\n   MSRP: Starting at $507,300",
```

#### Example for Model Search

For example, assume you are searching for a new Ferrari 458 Italia (the last Ferrari with a naturally aspirated engine)
using the following URL: http://localhost:8080/carcartapi/search_model?make=Ferrari&model=458&low_price=true
Then, ChatGPT would recommend you:

```json
"chatGptInfo": "The Ferrari 458 is a high-performance sports car that was produced by the Italian automaker, Ferrari, from 2009 to 2015. It received generally positive reviews from automotive enthusiasts and experts, who praised its exhilarating performance, precise handling, and stunning design. The car is often regarded as one of Ferrari's all-time greats.\n\nIn terms of reliability, the Ferrari 458 has proven to be a fairly solid and dependable car. However, being a high-performance vehicle, regular maintenance is crucial to keep it in top condition. Routine services, such as oil changes and brake replacements, can be expensive due to the specialized nature of the car and the need for genuine Ferrari parts.\n\nThe cost of operation for a Ferrari 458 can be quite high. It demands premium fuel, and insurance and maintenance costs can be substantial. Additionally, the car's fuel efficiency is not particularly impressive, so it may not be the most economical choice for daily commuting.\n\nUser experience with the Ferrari 458 is generally exceptional. The car offers a thrilling driving experience, with its powerful engine, lightning-fast acceleration, and responsive steering. The interior is luxurious and well-crafted, providing comfort and a sense of exclusivity.\n\nCommon problems with used models of the Ferrari 458 include issues with the dual-clutch transmission, which may experience jerky shifts or failure over time. Additionally, some owners have reported electrical glitches and minor cosmetic flaws. It is crucial to have a thorough pre-purchase inspection and to choose a reputable seller when considering a used Ferrari 458.\n\nThe Ferrari 458 comes packed with impressive features, including a naturally aspirated V8 engine, aerodynamic bodywork, advanced traction control systems, carbon ceramic brakes, and a sophisticated suspension setup. It also offers a range of customizable options for buyers to personalize their cars.\n\nWhen looking for a used Ferrari 458, a good price range would be around $150,000 to $200,000, depending on the model year, condition, mileage, and optional features. As for mileage, it is advisable to look for a car with less than 20,000 miles on the odometer, as higher mileage can potentially indicate more wear and tear or a harder life for the vehicle. However, it is important to consider the overall condition and maintenance history of the car rather than focusing solely on mileage.",
```

#### Example for Car Type Recommendation

For example, assume you are looking for a new SUV from Mercedes and use the following url from the API:
http://localhost:8080/carcartapi/recommendation?type=SUV&make=Mercedes-Benz. Then, ChatGPT will recommend you:

```json
"chatGptInfo": "SUVs, or Sports Utility Vehicles, are a type of vehicle that combines elements of both passenger cars and off-road vehicles. They typically feature a high ground clearance, a spacious interior, and the ability to handle various terrains. Here are some common features found in SUVs:\n\n1. Size and Space: SUVs offer ample seating capacity for passengers and usually have a larger cargo area compared to sedans or hatchbacks.\n\n2. All-Wheel Drive (AWD) or Four-Wheel Drive (4WD): Many SUVs come with AWD or 4WD systems, enhancing their off-road capabilities and providing better traction on slippery or uneven surfaces.\n\n3. Safety Features: SUVs often include advanced safety features like stability control, traction control, anti-lock braking systems, multiple airbags, blind-spot monitoring, lane-keeping assist, and collision avoidance systems.\n\n4. Technology and Connectivity: Modern SUVs come equipped with features like touchscreen infotainment systems, Bluetooth connectivity, smartphone integration, USB ports, advanced navigation systems, and premium audio systems.\n\n5. Comfort and Luxury: SUVs from luxury brands offer plush interiors, high-quality materials, luxurious seating options, climate control systems, panoramic sunroofs, and advanced driver-assist features.\n\n6. Towing Capacity: Many SUVs are designed with towing capabilities, allowing them to haul trailers or boats.\n\nNow, based on Car and Driver reviews, here are some recommended SUV models from Mercedes-Benz:\n\n1. Mercedes-Benz GLE: The GLE offers a comfortable and luxurious cabin, advanced safety features, a powerful engine lineup, and excellent off-road capabilities. It provides a smooth ride and comes with various tech features like a large infotainment display, smartphone integration, and a host of driver-assist systems.\n\n2. Mercedes-Benz GLC: The GLC combines style, comfort, and performance. It features a well-appointed interior, a user-friendly infotainment system, a smooth ride, and a range of engine options. Additionally, it offers good fuel economy and advanced safety features.\n\n3. Mercedes-Benz GLS: The GLS is a large luxury SUV that provides ample seating and cargo space. It offers a comfortable ride, powerful engine choices, advanced technology features, and a refined interior. The GLS also provides excellent towing capacity and a plethora of safety features.\n\nIn terms of reliability, Mercedes-Benz SUVs generally have a good reputation, but like any vehicle, they may encounter some common problems. These can include electrical issues, transmission problems, and occasional brake or suspension concerns. However, regular maintenance and servicing can help mitigate these problems.\n\nMaintenance and cost of operation for Mercedes-Benz SUVs can be higher compared to non-luxury brands due to the premium nature of the vehicles. Genuine parts and specialized servicing can contribute to higher maintenance costs.\n\nWhen it comes to pricing, Mercedes-Benz SUVs tend to be on the higher end of the spectrum due to their luxury status. Prices can vary depending on the specific model, trim level, optional features, and region.\n\nUser experience with Mercedes-Benz SUVs is generally positive, with drivers appreciating the comfort, performance, and advanced features. However, personal preferences and experiences can vary.\n\nUltimately, it is recommended to visit a Mercedes-Benz dealership, consult the official website, or refer to trusted automotive sources for the most accurate and up-to-date information on features, pricing, reliability, and common problems associated with specific Mercedes-Benz SUV models.",
```

### POST Adding a Car Listing

A common use case for a car listing service is when a user wants to put their car up for listing. CarCartAPI easily
allows for this to happen via a `POST` request to the URL extension `carcartapi/add_car`. You simply pass a `CarData`
object as the response body, without an `Id` (one is generated for the listing automatically) and CarCartAPI will add it
to the MySQL database. It will also return the added `CarData` object for the user's reference.

#### Example

For example, assume you wanted to add a listing for your 2011 Lexus IS250 that you are going to sell. You would send
a `POST` request to http://localhost:8080/carcartapi/add_car with the response body as follows:

```json
{
  "guid": "581d491a-903d",
  "vin": "JTHFF2C26B2515141",
  "price": "13000",
  "miles": "100000",
  "year": "2011",
  "make": "Lexus",
  "model": "IS250",
  "trim": "Sport",
  "drivetrain": "AWD",
  "stockNo": "",
  "bodyType": "Sedan",
  "vehicleType": "Car",
  "fuelType": "Unleaded",
  "engineSize": "2.5",
  "engineBlock": "V6",
  "sellerName": "private seller",
  "transmission": "Automatic",
  "street": "5400 Westside Dr",
  "city": "Dubuque",
  "state": "IA",
  "zip": "52003"
}
```

Your lexus would then be added to the database with its own ID, and the object would also be returned to you for your
reference!

### PUT Updating a Car Listing

Oftentimes, the user may want to update the specifications of their listing to attract more customers. This can be done
using the `PUT` request to CarCartAPI at the URL extension `carcartapi/update_car`. You can provide the following
optional parameters to update after providing the mandatory `id` parameter in the URL:

* Updated `price`
* Updated mileage as `miles`
* New seller name as `seller name`
* New `street`
* New `city`
* New `state`
* New `zip`

Then, CarCartAPI will update the matching entry and also return the updated `CarData` object for your reference.

#### Example

Assume you wanted to update the price for your 2011 Lexus IS250 you put on sale in the `POST` example with `id = 32568`.
You can simply send a `PUT` request to http://localhost:8080/carcartapi/update_car?id=32568&price=10000. This will then
update the entry and return the following `CarData` object:

```json
{
  "id": 32568
  "guid": "581d491a-903d",
  "vin": "JTHFF2C26B2515141",
  "price": "10000",
  "miles": "100000",
  "year": "2011",
  "make": "Lexus",
  "model": "IS250",
  "trim": "Sport",
  "drivetrain": "AWD",
  "stockNo": "",
  "bodyType": "Sedan",
  "vehicleType": "Car",
  "fuelType": "Unleaded",
  "engineSize": "2.5",
  "engineBlock": "V6",
  "sellerName": "private seller",
  "transmission": "Automatic",
  "street": "5400 Westside Dr",
  "city": "Dubuque",
  "state": "IA",
  "zip": "52003"
}
```

Note the price has changed from 13000 to 10000

### DELETE Car Listing

Assuming a user's car has been sold, they would like to remove the listing. This can be done by sending the `DELETE`
request to CarCartAPI at the URL Extension `carcartapi/delete_car` with the `id` of the listing you would like to
delete. THis will also return the deleted `CarData` object for your reference.

#### Example

Assume your 2011 Lexus IS250 has been sold with `id = 32568`. Then, you can delete it from the database by sending
a `DELETE` request to http://localhost:8080/carcartapi/delete_car?id=32568. This will delete your listing and return the
deleted `CarData` object:

```json
{
  "id": 32568
  "guid": "581d491a-903d",
  "vin": "JTHFF2C26B2515141",
  "price": "10000",
  "miles": "100000",
  "year": "2011",
  "make": "Lexus",
  "model": "IS250",
  "trim": "Sport",
  "drivetrain": "AWD",
  "stockNo": "",
  "bodyType": "Sedan",
  "vehicleType": "Car",
  "fuelType": "Unleaded",
  "engineSize": "2.5",
  "engineBlock": "V6",
  "sellerName": "private seller",
  "transmission": "Automatic",
  "street": "5400 Westside Dr",
  "city": "Dubuque",
  "state": "IA",
  "zip": "52003"
}
```

### Multithreading

All calls to CarCartAPI are multithreaded using Spring Boot `@Async` calls and `ThreadPoolTaskExecutor`. This includes:

1. Controller Requests
2. ChatGPT API calls
3. Database Requests

This makes CarCartAPI much faster. The main limiting speed for CarCartAPI from my testing is the ChatGPT API. If we
utilize the
enterprise license and key, then it should be much faster, since OpenAI reserves resources for enterprises. But
currently I have a
cheap consumer license, and that is good enough for reasonably fast speeds under ~15 seconds. All configuration for
Multithreading is done in
the `CarCartAPIConfiguration` class. Please see the design flow chart to understand how this is handled.

## Example of a Full GET API Call

The full API call includes both the `chatGptInfo` and `databaseListings` object. Assume you are searching for a Lexus GX
using the following URL: http://localhost:8080/carcartapi/search_model?make=Lexus&model=GX. This would return the
following
JSON result:

```json
{
  "chatGptInfo": "The Lexus GX is a luxury midsize SUV that offers a combination of off-road capability, comfort, and reliability. It has generally received positive reviews from both consumers and automotive experts. It is known for its strong build quality and dependable performance, making it a reliable choice for buyers.\n\nIn terms of maintenance, the Lexus GX is generally considered to be a low-maintenance vehicle. Regular servicing and oil changes are recommended, but it doesn't have any major reliability issues. The cost of operation, however, can be on the higher side due to its luxury status and the need for premium fuel.\n\nUsers often praise the Lexus GX for its comfortable and well-appointed interior, smooth ride quality, and ample space for both passengers and cargo. It offers a quiet and luxurious driving experience, making long trips enjoyable.\n\nWhen it comes to common problems with used models, some owners have reported issues with the infotainment system, suspension components, and electrical systems. However, these problems are relatively rare and not widespread.\n\nAs for its features, the Lexus GX comes equipped with a range of advanced technologies such as a touchscreen infotainment system, navigation, premium audio system, leather upholstery, heated and ventilated seats, adaptive cruise control, and a host of safety features like blind-spot monitoring and lane departure warning.\n\nFor a good used price and mileage to look for, it is recommended to search for a Lexus GX with around 50,000 to 70,000 miles on the odometer. Prices can vary based on factors such as model year, trim level, and condition, but a reasonable range would be between $30,000 to $40,000 USD.",
  "databaseListings": [
    {
      "id": 83,
      "guid": "aea8a65d-e91c",
      "vin": "JTJBT20X480150436",
      "price": "17995",
      "miles": "107146",
      "year": "2008",
      "make": "Lexus",
      "model": "GX",
      "trim": "470",
      "drivetrain": "4WD",
      "stockNo": "2103800",
      "bodyType": "SUV",
      "vehicleType": "Truck",
      "fuelType": "Premium Unleaded",
      "engineSize": "4.7",
      "engineBlock": "V",
      "sellerName": "damas auto va",
      "transmission": "Automatic",
      "street": "25358 Pleasant Valley Road 120",
      "city": "Chantilly",
      "state": "VA",
      "zip": "20152"
    },
    {
      "id": 151,
      "guid": "b42e6c32-25e9",
      "vin": "JTJBT20X860117128",
      "price": "",
      "miles": "218983",
      "year": "2006",
      "make": "Lexus",
      "model": "GX",
      "trim": "470",
      "drivetrain": "4WD",
      "stockNo": "10202",
      "bodyType": "SUV",
      "vehicleType": "Truck",
      "fuelType": "Premium Unleaded",
      "engineSize": "4.7",
      "engineBlock": "V",
      "sellerName": "cle cars llc",
      "transmission": "Automatic",
      "street": "11111 Lorain Avenue",
      "city": "Cleveland",
      "state": "OH",
      "zip": "44111"
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