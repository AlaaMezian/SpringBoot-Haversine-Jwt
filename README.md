# SpringBoot-Haversine-Jwt
this repository contains a demonstration on how to use haversine formula to get the nearest locations to a given location with in distance ,also the project is built around jwt authentication and role based access authorization using spring boot framework ,please consider rating and leaving a start if you find it useful

# What is Haversine Formula
this is a formula that is used to calculate the distance between 2 location and it goes as the following :
a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
c = 2 ⋅ atan2( √a, √(1−a) )
d = R ⋅ c
where	φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
note that angles need to be in radians to pass to trig functions!

# How Can We Make A good Use of it 
imagine you have a list of long and lat of locations in a database and a user provided you with his location using haver sine formula we can do magic haver sine going to calculate the distance between the user location and a given point and then we can classify if we want to display the other location or not if it match our specified distance 

other user we can query directly to the data base using haversine forumla which mean we can retrive entites based on there locations with in distance from neareast to farthest using this magical query 

:boom::boom:
```java 
static String HAVERSINE_PART = "(6371 * acos(cos(radians(:latitude)) * cos(radians(c.latitude)) * cos(radians(c.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(c.latitude))))";

	@Query("SELECT c FROM Client c WHERE " + HAVERSINE_PART + " < :distance ORDER BY " + HAVERSINE_PART + " DESC")
	public List<Client> findClientWithNearestLocation(@Param("latitude") double latitude,
			@Param("longitude") double longitude, @Param("distance") double distance);
  ````
  # Jwt authentication And Role Based Access 
  also in this repository you will find an insight on how to implement role based authorization and jwt athentication 
  
  # What is Jwt Authentication ?! 
  
its an authentication mechanism based aroud if user is authenticated he/she will obtain an encrypted token and that token will be used to authenticate the used ,the used will provide the token on each request and the server will decide if its a valid token or not and then grant access for the user on that resource.

we integrated role based access by putting the role of the user inside the token and then use spring security annotations such as @PreAuthorized("hasRole('ROLE-CUSTOMER')") to know whether the user is authorized to perform an action / get access to a specific end point 

please feel free to rate :star:  happy programming :smiley: :v: 

 [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet)


