# jdbc-rest
jdbc wrapper on actual jdbc driver for executing parameterized sql statements and convert the results into json.

Below is a simple query
```sql
select fname, lname from miki.mikitest --[json]
```
and below is the returned result as json
```json
{
  "fname": "Adam",
  "lname": "Smith"
}```

