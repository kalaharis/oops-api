# Outstanding Online Poll Service(OOPS) API
This is backend for online poll service. Its RESTful api created with [Spring Boot](https://projects.spring.io/spring-boot/). 

* [Basics](#basics)
* [List polls](#list-polls)
* [Create Poll](#create-poll)
* [Show Poll](#show-poll)
* [Voting](#voting)
* [Paging](#paging)
* [Sorting](#sorting)

## Basics
* All requests and responses are in JSON format.
* Dates using ISO 8601. Date format: "yyyy-mm-ddThh:mm:ssZ" 

Base url:
>http://host/api 
## List polls
`GET /polls`
#### Parameters
|Name|Type|Description|
|----|----|-----------|
|tags|string[]|List polls marked by certain tags|
|state|string|List only `closed` or `open` polls|
|start|string|List polls created after given date|
|end|string|List polls created before given date|

#### Response
~~~
Status: 200 OK
~~~
~~~
 {
 	"polls": poll[
 	{
 	#here will be poll when its final structure decided
 	}]
 }
~~~
## Create poll
`POST /polls`
#### Input
|Name|Type|Description|
|----|----|-----------|
|name|string|**Required**. Name of the poll|
|options|string[]|**Required. Should contain at least 2 options.** Poll's options|
  
#### Response
~~~
Status: 201 CREATED
Location: uri
~~~
~~~
 {
 	#here will be poll when its final structure decided
 }
~~~

## Show poll
`GET /polls/:id`
#### Response
~~~
Status: 200 OK
~~~
~~~
 {
 	"id": long,
 	"name": string,
 	"create_date": "yyyy-mm-ddThh:mm:ssZ",
 	"expire_date": "yyyy-mm-ddThh:mm:ssZ",
 	"tags": string[],
 	"total_votes": int,
 	"multioptions": boolean,
 	"state": string,
 	"options": [
 		{
 			"name": string,
 			"votes": long
 		}]
 }
 #structure may change
~~~

## Voting
`PUT /polls/{id}/vote`
#### Input
|Name|Type|Description|
|----|----|-----------|
|poll_id|long|**Required**. Id of poll|
|options|string[]|**Required. Should contain only one option if multioptions disabled for poll** Poll's options|

#### Response
~~~
Status: 200 OK
~~~
~~~
#here will be poll when its final structure decided
~~~
## Paging and Sorting
### Paging
To page content you should use parameters `page` and `size`.
##### Example
`GET /polls?page=1&size=5`
### Sorting
To sort content you should use parameter `sort` with field name and order `desc` or `asc`.
##### Example 
`GET /polls?sort=id,desc`
