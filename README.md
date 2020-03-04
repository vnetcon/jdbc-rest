## Short overview with sql examples
Below are couple of sql statements to show how to convert sql statements to json with this jdbc driver.  
Below is a simple query
```sql
select fname, lname from miki.mikitest --[json]
```
and below is the returned result as json
```json
{
  "fname": "Adam",
  "lname": "Smith"
}
```

And then slightly more advanced example
```
select fname as "FirstName", lname as "LastName" 
from miki.mikitest 
where fname = '{r_fname}' --[json=Person; r_fname=Adam]
```
which poduce following json

```json
{
  "Person": {
    "FirstName": "Adam",
    "LastName": "Smith"
  }
}
```
In this example we used r_fname parameter. r_ stands for request parameter that are passed in jdbc-restservelt to driver and are replaced with the values the requester send to servlet. In here we add "r_fname=Adam" as a default request parameter if the requests parameters are not available in real (e.g. you are running the queries in DBeaver).


## Quick Start (for windows 64bit)
For setting up the development environment you don't need install anything. 
You just download the zip file and start the downloaded software with following steps:  

* create folder c:\vnetcon
* Download Development environment [here]() to c:\vnetcon
* unzip the file. After this you should have c:\vnetcon\dev-env folder
* Create folder c:\etc\vnetcon and copy the database.properties file there
* Start databaes by double clicking 1_StartPostgreSQL.bat
* Start [DBeaver](https://dbeaver.io/) database tool by double clicking 2_StartDBeaver.bat
* Start Tomcat by double clicking 3_StartTomcat8.bat

After this you can point your browser to http://localhost:8080/jdbc-rest/rest/default/getUser/v1?userid=3 
to see the demo json.  
The DBeaver has ready configured setting for creating and executing sql statements against local postgresql database.


## Building
1. Clone the repo and move to the folder where pom.xml exists
2. execute: mvn clean isntall
3. execute: mvn package  
4. Use the *-with-dependencies.jar" as your jdbc driver


## Commercial use
If you want to use this in closed code project or product you can buy a 99 USD license [here](https://vnetcon.com)  
If you think the price is too low or high you can also change the price there :)

Below is a screenshot of DBeqver executing sql statements

![jdbc-rest-dbeaver](http://vnetcon.s3-website-eu-west-1.amazonaws.com/img/jdbc-json-dbeaver.png)



