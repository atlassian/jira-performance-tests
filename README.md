![picture](docs/JPT-banner@1x.png)

Jira Performance Tests (JPT) is a fully-automated performance benchmarking tool designed for Jira Server and Jira Data Center.
It can manage all aspects of the testing process -- provisioning a new Jira instance, running performance tests, and generating
a report.

JPT focuses on _user experience_, allowing you to test both front-end and back-end performance simultaneously. Rather than
using simple HTTP traffic, JPT uses browsers to test end-to-end interactions.

Atlassian maintains and uses JPT to benchmark Jira's performance each release (see [Scaling Jira](https://confluence.atlassian.com/enterprise/scaling-jira-867028644.html) for details).
If you're an Atlassian developer, you can access their [internal JPT tests](https://stash.atlassian.com/projects/JIRASERVER/repos/jira-performance-tests/browse/README.md)
using your employee credentials.

## Features

  - Runs performance tests
  - Automatically provisions a Jira instance in AWS infrastructure
  - Generates a report and chart
  - Uses configuration as a code
  - Uses a large Jira dataset
  - Uses data-agnostic scenarios
  - Starts with reasonable defaults
  - Gathers system and GC metrics
  - Supports Jira 7.2 and up
  - Compares Jira with and without plugin installed
  - _(planned)_ Supports Jira 8+

## Basic use cases

- As a customer, you can **[test the performance of an existing Jira instance](docs/tests/ON_PREMISE.md)**.
- As a Jira app developer, you can **[test the performance impact of your app](docs/tests/APP.md)**.

## Feedback

We welcome all feedback. You can send it our way via our Slack and Jira.
You can find the details on the [feedback page](FEEDBACK.md).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

Copyright (c) 2018 Atlassian and others.
Apache 2.0 licensed, see [LICENSE.txt](LICENSE.txt) file.
