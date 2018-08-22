# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]
[Unreleased]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/master%0Drelease-0.0.3

## [0.0.3]
[0.0.3]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/release-0.0.3%0Drelease-0.0.2

### Added
- Allow running performance tests without extra Jira apps installed.
- Generate charts in app tests. [JPERF-9](https://ecosystem.atlassian.net/browse/JPERF-9).
- Explain [contribution guidelines](CONTRIBUTING.md).

### Fixed
- Correctly label the experiment test cohort.
- Distinguish between cohorts even if we test the same version of a plugin. [JPERF-7](https://ecosystem.atlassian.net/browse/JPERF-7).
- Allow tests to consume custom datasets created within the same task.
- Fix scanner errors in log. See [JPERF-10](https://ecosystem.atlassian.net/browse/JPERF-10).
- Print a full stack trace when ref app fails. [JPERF-8](https://ecosystem.atlassian.net/browse/JPERF-8).

## [0.0.2] - 2018-08-08
[0.0.2]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/release-0.0.2%0Drelease-0.0.1

### Fixed
- Add the missing virtual users main class. See [JPERF-2](https://ecosystem.atlassian.net/browse/JPERF-2).

## [0.0.1] - 2018-08-07
[0.0.1]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/release-0.0.1%0Dinitial-commit

### Added
- Migrate high-level test API from [JPT submodule].
- Add [README.md](README.md).
- Add [CHANGELOG.md](CHANGELOG.md).
- Enable Bitbucket Pipelines.

[JPT submodule]: https://stash.atlassian.com/projects/JIRASERVER/repos/jira-performance-tests/browse/jira-performance-tests?at=24b1522734605e8689a72396917e6080fddb8731
