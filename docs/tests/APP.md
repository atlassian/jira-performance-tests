# Evaluate your app's performance impact

[JPT](../../README.md) provides a framework for automating the way you test your apps.
It’s designed to compare how your app impacts the Jira user experience, comparing the response times of Jira with and without your app installed.
JPT can automate all aspects of the testing cycle, from provisioning instances, all the way to testing them and collecting performance data.

Atlassian uses JPT to benchmark each Jira release (posting the results on [Scaling Jira](https://confluence.atlassian.com/enterprise/scaling-jira-867028644.html)),
and also to test internally-developed apps like [Portfolio for Jira](https://www.atlassian.com/software/jira/portfolio).

## How it works

JPT starts by provisioning two Jira instances in Amazon Web Services (AWS):

* a _baseline_ instance, with no apps installed, and
* an _experiment_ instance, with your app installed

JPT will then benchmark both instances, running the most common user actions multiple times. When JPT finishes testing either instance, it reports:

1. What **user actions** it performed
2. **How many times** it performed each action
3. How many times each action resulted in an **error**
4. The **95th percentile** of all response times for each action

The _difference_ between both instance's response times will help show you the performance impact of your app.

## Requirements

You can install and run JPT from any system that has:

* [JDK](http://openjdk.java.net/) 8 - 11
* [Git](https://git-scm.com/)
* MacOS or Linux installed

### Amazon Web Services instances

JPT provisions Jira instances in AWS.
When you run JPT, you will be prompted for your AWS credentials.

Keep in mind that running JPT's tests will incur AWS usage costs.
Your own costs may vary depending on the size and resource usage of your app.
For comparison, running a two-node Jira Software Data Center instance in AWS typically costs around $6 per hour.

### Windows Compatibility

None of the technologies exclude Windows shell from being used, but there is no Windows compatibility CI employed to protect any compatibility promise. Use at your own risk.

## How to use JPT

JPT ships with a reference app, which you will replace with your own.
From there, JPT can handle the rest of the testing process.

### Preparing JPT for testing

1. Clone the JPT repository:

    ```
    git clone https://bitbucket.org/atlassian/jira-performance-tests.git
    ```

2. This repository ships with a Maven project. Open the following project in your IDE:


    ```
    examples/ref-apps/
    ```

3. This Maven project is a reference app. Replace it with your own app.


### Configuring virtual user actions


JPT uses _virtual users_ to perform common user actions.
You can configure those actions to create a more suitable test cases for your app.

To do this, you'll need to create your own test module.
Use the following modules as a reference:

- https://bitbucket.org/atlassian/jira-actions/src/master/
- https://bitbucket.org/atlassian/jira-software-actions/src/master/

### Setting your AWS credentials

JPT needs your AWS credentials to provision instances.
By default, JPT manages AWS credentials through the `com.amazonaws.auth.DefaultAWSCredentialsProviderChain` class.

You can provide your AWS credentials for this class through the following methods:

- Setting the environmental variables `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`, or
- Setting Java System Properties `aws.accessKeyId` and `aws.secretKey`

For more information about this class (including alternative methods for setting credentials), see [Working with AWS Credentials](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html).

JPT ships with a pre-configured policy for managing AWS access.
You can view and edit this policy through the [aws-policy.json file](../../aws-policy.json).
For more information, see [Policies and Permissions](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies.html).

### Running benchmark tests with JPT

Once you've applied your app and set your AWS credentials, run the following command from the repository's root:


```
cd jira-performance-tests/
mvn -f examples/ref-app/pom.xml install
```

Depending on your bandwidth, the entire test (from provisioning to data collection) could take around 45 minutes.
JPT will display test results for both baseline and experiment instances:

![Plain text report](plain-text-report.png)

JPT will also generate detailed test results and store them under `examples/ref-app/reference-jira-app-performance-tests/target/`:

- `detailed.log`: for detailed test logs.
- `/jpt-workspace/` subdirectory: contains raw test results for both baseline and experiment instances.
- `/surefire-reports/` subdirectory: contains detailed results in CSV and HTML formats.

After completing the test, JPT will terminate both instances it provisioned in AWS.
