# Enrollment System RESTFul API Implementation and continuous deployment
This is a java program that provides the REST apis and backend support for a school enrollment system.

The users of the system will consist of both school administrators and students. School administrators will create student identities, and students will be able to enroll themselves in classes before each semester starts.

## Tech stack
- **Backend**
    - Spring Boot, Spring Web, Spring Cache, Spring Data
    - MongoDB
    - Maven
    - Libraries (Lombok, Map struct, Mockito, Junit 5 and Swagger Open Api)
- **DevOps**
    - Docker, AWS (CodePipeLine, ECR and ECS/EC2)
    
## Pre-requisites
 
 - Java 8+ ([https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html))
 - Maven  ([https://maven.apache.org/install.html](https://maven.apache.org/install.html))
 - Docker ([https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/))

## How to build

```shell
$ mvn clean package
```

## How to test

```shell
$ mvn clean test
```

## How to package application as docker image

```shell
$ docker build -t enrollment-system:1.0 .
```

## How to run application

```shell
$ docker-compose up
```

You can then access to the crud endpoints at http://localhost:8080.
You can import enrollment system api.postman_collection into your postman app to consume the endpoints.

API Documentation can be accessed at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
Also see API Use Cases Section below for examples.

## How to stop application

- Mac: `ctrl + c`
- Windows: `ctrl + c`
- or from another terminal tab:

```shell
$ docker-compose down 
```

## How to view application logs
```shell
$ docker logs enrollment-system
```

## How to connect to MongoDB Container
```shell
$ docker exec -it mongodb bash
```

## Design/Implementation notes:
1. ### The following are utilized in an effort to improve performance and scalability:
- Add pagination support for GET /fetchStudents which could return huge Json response.
- MongoDb is used so that DB can be easily scaled horizontally to support huge dataset. Document-based DB has the advantage of improved query performance too, because the related document (Enrollments) has already been stored with the main document (Student), therefore reducing overhead of table joins.
- Use of MongoTemplate to implement fetchStudents and fetchClasses search functionality based on the different params the client passes in. This greatly improves data fetching performance over using java code that bring all data then filter.
- Spring cache is used to cache class and semester data.
- Concurrent Hashset implemented with ConcurrentHashMap are used for collections of classes for an enrollment and collections of enrollments for a student. AtomicInteger is used for total credits for a student. All of these concurrent data structure are used to support multi-threading and support any future concurrent enhancement needs.

2. I created interfaces for all service methods, so that different implementations (e.g. getting data from kafka stream, or using 3rd party services, etc.) can be easily configured to provide the needed functionality.

3. Hibernate implementation of javax validations are utilized to validate the json input data.

4. ControllerAdvice is utilized to handle error responses to the clients.

5. Dto objects are utilized to provide a clean separation of entity DB objects in favor of clean abstraction of persistence layer. Mapstruct library is utilized to provide data transformation of dto and entity objects.

6. Lombok library is utilized for complie-time code auto generation (getters/setters/contrctors, etc...).

7. Comprehensive test cases are developed to provide unit testing and integration testing to ensure code quality and facilitate future enhancement/maintenance.

8. Spring properties/configuration/injection are utilized to ease future enhancement/maintenance efforts.

9. Open API (Swagger) is utilized to generate API documentation accessible through browser.

## API Use Cases

### Start a new semester
	Method: Post
	Url: http://localhost:8080/semesters
	Request Body: 
		{
              "name": "2022fall",
              "startDate": "2022-08-27",
              "endDate": "2022-08-27"
		}

### Add a new class
	Method: Post
	Url: http://localhost:8080/classes
	Request Body: 
		{
		  "name": "3A",
		  "credit": 3
		}

### Add a new student
    Method: Post
    Url: http://localhost:8080/students
    Request Body: 
      {
        "firstName": "Michael",
        "lastName": "Wong",
        "nationality": "Singapore",
      }
### Enroll a student
    Method: Post
    Url: http://localhost:8080/enrollments
    Request Body:
      {
        "semester": "2022fall",
        "id": 1,
        "class": "3A"
      }
		
### Get a student by id
    Method: Get
    Url: http://localhost:8080/students/1
	
### Modify a student
    Method: PUT
    Url: http://localhost:8080/students
    Request Body: 
      {
       "id": 1,
        "firstName": "Michael",
        "lastName": "Wong",
        "nationality": "US",
      }

### Get the list of classes for a particular student for a semester or the full history of classes enrolled.
    Method: GET
    Urls:
        http://localhost:8080/fetchClasses?semester=2022fall&id=1
        http://localhost:8080/fetchClasses?id=1

### Get the list of students enrolled in a class for a particular semester (pagination enabled)
    Method: GET
    Urls:
        http://localhost:8080/fetchStudents
        
        http://localhost:8080/fetchClasses?id=1
        
        http://localhost:8080/fetchClasses?semester=2022fall
        
        http://localhost:8080/fetchClasses?pageNo=1&pageSize=100

#### Drop a student from a class.
    Method: Delete
    Url: http://localhost:8080/enrollments?id=1&class=3A

## CI/CD Using AWS CodePipeline

If any code pushes to GitHub repository, the Git webhook initiates the AWS CodePipeline automatically, as defined in: 

***buildSpec.yml*** 

- Execute the maven test and build
- Create a docker image and push it to ECR
- Create an imagedefinitions.json file for the deployment

The code pipeline for this application consists of three stages:
- Code Source - Pull sourcecode from Github
- Code Build - Maven build and create a docker Image
- Code Deploy - Deploy into ECS/EC2 instance

## Requirements Description:
### Restraints
- School administrators can create and modify student records but never delete them.
- Each class a fixed credit/unit. Some harder classes might be 4 credits while easier
  ones could be 2 or 3 credits.
- Each student is only allowed to be enrolled in a maximum of 20 credits for each
  semester. There is a minimum of 10 credits to be considered full time.

### Required APIs:
- API to add new students or modify them
- API to create a new semester
- API to enroll a student into a class for a particular semester
- API to get the list of classes for a particular student for a semester, or the fully history
  of classes enrolled.
- API to get the list of students enrolled in a class for a particular semester.
- API to drop a student from a class.

### Non Functional Considerations
Performance and scalability aspects of your code will also be evaluated. Make sure the data
structures that you use are chosen for scale and efficiency. For example, think about which
APIs might be called more often than others with what parameters, and make sure those
can handle the load efficiently.

See Test_Backend_Role.pdf for more details. 
