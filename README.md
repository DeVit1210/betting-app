# betting-app

This project is a backend part of the betting company's website, written in <b>Java</b> using <b>Spring framework</b>. Most of the methods are covered by <b>jUnit</b> tests. The purpose of creating this application is to understand the basics of web development with Spring Boot and improve software testing skills.

It is possible to add events of all sports in which there is a team versus team or player versus player competition, as well as adding the main stake types on these events. For instance:
- football: result, double outcome handicap, goals total, score, yellow card quantity and fouls quantity, penalty or red card presence, etc. All of these stake types implemented for the whole match or for the certain period and apply to all game sports
- boxing: the way of fight ending, is the fight went full distance, knockdowns quantity, double fight outcome, etc. 

Event coefficients and their results are not generated randomly, but are added by the administrator

<br>

# features

The project is still under development. Here is the list of features that has been implemented yet.

## player

- user registration with email confirmation
- authentication using JWT
- password restoring via email link or phone code

## admin

- managing sport events
- adding event results
- adding odds to the newly added events
- automatic stake types addition to the newly added event
- automated stake processing based on event results
<br>

# Technologies

- Spring Boot 3
- Spring Security 6
- Spring Data JPA (Hibernate)
- PostgreSQL
- Lombok
- Java Mail Sender
- jUnit
- Mockito

<br>

# To be implemented

There is still a lot to bring to life in order to make this project full-fledged and complete. The most important thing is to add an opportunity for a player to place bets. Also the player's personal account must be implemented and have the following functionality: viewing the betting history, enabling two-factor authentication, account repleniishment and withdrawal of funds. 

There are also plans to implement a frontend part for the developed API, document its endpoints using Swagger and configure Redis for caching enabling and performance improvement
