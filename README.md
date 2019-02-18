# SearchEstimator
Search popularity estimator for amazon

### Idea
Check popularity of the exact word in Amazon search

### How does it work?

1) A keyword is sent to the REST API of the estimator, 
then it is sent to Amazon autocomplete API. 
2) The API returns the list of suggestions based on the keyword.
The maximum number of suggestions is 10
3) The number of occurrences of the keyword in returned suggestions list
is calculated. 
4) The number of occurrences for keyword is saved     
5) Steps 3 and 4 are repeated for two more cases:  
     - First letter of the keyword
     - Half of the keyword. If half of the keyword ends with space the first letter of the next word is added       
6) After all three calls are made and occurrences are calculated the score is estimated.

### Assumptions
1. Why three calls? 
    - The results of search by full keyword,first letter and part of keyword are enough
    If the keyword popular enough the search even by the first letter will return occurrences. Half of the keyword returns good enough results for estimation.
    - Performance reasons. The required SLA for the microservice is 10 seconds. If more calls to the API were required, for the very long keyword the SLA would have been exceeded
2. The order of suggestions returned by the API is insignificant. The algorithm requires the number of occurrences. 
3. Occurrence is counted in the following way:
      - The keyword can consist of a single word or of multiple words split by spaces. Suggestion 
      must contain the keyword itself or the keyword with spaces before it,after it or with spaces on both sides
      For example:
      ```yaml
      Keyword is "test". Suggestions are: "test", "test1","test 2","big test","big test 1".
      ```
      The number of occurrences is 4 as we don't count result test1 because it is a separate word   
     
      ```yaml
        Keyword is "big test". Suggestions are: "big test1", "bigtest","big test","more big test" 
      ```
      The number of occurrences is 2 as "big test1" has 1 at the end, "bigtest" doesn't contain space.   
4. Score estimation 
      - The maximum number of suggestions returned by each call to the autocomplete API is 10. It means that if 3 calls are made the maximum number of occurrences for all calls is 30.
   The score is a result of division of the sum of all occurrences for the keyword by maximum number of occurrences  
      

### Build and run

Running the jar
```yaml
  mvn clean install
  java -jar SearchEstimator-1.0.jar
```

Running with Spring Boot plugin
```yaml
mvn clean install spring-boot:run
```

REST call   
```yaml
http://localhost:8080/estimator?keyword=some+keyword
```   
REST service description in WADL
```yaml
http://localhost:8080/?_wadl
```
