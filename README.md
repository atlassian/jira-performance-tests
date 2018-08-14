# Jira Performance Tests (JPT)

Jira Performance Tests is an integrated solution for testing performance of Jira Server and Jira Data Center. 
The tool is fully automated and takes care of all the aspects of the process. It will set-up
a new Jira instance, run performance tests and generate a report.

JPT measures user experience and gives you the ability to test both front-end and back-end performance at the same time. 
Instead of simple HTTP traffic, it users browsers to test end-to-end interactions.

Atlassian uses JPT internally to produce
[scaling Jira reports](https://confluence.atlassian.com/enterprise/scaling-jira-867028644.html).

## Run this 

If you **already have a test environment**, see [BTF tests](docs/tests/BTF.md).

If you're a **Jira App developer** and would like to test your App, see our 
[app tests](docs/tests/APP.md). 

If you're an **Atlassian Developer**, 
see our [internal tests](https://stash.atlassian.com/projects/JIRASERVER/repos/jira-performance-tests/browse/README.md).
 
## Features

  - Runs performance tests
  - Provisions Jira instance in AWS infrastructure
  - Generates a report
  - Generates a chart
  - Uses configuration as a code
  - Provides a large Jira dataset
  - Uses data agnostic scenarios
  - Starts with reasonable defaults
  - Provides a way to benchmark Jira
  - Gathers system and GC metrics
  - Supports Jira 7.2 - 7.11
  - (planned) Compares Jira with and without plugin installed
  - (planned) Supports Jira 8+

## Reporting issues

(**incomplete**)

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

Copyright (c) 2018 Atlassian and others.
Apache 2.0 licensed, see [LICENSE.txt](LICENSE.txt) file.


