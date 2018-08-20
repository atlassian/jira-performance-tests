# App Developer tests

This section describes, how to integrate [JPT](../../README.md) into your app tests.

You can start with a reference app test(**incomplete** link). The test runs two separate Jira instances, 
one with the reference app installed and another one without the app. It runs default Jira Software scenario.

## Requirements

 - [JDK](http://openjdk.java.net/) 8 - 11
 - [AWS](https://aws.amazon.com/) account (**incomplete** what kind of access/permissions do we need)
 - [Git](https://git-scm.com/)

## How to start

1. Clone the repository

    ```
    git clone git@bitbucket.org:atlassian/jira-performance-tests.git
    ```

2. Open the maven project located in `examples/ref-app` directory in an IDE of your choice
3. Run `mvn -f examples/ref-app/pom.xml install` in the main JPT directory
4. The test will ask for AWS credentials (**incomplete** You can implement AWS auth)
5. It takes 30 - 40 minutes to complete
 
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