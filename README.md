# jdbc-rest
jdbc wrapper on actual jdbc driver for executing parameterized sql statements and convert the results into json.
On this driver we have also developed a REST API server application [jdbc-restservlet](https://github.com/vnetcon/jdbc-restservlet)

## Building
mvn clean install  
mvn package

## Commercial use
If you want ot use this in closed code project or product you can buy a 99 USD license [here](https://vnetcon.com)  
If you think tha price is too low or high you can also change the price there :)


## Short introduction

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

## Quick start with DBeaver
During the development we have use DBeaver as our database tool and due to this our instructions are based on this.

Below is a screenshot where we have add the jdbc-rest driver into DBeaver and executing sql statements.

![jdbc-rest-dbeaver](http://vnetcon.s3-website-eu-west-1.amazonaws.com/img/jdbc-rest-dbeaver.png)


//TODO: Finalize this documentation, add the code and create wiki pages. In short we will update these pages soon

