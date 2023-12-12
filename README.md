# RidePal Web Application

RidePal, a web app, crafts personalized playlists based on users' music tastes and travel durations. It taps into external tools like Microsoft Bing Maps and Deezer to gauge travel times and fetch music data, building custom playlists within the platform.

![Home Page](https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/Home_Page.JPG)

## Table of Contents
- [Technologies and Principles](#technologies-and-principles)
- [Main Functionalities](#main-functionalities)
- [Public Part](#public-part)
- [Private Part](#private-part)
- [Administration Part](#administration-part)
- [REST API](#rest-api)
- [External Services](#external-services)
- [Database](#database)
- [Installation](#installation)
- [Developed By](#developed-by)

## Technologies and Principles
- Java, Spring Boot, Spring MVC, Thymeleaf, JUnit, Mockito, HTML, CSS, LESS, JavaScript, Hibernate, MariaDB
- DRY, KISS, YAGNI, SOLID, OOP, REST API design
- Service layer tests have over 80% code coverage
- Multilayered architecture

## Main Functionalities
### Playlist Generation
- Users can enter starting and destination addresses, select by car or on foot, select musical genres to create a playlist
- RidePal calculates travel duration and generates playlists with tracks from specified genres to match the travel time
- Playlists are saved under the user's profile for future listening

![Playlist Generation](https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/Playlist.JPG)

### Playlist Browsing
- Users can browse playlists created by other users, filter by total duration and genre tags
- Playlists are sorted by average rank in descending order by default

![All Playlists](https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/All_Playlists.JPG)

## Public Part
- Visible without authentication
- Users have access to the registration page, where they can create a new account
- On the login page, there is a link for first-time registration
- On the home page, there is a link to Login if the user is not logged in
- Playlists page is open for unauthenticated users, where playlists can be filtered by genre, minimum/maximum duration in minutes

![Register Page](https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/Register.JPG)

## Private Part
- Generating a new playlist
- Editing/Deleting owned playlists
- Editing of a playlist lets the user change its title and image
- Editing/Deleting user profile

![Generate Playlist](https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/Generate.JPG)

## Administration Part
### System administrators can administer all major information objects in the system. On top of the regular user capabilities, the administrators have the following capabilities:
- Admins can see all users
- Admins can update/Delete users profile pictures
- Admins can update/Delete playlists

![My Profile](https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/My_Profile.JPG)

## REST API
- http://localhost:8080 (access after you have successfully configured the project)

## External Services
### Deezer's API (https://developers.deezer.com/api)
- Used to fetch tracks, genres, albums, and artists

### Microsoft Bing Maps's Distance Matrix API (https://www.microsoft.com/en-us/maps/bing-maps/distance-matrix)
- Used to calculate distance and duration between two waypoints. Each waypoint is represented as a combination of longitude and latitude

## Database
MariaDB is used as a Database

## Installation
### Prerequisites
- Java Development Kit (JDK)
- MariaDB

1. Clone the project -> https://github.com/atanas-hristozov/RidePal/tree/main/RidePal
2. Configure the database connection in application.properties:
3. Execute the two SQL scripts located in the 'DB' folder in 'src'. First, create the database and then insert the data in it.
4. You do not need an API key to work with RidePal
5. Access the application at http://localhost:8080 in your web browser.

## Developed By
- Atanas Hristozov
- Rosen Yanev

![About](https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/About.JPG)
