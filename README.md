# NEWS-Service-API

News Service API

This API allows you to controle a news service. 

Solution 1: 

The news service design as single api from both user and news management. 

Demo Video:



https://user-images.githubusercontent.com/58274552/146839893-c2a8f9bf-f258-4f91-bfa8-ef1e98cfa827.mp4



Solution 3: Its wrote in IntelliJ ultimate edition ( 1 month free ). The work is not finised yet due to software licence.

How solution 3 works


![News Service_solution 3](https://user-images.githubusercontent.com/58274552/147097764-fd94d89c-7bf4-458f-8289-e63cba826586.PNG)

update till December 23, 2021: Solution 3 have few errors that I can not remove due to sodtware licence.
error : dependency error in news-service micro service and apigateway 

after run the clean mvn and install checking the error and trying to solve it

few errors I am listing down : 

1. plugin'org.springframework.bootspring-boot-maven-plugin' not found > after resolving this problem, it came again and again !!!!!!
2. 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: org.springframework.boot:spring-boot-starter-web:jar -> duplicate declaration of version
3. Cannot resolve symbol 'springframework'
4. java: Internal error in the mapping processor: java.lang.NullPointerException
5. path.directoy configuration property error
