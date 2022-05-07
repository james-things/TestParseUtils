# TestParseUtils

A collection of parsing and printing utilities handling XML and CSV files.

These utilities were created to assist in completing a jUnit test analysis project,
and are specifically written for their purposes.

The current set of options is meant to be run against the following repository:
https://github.com/james-things/java-test-prioritization-projects

The assumption hardcoded in is that the repo will be cloned to C:\repos\testing-projects\ and as such the code must be
updated if this does not match the location that the repository is cloned to.

This application presents the following options and associated functionality:

Print outputs of jUnit XML files in columns suitable to be imported into Excel:
  
  1. Print JSON-java tests
  2. Print junrar tests
  3. Print SimplifyLearning tests
  4. Print telek-math tests

Print a list of "Sum Functions" to insert into an Excel document
  
  6. Print spreadsheet sum functions

Print the ordering and results of two contrasting test prioritization strategies: (WIP)
  
  7. Print junrar csv
  8. Print SimplifyLearning csv
  9. Print telek-math csv
