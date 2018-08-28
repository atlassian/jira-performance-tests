
### Setup of your instance

The testing process, through interacting with your instance will make the following changes:

1. Globally disable the Rich Text Editor.
2. Affect the browse history of the tested user (recent issues, recent boards, current JQL search).
3. Affect data displayed by the Activity Stream.
4. Create roughly 4 issues per minute, which means roughly 80 issues (with default load settings).
5. Create roughly 2 comments per minute, which means roughly 30 comments (with default load settings).
6. Load caches and generate resources for visited pages.

The above factors may or may not influence subsequent test results. Depending on actual conditions 400 issues may be negligible to data already contained on the instance. Data growth not exceeding 0.5% can safely be considered negligible.

For most relevant results, it is advised to roll back these changes. In order to be able to reason from the results, always start the test from the same initial conditions. Preparation of the instance prior to each benchmark should include, but not necessarily be limited to:

1. restoring data on the instance,
2. restarting Jira.

