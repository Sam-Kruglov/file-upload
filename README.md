# File Upload

This is a sample project containing logic that supports uploading and downloading of multipart files.

There is a **server** module with a stub Spring MVC controller that works with multipart files.

There is a **client** module that contains two DAO entities: Netflix Feign client and Spring Rest Template. While Feign only supports this kind of logic only using in-memory byte arrays, Rest Template is able to achieve the same goal using Input/Output Streams which is a better approach for large files.

There is also a set of Spring Boot integration tests that use both modules
