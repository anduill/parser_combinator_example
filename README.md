# Parser Combinator Example and API
This simple project demonstrates how to make a simple grammar for parsing various non-standard
scientific units and translating them standard units (i.e. si units).  For example:

```
degree/minute
```
would be translated to:
```
rad/s (i.e. radians/seconds)
```
However, we need to know how many 'rad/s' are in a 'degree/min'.  In other words, we need a "multiplication factor".

In this example, the factor is:
```
((π/180) rad) / (60 s) = 0.00029088820866572 (rad/s)
```

In this project, arbitrary combinations (only combining units with multiplication and division)
are parsed and processed.  The parsing is available as a web endpoint:

```
http://127.0.0.1:8080/units/si?units=(degree/(min*min))
```
will return:
```json
{
    "unit_name": "(rad/(s*s))",
    "multiplication_factor": 0.00000484813681
}
```
## Running Web Service
This is a jvm service and is hardcoded to host "0.0.0.0" and port "8080".  The service can be run
in a variety of ways. (e.g. with sbt, natively, or as docker container)

### Build
```bash
sbt clean test assembly
```
### SBT Run
```bash
sbt run
```
### Docker Build
```bash
docker build -t combinator:v1 .
```
### Docker Run
```bash
docker run -p 8080:8080 combinator:v1
```
### API Usage
Simply hit the following endpoint (as in this example):
```bash
http://127.0.0.1:8080/units/si?units=(degree/(min*min))
```
If a improperly formatted string is passed, the service will return a BAD_REQUEST code with
the following error message:
```json
{
    "message": "Parsing failure for unit string <BAD STRING>",
    "exception": "No result when parsing failed"
}
```