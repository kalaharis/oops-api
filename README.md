# Outstanding Online Poll Service(OOPS) API
This is backend RESTful api created with [Spring Boot](https://projects.spring.io/spring-boot/) 
for [online poll service](http://176.100.17.42/oops). 

* [Basics](#basics)
* [List polls](#list-polls)
* [Create Poll](#create-poll)
* [Show Poll](#show-poll)
* [Voting](#voting)
* [Paging](#paging)
* [Sorting](#sorting)
* [Errors](#errors)

## Basics
#### Request and response body
All requests and responses using JSON format.
#### Date format
Dates using [UTC](https://en.wikipedia.org/wiki/Coordinated_Universal_Time) 
and [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) standards.  
Example: "2017-07-31T13:01:20Z" 

#### Base url:
Hosted at my home server
>176.100.17.42:8080/api/

## List polls
List all public pools.

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
     "content": [
         {
             "name": "Favorite game genre",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": "2017-07-25T21:50:05Z",
             "tags": [
                 "playstation",
                 "PC",
                 "games",
                 "XBOX"
             ],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 499,
             "state": "CLOSED",
             "options": [
                 {
                     "name": "RPG",
                     "votesCount": 203
                 },
                 {
                     "name": "Sports",
                     "votesCount": 85
                 },
                 {
                     "name": "RTS",
                     "votesCount": 64
                 },
                 {
                     "name": "Platformer",
                     "votesCount": 56
                 },
                 {
                     "name": "ARPG",
                     "votesCount": 34
                 },
                 {
                     "name": "FPS",
                     "votesCount": 29
                 },
                 {
                     "name": "MMO",
                     "votesCount": 28
                 }
             ],
             "id": "diLLi"
         },
         {
             "name": "Do you like secrets?",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": null,
             "tags": [
                 "secret"
             ],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 288,
             "state": "OPEN",
             "options": [
                 {
                     "name": "Yes",
                     "votesCount": 203
                 },
                 {
                     "name": "No",
                     "votesCount": 85
                 }
             ],
             "id": "4T9kT"
         },
         {
             "name": "Transport of choice?",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": "2069-09-22T21:00:00Z",
             "tags": [
                 "social",
                 "funny",
                 "multiple choices"
             ],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 598,
             "state": "OPEN",
             "options": [
                 {
                     "name": "car",
                     "votesCount": 206
                 },
                 {
                     "name": "train",
                     "votesCount": 89
                 },
                 {
                     "name": "bike",
                     "votesCount": 65
                 },
                 {
                     "name": "motobike",
                     "votesCount": 60
                 },
                 {
                     "name": "plain",
                     "votesCount": 35
                 },
                 {
                     "name": "boat",
                     "votesCount": 29
                 },
                 {
                     "name": "space rocket",
                     "votesCount": 112
                 }
             ],
             "id": "jcqXc"
         },
         {
             "name": "Best ongoing tv series",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": null,
             "tags": [],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 356,
             "state": "OPEN",
             "options": [
                 {
                     "name": "Game of thrones",
                     "votesCount": 204
                 },
                 {
                     "name": "Lucifer",
                     "votesCount": 86
                 },
                 {
                     "name": "Preacher",
                     "votesCount": 66
                 }
             ],
             "id": "niAdi"
         },
         {
             "name": "What programming languages do you use?",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": null,
             "tags": [
                 "PC",
                 "programming",
                 "multiple choices"
             ],
             "voted": false,
             "multiOptions": true,
             "totalVotes": 505,
             "state": "OPEN",
             "options": [
                 {
                     "name": "Java",
                     "votesCount": 205
                 },
                 {
                     "name": "C#",
                     "votesCount": 86
                 },
                 {
                     "name": "Javascript",
                     "votesCount": 68
                 },
                 {
                     "name": "C/C++",
                     "votesCount": 56
                 },
                 {
                     "name": "Swift",
                     "votesCount": 34
                 },
                 {
                     "name": "Ruby",
                     "votesCount": 29
                 },
                 {
                     "name": "Objective-C",
                     "votesCount": 28
                 }
             ],
             "id": "7ToET"
         },
         {
             "name": "Favourite colour?",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": null,
             "tags": [],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 471,
             "state": "OPEN",
             "options": [
                 {
                     "name": "Black",
                     "votesCount": 203
                 },
                 {
                     "name": "White",
                     "votesCount": 85
                 },
                 {
                     "name": "Green",
                     "votesCount": 64
                 },
                 {
                     "name": "Red",
                     "votesCount": 56
                 },
                 {
                     "name": "Yellow",
                     "votesCount": 34
                 },
                 {
                     "name": "Purple",
                     "votesCount": 29
                 }
             ],
             "id": "dcLyc"
         },
         {
             "name": "Console of choice",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": "2017-07-25T21:50:06Z",
             "tags": [
                 "playstation",
                 "PC",
                 "Wii",
                 "games",
                 "XBOX"
             ],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 408,
             "state": "CLOSED",
             "options": [
                 {
                     "name": "Screw console, i choose PC",
                     "votesCount": 203
                 },
                 {
                     "name": "Playstation",
                     "votesCount": 85
                 },
                 {
                     "name": "XBOX",
                     "votesCount": 64
                 },
                 {
                     "name": "Wii",
                     "votesCount": 56
                 }
             ],
             "id": "ji6qi"
         },
         {
             "name": "Favourite hot beverage?",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": "2017-07-25T21:50:06Z",
             "tags": [
                 "social"
             ],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 408,
             "state": "CLOSED",
             "options": [
                 {
                     "name": "tea",
                     "votesCount": 203
                 },
                 {
                     "name": "coffee",
                     "votesCount": 85
                 },
                 {
                     "name": "Hot chocolate",
                     "votesCount": 64
                 },
                 {
                     "name": "boiled water",
                     "votesCount": 56
                 }
             ],
             "id": "gTXoT"
         },
         {
             "name": "Is this website cool",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": null,
             "tags": [],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 290,
             "state": "OPEN",
             "options": [
                 {
                     "name": "Yes",
                     "votesCount": 204
                 },
                 {
                     "name": "Sure",
                     "votesCount": 86
                 }
             ],
             "id": "jcqnc"
         },
         {
             "name": "Turn down 4 watt",
             "createDate": "2017-07-25T21:50:05Z",
             "expireDate": null,
             "tags": [],
             "voted": true,
             "multiOptions": false,
             "totalVotes": 292,
             "state": "OPEN",
             "options": [
                 {
                     "name": "-__-",
                     "votesCount": 204
                 },
                 {
                     "name": "(:",
                     "votesCount": 85
                 }
             ],
             "id": "AijKi"
         },
         {
             "name": "dank",
             "createDate": "2017-07-26T18:53:24Z",
             "expireDate": null,
             "tags": [
                 "cool"
             ],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 1,
             "state": "OPEN",
             "options": [
                 {
                     "name": "me",
                     "votesCount": 0
                 },
                 {
                     "name": "k",
                     "votesCount": 1
                 }
             ],
             "id": "4Tk7T"
         },
         {
             "name": "adfasdfadf?",
             "createDate": "2017-07-28T17:17:53Z",
             "expireDate": null,
             "tags": [],
             "voted": false,
             "multiOptions": false,
             "totalVotes": 0,
             "state": "OPEN",
             "options": [
                 {
                     "name": "1",
                     "votesCount": 0
                 },
                 {
                     "name": "2",
                     "votesCount": 0
                 },
                 {
                     "name": "3",
                     "votesCount": 0
                 }
             ],
             "id": "Gcd4c"
         }
     ],
     "totalPages": 1,
     "totalElements": 12,
     "last": true,
     "size": 20,
     "number": 0,
     "first": true,
     "numberOfElements": 12,
     "sort": null
 }
~~~
## Create poll
`POST /polls`
#### Input
|Name|Type|Description|
|----|----|-----------|
|name|string|**Required**. Name of the poll|
|options|string[]|**Required at least 2.** Poll's options|
|tags|string[]|Poll's tags
|expireDate|string|When poll will be closed.
|hidden|boolean|Will poll be public or private
|multiOptions|boolean| Will poll allow multiple choices
|multipleVotesIp|boolean| Allow multiple votes from one ip
#### Response
~~~
Status: 201 CREATED
~~~
~~~
 {
     "name": "Do you like cats?",
     "createDate": "2017-07-31T14:08:07Z",
     "expireDate": "2020-07-20T13:01:20Z",
     "tags": [
         "cats",
         "internet"
     ],
     "multiOptions": true,
     "totalVotes": 0,
     "state": "OPEN",
     "options": [
         {
             "name": "Yep",
             "votesCount": 0
         },
         {
             "name": "Nope",
             "votesCount": 0
         },
         {
             "name": "I am cat",
             "votesCount": 0
         }
     ],
     "id": "cKEei"
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
~~~

## Voting
`PUT /polls/{id}`
#### Parameters
|Name|Type|Description|
|----|----|-----------|
|vote|number[]|**Required.** Selected option(s)|

#### Response
~~~
Status: 200 OK
~~~
~~~
{
     "name": "Do you like cats?",
     "createDate": "2017-07-31T14:08:07Z",
     "expireDate": "2020-07-20T13:01:20Z",
     "tags": [
         "cats",
         "internet"
     ],
     "multiOptions": true,
     "totalVotes": 1,
     "state": "OPEN",
     "options": [
         {
             "name": "Yep",
             "votesCount": 0
         },
         {
             "name": "Nope",
             "votesCount": 0
         },
         {
             "name": "I am cat",
             "votesCount": 1
         }
     ],
     "id": "cKEei"
 }
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

## Errors
All common request fails will also return error object in body that better explains what is wrong: 
~~~
{
    "httpStatusCode": 400,
    "errorMsg": "Cannot vote: poll doesn't allow multiple votes per ip"
}
~~~
#### General
|Status Code|Name|Meaning|
|----|----|-----------|
|400|Bad Request|Something wrong with parameters or body of request|
|404|Not Found|Request poll was not found|
|500|Internal Server Error|Something unpredictable happened :(|

