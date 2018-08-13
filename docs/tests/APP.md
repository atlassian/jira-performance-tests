# App Developer tests

This section describes, how to integrate [JPT](../../README.md) into your app tests.

You can start with a reference app test(**incomplete** link). The test runs two separate Jira instances, 
one with the reference app installed and another one without the app. It runs default Jira Software scenario.

## Requirements

 - JDK 8 - 11
 - AWS account (**incomplete** what kind of access/permissions do we need)

## How to start

1. Clone the repository
2. Open the maven project located in `examples/ref-app` directory in an IDE of your choice
3. Run `mvn verify` 
4. The test will ask for AWS credentials (**incomplete** You can implement AWS auth)
5. It should take around 25 minutes (**incomplete** check the time). 
In the end, you should see a simple report with results

```
TODO example console report
```

The test will also generate detailed a detailed report (**incomplete** example) and a chart (**incomplete** example)
 
6. You can modify (**incomplete** link) test to check if it works with your app.
    - you can point to your app by (**incomplete**)
      - GAV
      - File (??)
      - Marketplace link (??)

(**incomplete** it should auto clean AWS instances) 
(**incomplete** how to clean AWS instances) 

## AWS Account

JPT uses AWS to provision Jira instances. See the Architecture section for more details.
We estimate it should cost around 5$ (**incomplete** real data) to run a test.

## Architecture

(**incomplete** text)
(**incomplete** diagram)