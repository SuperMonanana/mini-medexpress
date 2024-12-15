# MengyanLi's HeliosX Technical Test
This application is a mini MedExpress, written in Java 21, it provides an endpoints to serve the consultation questions and received the answers then return a decision whether we are likely to make a prescription.

## Build
```
./mvnw clean install
``` 

## Run
Run MedExpressApplication main() method. 

Once the app is running, API doc is published [here](http://localhost:8080/swagger-ui/index.html#/Consultation) 

The application supports the following two endpoints, please see the API docs for more details
1. Serves to the frontend the questions that a customer is to be asked
```
GET /consultation/questions/{condition}
```
Example command:
```
curl -X 'GET' \
  'http://localhost:8080/consultation/questions/migraine' \
  -H 'accept: */*'
```
2. Receives the answers to the consultation questions. Upon receiving all the answers, returns a JSON document indicating whether
   we are likely able to prescribe the medication
```
POST /consultation/answers
```
Example command:
```
curl -X 'POST' \
  'http://localhost:8080/consultation/answers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "answers": [
    {
      "questionId": "q1",
      "answer": "Yes",
      "category": "PESONAL_INFO"
    }
  ]
}'
```

## Limitations and considerations
This application now only supports one condition - migraine. It saves some sample questions in memory, but it's easy to add other questions and conditions.

The eligibility check is also simple now - it will return false if the users answer "Yes" to any Health or Medication questions or didn't agree with the agreement. The logic is isolated in PrescriptionEligibilityChecker so it's easy to be expanded in the future.

"Code first" appoach is used for better flexibility and simplicity, in reality "API first" approach could be used to enable better collaboration 


## Lombok
[Project Lombok](https://projectlombok.org) is used to auto-generate constructor, builder, setter, getter, hashCode and equals, toString methods
