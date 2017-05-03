[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
# ticketing-service
## Description
This module demonstrates an integration of the Service Now REST APIs into Symphony.
## Documentation
## API overview
## Before you begin
Please create a file ticket-servicenow/config.properties with the following content:  
```servicenow.instance=YOURINSTANCENAME.service-now.com  
servicenow.username=YOURUSERNAME  
servicenow.password=YOURPASSWORD```  

## Building
To compile the code and then create a Docker image:  
  
```mvn compile -U clean  
mvn install```  


## Packaging
The code is packaged in a jar file inside a Docker container.  
## Deploying

## Contributing
## Community
