<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1>RidePal Web Application</h1>
    <p>RidePal, a web app, crafts personalized playlists based on users' music tastes and travel durations. It taps into external tools like Microsoft Bing Maps and Deezer to gauge travel times and fetch music data, building custom playlists within the platform.</p>
    <img src="https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/Home_Page.JPG" alt="">
    <br>
    <h2>Table of Contents</h2>
    <ul>
        <li><a href="#technologies">Technologies and Principles</a></li>
        <li><a href="#main-requirements">Main Functionalities</a></li>
        <li><a href="#public-part">Public Part</a></li>
        <li><a href="#private-part">Private Part</a></li>
        <li><a href="#administration-part">Administration Part</a></li>
        <li><a href="#rest-api">REST API</a></li>
        <li><a href="#external-services">External Services</a></li>
        <li><a href="#database">Database</a></li>
        <li><a href="#instructions">Installation</a></li>
        <li><a href="#info">Developed By</a></li>
    </ul>
    <br>

    <h2 id="#technologies">Technologies and Principles</h2>
    <ul>
      <li>Java, Spring Boot, Spring MVC, Thymeleaf, JUnit, Mockito, HTML, CSS, LESS, JavaScript,Hibernate, MariaDB</li>
      <li>DRY, KISS, YAGNI, SOLID, OOP, REST API design</li>
      <li>Service layer tests have over 80% code coverage</li>
      <li>Multilayered architecture</li>
    </ul>
    <br>
    <br>

    <h2 id="#main-requirements">Main Functionalities</h2>
    <h3>Playlist Generation</h3>
    <ul>
        <li>Users can enter starting and destination addresses, select with car or on foot, select musical genres to create a playlist</li>
        <li>RidePal calculates travel duration and generates playlists with tracks from specified genres to match the travel time</li>
        <li>Playlists are saved under the user's profile for future listening</li>
    </ul>
    <img src="https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/Playlist.JPG" alt="">

    <br>
    <h3>Playlist Browsing</h3>
    <ul>
      <li>Users can browse playlists created by other users, filter by total duration and genre tags</li>
      <li>Playlists are sorted by average rank in descending order by default</li>
    </ul>
     <img src="https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/All_Playlists.JPG" alt="">
    <br>
    <br>
    <h2 id="#public-part">Public Part</h2>
    <ul>
      <li>Visible without authentication</li>
      <li>Users have access to the registration page, where they can create a new account</li>
      <li>On the login page there is a link for first time for registration</li>
      <li>On the home page there is a link to Login if the user is not loggedin</li>
      <li>Lastly, the playlists page is also open for unauthenitcated users, where playlists can be filtered by genre, minimum/maximum duration in minutes</li>
    </ul>
    <img src="https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/Register.JPG" alt="">
    <br>
    <br>
    <h2 id="#private-part">Private Part</h2>
    <ul>
      <li>Generating a new playlist</li>
      <li>Editing/Deleting owned playlists</li>
      <li>Editing of a playlist lets user change its title and image</li>
      <li>Editing/Deleting user profile</li>
   </ul>
   <img src="https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/Generate.JPG" alt="">
    <br>
   <br>
   <h2 id="#administration-part">Administration Part</h2>
   <h3>System administrators can administer all major information objects in the system. On top of the regular user capabilities, the administrators have the following capabilities:</h3>
    <ul>
      <li>Admins can see all users</li>
      <li>Addmins can update/Delete users profile pictures</li>
      <li>Addmins can update/Delete playlists</li>
   </ul>
   <img src="https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/My_Profile.JPG" alt="">
   <br>
   <br>
   <h2 id="#rest-api">REST API</h2>
  <ul>
    <li>http://localhost:8080 (access after you have successfully configured the project)</li>
  </ul>
<br>
<br>
<h2 id="#external-services">External Services</h2>


   <h3>Deezer's API (https://developers.deezer.com/api)</h3>
    <ul>
      <li>Used to fetch tracks, genres, albums and artists</li>
    </ul>

<br>
<h3>Microsoft Bing Maps's Distance Matrix API (https://www.microsoft.com/en-us/maps/bing-maps/distance-matrix)</h3>
  <ul>
  <li>Used to calculate distance and duration between two waypoints. Each waypoint is represented as a combination of longitude and latitude</li>
  </ul>
  <h2 id="#database">Database</h2>
  <p>MariaDB is used as a Database</p>
  <br>

<br>
<br>

<h2 id="#instructions">Installation</h2>

<h3>Prerequisites</h3>

<ul>
  <li>Java Development Kit (JDK)</li>
  <li>MariaDB</li>
</ul>

<br>
<ol>
  <li>Clone the project -> https://github.com/atanas-hristozov/RidePal/tree/main/RidePal</li>
  <br>
  <li>Configure the database connection in application.properties:</li>
<br>
spring.datasource.url=jdbc:mariadb://localhost:3306/your_database<br>
spring.datasource.username=your_username<br>
spring.datasource.password=your_password
<br>
  
<br>
<li>As you have successfully established connection with the database, you must execute the two SQL scripts located in the 'DB' folder in 'src'. First create the database and then insert the data in it.</li>
<br>
 
  <li>You do not need an API key to work with RidePal</li>
<br>
<li>Access the application at http://localhost:8080 in your web browser.</li>
<br>
</ol>

<h2 id="#info">Developeders</h2>
<ul>
    <li>Atanas Hristozov</li>
    <li>Rosen Yanev</li>
  </ul>
<img src="https://github.com/atanas-hristozov/RidePal/blob/main/RidePal/src/main/resources/static/img_documentation/About.JPG" alt="">

</body>
</html>