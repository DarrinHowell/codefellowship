# Code Fellowship

## Author 
- Darrin Howell

## Overview
Programming bootcamps require persistence, focus, and dedication. Participants of these programs
also need one another. This application exists to connect up and coming devs at Code Fellows
to connect with one another and post comments on a group blog page. 

At this point in the project, only logged in users can access the user's pages. Only logged
in users can post to their own page. Logged in users can see blog posts from every user
on their respective pages, but logged in users can only post to their own pages.

After the most recent update to our controllers and templates, we are now checking 
for harmful SQL and HTML injections by users into our form objects. We have also enabled
users to follow one another. The number of people whom they are following is rendered
on their profile page. Users can also navigate to the "feed" page after logging in to
see posts from the users that they are following. 

## Running program
There are two options for running this application: <br/> 
1. Open the project in IntelliJ and run the CodeFellowshipApplication class.
2. Type this command into the terminal: ./gradlew bootRun 

This application requires a local PostGreSQL database. In order to connect your local
psql database on a mac, please provide information with your unique passwords and database 
names: <br/>

spring.datasource.platform=postgres <br/>
spring.datasource.url=jdbc:postgresql://localhost:5432/database_name <br/>
spring.datasource.username=your_psql_userName_here <br/>
spring.datasource.password=your_psql_password_here <br/>
spring.jpa.hibernate.ddl-auto=create <br/>
