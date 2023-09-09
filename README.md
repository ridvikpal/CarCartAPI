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
      * [Example for Make Search](#example-for-make-search)
      * [Example for Model Search](#example-for-model-search)
      * [Example for Car Type Recommendation](#example-for-car-type-recommendation)
    * [Used Car Listings](#used-car-listings)
      * [Find a Specific Make](#find-a-specific-make)
        * [Example](#example)
      * [Return a Specific Make and Model](#return-a-specific-make-and-model)
        * [Example](#example-1)
      * [Recommend a Specific Type and Model](#recommend-a-specific-type-and-model)
        * [Example](#example-2)
    * [Example of a Full API Call](#example-of-a-full-api-call)
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
improves your decision-making capability when buying a used car. The ChatGPT info is returned in the `chatGptInfo` JSON
object.

#### Example for Make Search

For example, assume you are searching for cars from Ferrari using the following
URL: http://localhost:8080/api/v1/search_make?make=Ferrari. Then ChatGPT would recommend you:

```json
{
  "chatGptInfo": "Ferrari is an Italian luxury car manufacturer known for its high-performance sports cars and iconic brand image. Reviews of Ferrari vehicles often highlight their exceptional performance, precise handling, and striking design. However, some reviewers also note that Ferrari cars can be expensive to purchase and maintain, making them less practical for everyday use. In terms of reliability, Ferrari models are generally well-built, but their maintenance costs can be high due to the specialized nature of the brand. \n\nUser experience with Ferrari cars is often described as thrilling and exhilarating, with powerful engines and a sense of exclusivity. However, some users may find the firm suspension and limited interior space less comfortable for longer drives. Overall, Ferrari cars are admired for their performance and heritage, but they may not be the most practical choice for everyone.\n\nHere are five cars offered by Ferrari:\n\n1. Name: Ferrari 488 GTB\n   Engine: 3.9-liter V8\n   Type: Coupe\n   Features: Turbocharged, rear-wheel drive, advanced aerodynamics\n   MSRP: Starting at $262,647\n\n2. Name: Ferrari F8 Tributo\n   Engine: 3.9-liter V8\n   Type: Coupe\n   Features: Turbocharged, rear-wheel drive, advanced driver-assistance systems\n   MSRP: Starting at $276,550\n\n3. Name: Ferrari Portofino\n   Engine: 3.9-liter V8\n   Type: Convertible\n   Features: Retractable hardtop, rear-wheel drive, luxurious interior\n   MSRP: Starting at $215,000\n\n4. Name: Ferrari Roma\n   Engine: 3.9-liter V8\n   Type: Coupe\n   Features: Sleek design, advanced infotainment system, rear-wheel drive\n   MSRP: Starting at $222,420\n\n5. Name: Ferrari SF90 Stradale\n   Engine: 4.0-liter V8 combined with electric motors\n   Type: Hybrid\n   Features: All-wheel drive, high-performance hybrid powertrain, advanced technology\n   MSRP: Starting at $507,300",
```

#### Example for Model Search

For example, assume you are searching for a new Ferrari 458 Italia (the last Ferrari with a naturally aspirated engine)
using the following URL: http://localhost:8080/api/v1/search_model?make=Ferrari&model=458&low_price=true
Then, ChatGPT would recommend you:

```json
{
  "chatGptInfo": "The Ferrari 458 is a high-performance sports car that was produced by the Italian automaker, Ferrari, from 2009 to 2015. It received generally positive reviews from automotive enthusiasts and experts, who praised its exhilarating performance, precise handling, and stunning design. The car is often regarded as one of Ferrari's all-time greats.\n\nIn terms of reliability, the Ferrari 458 has proven to be a fairly solid and dependable car. However, being a high-performance vehicle, regular maintenance is crucial to keep it in top condition. Routine services, such as oil changes and brake replacements, can be expensive due to the specialized nature of the car and the need for genuine Ferrari parts.\n\nThe cost of operation for a Ferrari 458 can be quite high. It demands premium fuel, and insurance and maintenance costs can be substantial. Additionally, the car's fuel efficiency is not particularly impressive, so it may not be the most economical choice for daily commuting.\n\nUser experience with the Ferrari 458 is generally exceptional. The car offers a thrilling driving experience, with its powerful engine, lightning-fast acceleration, and responsive steering. The interior is luxurious and well-crafted, providing comfort and a sense of exclusivity.\n\nCommon problems with used models of the Ferrari 458 include issues with the dual-clutch transmission, which may experience jerky shifts or failure over time. Additionally, some owners have reported electrical glitches and minor cosmetic flaws. It is crucial to have a thorough pre-purchase inspection and to choose a reputable seller when considering a used Ferrari 458.\n\nThe Ferrari 458 comes packed with impressive features, including a naturally aspirated V8 engine, aerodynamic bodywork, advanced traction control systems, carbon ceramic brakes, and a sophisticated suspension setup. It also offers a range of customizable options for buyers to personalize their cars.\n\nWhen looking for a used Ferrari 458, a good price range would be around $150,000 to $200,000, depending on the model year, condition, mileage, and optional features. As for mileage, it is advisable to look for a car with less than 20,000 miles on the odometer, as higher mileage can potentially indicate more wear and tear or a harder life for the vehicle. However, it is important to consider the overall condition and maintenance history of the car rather than focusing solely on mileage.",
  ...
```

#### Example for Car Type Recommendation

For example, assume you are looking for a new SUV from Mercedes and use the following url from the API:
http://localhost:8080/api/v1/recommendation?type=SUV&make=Mercedes-Benz. Then, ChatGPT will recommend you:

```json
{
  "chatGptInfo": "SUVs, or Sports Utility Vehicles, are a type of vehicle that combines elements of both passenger cars and off-road vehicles. They typically feature a high ground clearance, a spacious interior, and the ability to handle various terrains. Here are some common features found in SUVs:\n\n1. Size and Space: SUVs offer ample seating capacity for passengers and usually have a larger cargo area compared to sedans or hatchbacks.\n\n2. All-Wheel Drive (AWD) or Four-Wheel Drive (4WD): Many SUVs come with AWD or 4WD systems, enhancing their off-road capabilities and providing better traction on slippery or uneven surfaces.\n\n3. Safety Features: SUVs often include advanced safety features like stability control, traction control, anti-lock braking systems, multiple airbags, blind-spot monitoring, lane-keeping assist, and collision avoidance systems.\n\n4. Technology and Connectivity: Modern SUVs come equipped with features like touchscreen infotainment systems, Bluetooth connectivity, smartphone integration, USB ports, advanced navigation systems, and premium audio systems.\n\n5. Comfort and Luxury: SUVs from luxury brands offer plush interiors, high-quality materials, luxurious seating options, climate control systems, panoramic sunroofs, and advanced driver-assist features.\n\n6. Towing Capacity: Many SUVs are designed with towing capabilities, allowing them to haul trailers or boats.\n\nNow, based on Car and Driver reviews, here are some recommended SUV models from Mercedes-Benz:\n\n1. Mercedes-Benz GLE: The GLE offers a comfortable and luxurious cabin, advanced safety features, a powerful engine lineup, and excellent off-road capabilities. It provides a smooth ride and comes with various tech features like a large infotainment display, smartphone integration, and a host of driver-assist systems.\n\n2. Mercedes-Benz GLC: The GLC combines style, comfort, and performance. It features a well-appointed interior, a user-friendly infotainment system, a smooth ride, and a range of engine options. Additionally, it offers good fuel economy and advanced safety features.\n\n3. Mercedes-Benz GLS: The GLS is a large luxury SUV that provides ample seating and cargo space. It offers a comfortable ride, powerful engine choices, advanced technology features, and a refined interior. The GLS also provides excellent towing capacity and a plethora of safety features.\n\nIn terms of reliability, Mercedes-Benz SUVs generally have a good reputation, but like any vehicle, they may encounter some common problems. These can include electrical issues, transmission problems, and occasional brake or suspension concerns. However, regular maintenance and servicing can help mitigate these problems.\n\nMaintenance and cost of operation for Mercedes-Benz SUVs can be higher compared to non-luxury brands due to the premium nature of the vehicles. Genuine parts and specialized servicing can contribute to higher maintenance costs.\n\nWhen it comes to pricing, Mercedes-Benz SUVs tend to be on the higher end of the spectrum due to their luxury status. Prices can vary depending on the specific model, trim level, optional features, and region.\n\nUser experience with Mercedes-Benz SUVs is generally positive, with drivers appreciating the comfort, performance, and advanced features. However, personal preferences and experiences can vary.\n\nUltimately, it is recommended to visit a Mercedes-Benz dealership, consult the official website, or refer to trusted automotive sources for the most accurate and up-to-date information on features, pricing, reliability, and common problems associated with specific Mercedes-Benz SUV models.",
  ...
```

### Used Car Listings

Used car listings are returned in a `databaseListings` JSON Object. You can find specific makes, models and car types.

#### Find a Specific Make

You can easily find used car listings for a specific make using the URL
extension: [api/v1/search_make?make=make_name](api/v1/search_make?make=make_name). You must provide the `String`
parameter `make`. You can also provide another optional `boolean` parameter called `low_price` to sort the listings by
the lowest price.

##### Example

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

#### Return a Specific Make and Model

You can easily find used car listings for a specific make and model using the URL
extension: [api/v1/search_model?make=make_name&model=model_name](api/v1/search_model?make=make_name&model=model_name).
You must provide the `String` parameters `make` and `model`. You can also provide another optional `boolean` parameter
called `low_price` to sort the listings by the lowest price.

##### Example

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

#### Recommend a Specific Type and Model

You can easily find used car listings and get a recommendation for a specific type of car using the URL
extension: [api/v1/recommendation?type=car_type](api/v1/recommendation?type=car_type). You must provide the`String`
parameter `type`. You can also find a recommendation for a specific type of car from a specific make as well via
the `String` parameter `make`.

##### Example

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

### Example of a Full API Call

The full API call includes both the `chatGptInfo` and `databaseListings` object.

## Features to Implement in the Future

There are lots of features I would like to implement once I get the time:

* Filtering entries by date. For example, the user may only like to see listings between a specific range, or sort by
  the newest listings first.
* Sorting by car trim. For example, the user may only want the Touring Version of the Honda Civic.
* Sorting by car specifications, including engine size, drivetrain, transmission, and fuel type. For example, a user may
  only want a car that is 4WD or AWD.
* Sorting by mileage. The user may want a low mileage car and prioritize those listings.
* Creating a website using this API by adding a front-end view.