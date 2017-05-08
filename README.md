[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
# ticketing-service-paqx-parent-sample
## Description
This repository demonstrates integration of ServiceNow REST APIs into Project Symphony.
## Documentation
You can find additional documentation for Project Symphony at [dellemc-symphony.readthedocs.io](https://dellemc-symphony.readthedocs.io).
## Before you begin
Create a ticket-servicenow/config.properties file with the following content:  
```
servicenow.instance=YOURINSTANCENAME.service-now.com  
servicenow.username=YOURUSERNAME  
servicenow.password=YOURPASSWORD
```
## Building
Run the following commands to compile the code and create a Docker image:  
  
```
mvn compile -U clean  
mvn install
```  

The code is packaged in a JAR file inside a Docker container. 

## Contributing
Project Symphony is a collection of services and libraries housed at [github.com/dellemc-symphony](https://github.com/dellemc-symphony).

Contribute code and make submissions at the relevant GitHub repository level. See [our documentation](https://dellemc-symphony.readthedocs.io/en/latest/contributing.html) for details on how to contribute.
## Community
Reach out to us on the Slack #symphony channel. Request an invite at [community.codedellemc.com](http://community.codedellemc.com).

You can also join [Google Groups](https://groups.google.com/d/forum/dellemc-symphony) and start a discussion.
