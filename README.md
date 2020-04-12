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


<!--
## Quick Start (for windows 64bit) - updated 06/03/2020 (dd/mm/yyyy)
For setting up the development environment you don't need install anything. 
You just download the zip file and start the downloaded software with following steps:  

* create folder c:\vnetcon
* Download Development environment [here](http://vnetcon.s3-website-eu-west-1.amazonaws.com/dev-env.zip) to c:\vnetcon
* unzip the file. After this you should have c:\vnetcon\dev-env folder
* Create folder c:\etc\vnetcon and copy the database.properties file there
* Start apache drill by double clicking 1_StartDrill.bat
* Start database by double clicking 1_StartPostgreSQL.bat
* Start [DBeaver](https://dbeaver.io/) database tool by double clicking 2_StartDBeaver.bat
* Start Tomcat by double clicking 3_StartTomcat8.bat

After this you can point your browser to http://localhost:8080/jdbc-rest/rest/default/getUser/v1?userid=3 
to see the demo json.  
The DBeaver has ready configured setting for creating and executing sql statements against local postgresql database.
-->


## Building
1. Clone the repo and move to the folder where pom.xml exists
2. execute: mvn clean isntall
3. execute: mvn package  
4. Use the *-with-dependencies.jar" as your jdbc driver

## Suported databases
In theory all databases that have JDBC driver. Postgresql, Oracle, SQL Server etc.

## jdbc url
he key for understanding the jdbc url format is to keep in mind, that the /etc/vnetcon/database.properties file is the 
start point for creating connection. Example from following url
  
```
jdbc:vnetcon:rest://default
```

  
the "default" is the "prefix" in configuration parameters in database.properties files.
  
The jdbc-rest json does not have any parameters

## Commercial use
If you want to use this in closed code project or product you can buy a 99 USD license [here](https://vnetcon.com)  
If you think the price is too low or high you can also change the price there :)

## sql syntax and parameters
In short the idea is to contert normal sql to json by with --[json] comment. This comment will tell the driver 
to convert execute the statement as jdbc-rest statement. Below is a simple example

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

Below are some notes related to this. More detailed examples can be fuond from dev-nev.zip and DBeaver in there.
* --[json]: convert the result set to json and in insert/update/delete replace the rest-json parameters '{param_name}' with correct valus
* --[json=Person]: Give the name for root elemente in select statements
* r_ at the begining of parameter indicates that the actual value is retrived from htttp request (e.g. client send client id the parameter is sql should be '{r_clientid}'
* --[json:Person; r_clientid=default_value]: Set the default value for parameter 
* hidden_ indicates that the column should not be displayed in result json (e.g. select a as hidden_a from table)
* subquery_ indicates that the column is a select that should be executed (e.g. select 'select a, b form table' as subquery_colname). It is possible to have subqueries in subqueries.
* t_ indicates that the param value should be replaced in subquery with "parent sql column value" (e.g.  '{t_userid}' would be replaced with userid columnvalue from main query

## Data types
All data is treated as stings. If you need to insert/update data in different data type you need to put the parameter into database function that will do the conversion.  
insert into table a (a, b) values ('{r_a}', to_number('{r_b}') --[json]


## Screenshots
Below is a screenshot of DBeqver executing sql statements

![jdbc-json-dbeaver](http://vnetcon.s3-website-eu-west-1.amazonaws.com/img/jdbc-json-dbeaverx.png)



