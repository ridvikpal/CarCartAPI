# CarCostAPI

<!-- TOC -->
* [CarCostAPI](#carcostapi)
  * [Introduction](#introduction)
  * [Goals](#goals)
  * [Maven Dependencies](#maven-dependencies)
  * [Design](#design)
  * [Features](#features)
    * [ChatGPT Integration](#chatgpt-integration)
    * [Find a Specific Make](#find-a-specific-make)
    * [Return a Specific Make and Model](#return-a-specific-make-and-model)
    * [Recommend a Specific Type and Model](#recommend-a-specific-type-and-model)
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

## Design

## Features

### ChatGPT Integration

### Find a Specific Make

### Return a Specific Make and Model

### Recommend a Specific Type and Model