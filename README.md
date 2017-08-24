# Courses4Free - Java EE Backend
RESTful API in JAVA EE Environment                                                                                
----------------------------------------------------------
An API exposes Services for accessing Web Courses, designed and developed in a RESTful way, with JAX-RS Spec and Jersey Implementation. Project include modules like authentication, authorization, data validation, file uploading and in general CRUD operations on a MySQL database.

The entities of the API are Students and Teachers. Students can register and join courses. Teachers can register, create and teach courses. 

- Endpoints for accessing (locally working) resources are (some of the uris mentioned there):
 CourseResource - localhost:8080/services/courses,
 StudentResource - localhost:8080/services/students & localhost:8080/services/students/{studentId}/courses,
 TeacherResource - localhost:8080/services/teachers & localhost:8080/services/teachers/{teacherId}/courses
 & localhost:8080/services/teachers/{teacherId}/students

- Some of the presented specifications are:
JAX-RS (Jersey implementation), JPA (EclipseLink implementation), EJB, CDI, JWT (Auth2), Bean Validation 

- Media Types that is fully supported by this RESTful API are:
JSON, XML and MULTIPART FORM DATA (for the uploaded files)

- Maven Build Tool is used for the building operation (for dependencies-libraries) of the whole project

- JUnit is used for testing purposes

- Eclipse IDE is used for development

- Payara Application Server is used for deployment
