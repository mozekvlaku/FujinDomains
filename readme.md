<img src="https://cdn.vespotok.net/img/fujindomains.svg" alt="Fujin Domains" width="200"/>

Fujin Domains is a LDAP-like directory service and Credential Provider for Fujin web-os

## Getting it up
Fujin Domains is available to be run via Docker.


``
docker network create dbnetwork``

``
docker-compose up
``

# Warning

If you want to compile the project yourself and run server in docker, you'll need to edit the .jar file's META-INF > persistence.xml.

``
<property name="javax.persistence.jdbc.url"
value="jdbc:mysql://localhost:3306/fjdcs_server?serverTimezone=America/New_York&amp;useEncoding=true&amp;characterEncoding=UTF-8" />
``

Change to

``
<property name="javax.persistence.jdbc.url"
value="jdbc:mysql://host.docker.internal:3306/fjdcs_server?serverTimezone=America/New_York&amp;useEncoding=true&amp;characterEncoding=UTF-8" />
``

# First run

At first, Fujin Domains will create it's first, built-in domain. You'll need to press Enter on prompt an restart the server service.
Then, just navigate to localhost:8082 and login with username **Administrator@builtin.local**, or using NT4-style **BUILTIN\Administrator** and password **admin1**.
