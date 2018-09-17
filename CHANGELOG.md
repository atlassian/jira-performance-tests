# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## API
The API consists of all public Java types from `com.atlassian.performance.tools.jiraperformancetests.api` and its subpackages:

  * [source compatibility]
  * [binary compatibility]
  * [behavioral compatibility] with behavioral contracts expressed via Javadoc

[source compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#source_compatibility
[binary compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#binary_compatibility
[behavioral compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#behavioral_compatibility

## [Unreleased]
[Unreleased]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/master%0Drelease-1.1.0

### Added
- Add an option to test on premise Jira with custom test scenario, which resolves [JPREF-84].
- Accept any `AppSource` in `AppImpactTest`. Work around [JPREF-93].

[JPREF-84]: https://ecosystem.atlassian.net/browse/JPERF-84
[JPREF-93]: https://ecosystem.atlassian.net/browse/JPERF-93

### Fixed
- Works around [JPERF-83].

[JPERF-83]: https://ecosystem.atlassian.net/browse/JPERF-83

## [1.1.0]
[1.1.0]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/release-1.1.0%0Drelease-1.0.0

### Added
- Add a way to test on premise Jira instance which resolves [JPREF-16](https://ecosystem.atlassian.net/browse/JPERF-16)

## [1.0.0]
[1.0.0]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/release-1.0.0%0Drelease-0.1.2

## [0.1.2]
[0.1.2]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/release-0.1.2%0Drelease-0.1.1

### Changed
- Use stable APIs.

### Added
- Added `AwsHousekeeping` to the API.

## [0.1.1]
[0.1.1]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/release-0.1.1%0Drelease-0.1.0

### Added
- Allow throttling virtual user diagnostics. 

### Changed
- Define the public API.

## [0.1.0]
[0.1.0]: https://bitbucket.org/atlassian/jira-performance-tests/branches/compare/release-0.1.0%0Drelease-0.0.3

### Changed
- Reshape `JiraPerformanceTest` into `AppImpactTest`.

### Added
- Choose deployment for Jira and number of nodes for DC.

### Fixed
- Expect a correct report count.
- Force updating snapshots when running `testRefApp`.
- Hint how to customize the `AppImpactTest`.
- Depend on a stable version of APT `infrastructure`.
- Depend on a stable version of APT `report`.

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
- Add this changelog.
- Enable Bitbucket Pipelines.

[JPT submodule]: https://stash.atlassian.com/projects/JIRASERVER/repos/jira-performance-tests/browse/jira-performance-tests?at=24b1522734605e8689a72396917e6080fddb8731
