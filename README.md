[![DOI](https://zenodo.org/badge/141192128.svg)](https://zenodo.org/badge/latestdoi/141192128)

# Refactoring-involved Merge Conflict Analysis [![Build Status](https://travis-ci.com/ualberta-smr/RefactoringsInMergeCommits.svg?branch=master)](https://travis-ci.com/ualberta-smr/RefactoringsInMergeCommits)



## Dumped Data for Evaluation of IntelliMerge:

Directly use the dumped data used in the evaluation of IntelliMerge, no need to run the :

1. Download the dumped data: `stats/20190401.sql`;

2. Start MySQL 5.7 locally, import the data into it:

   ```sql
   mysql -u root -p
   Enter password: *****
   mysql> CREATE DATABASE refactoring_analysis;
   mysql> use refactoring_analysis
   mysql> source 20190401.sql
   ```

3. Show studied projects:

   ```sql
   mysql> show tables;
   mysql> select * from project;
   ```

4. To get the number of merge commits with commits for one project:

   ```sql
   select * from project where name="error-prone";
   select count(*) from merge_commit WHERE project_id=10 and is_conflicting=1 and timestamp < 1554048000;
   ```

   > 1554048000 = 2019.04.01

5. To get the csv file with detailed refactoring-involved merge scenarios inside it:

   1. Edit `stats/data_resolver.py` to config:

      ```python
      def get_db_connection():
          username = 'USER'
          password = 'PASSWORD'
          database_name = 'refactoring_analysis'
          server = '127.0.0.1'
          ...
      def get_merge_scenarios_involved_refactorings():
          # process one project each time
          repo_id = "10"
          repo_name = "error-prone"
          repo_paths = [
              'D:\\github\\repos\\'+repo_name
          ]
          csv_path = 'merge_scenarios_involved_refactorings_' + repo_name + '.csv'
      ```

   2. Run the main() function of the file.

---

## Run in Windows

> Notice: The original tool only support Linux or macOS, in order to run it in Windows, please follow the given instructions:

### Requirements

- Windows
- Git 2.18.0
- Java 8
- MySQL 5.7
- Intellij IDEA (with maven integration)

### Setup

1. Clone the repo and open it with IDEA, then import the dependencies;

2. Edit the `database.properties` file and config your MySQL username and password:

   ```json
   development.driver=com.mysql.jdbc.Driver
   development.username=USERNAME
   development.password=PASSWORD
   development.url=jdbc:mysql://localhost/refactoring_analysis
   ```

3. Run the `Main.main()`, an error will be reported as expected, the following steps are going to resolve it.

   ![step0](https://github.com/Symbolk/RefactoringsInMergeCommits/blob/master/screenshots/step0.png)

4. Open the `Maven` tab in IDEA, click `Run Maven Build` on the item `activejdbc-instrumentation` of the plugins:

   ![step1](https://github.com/Symbolk/RefactoringsInMergeCommits/blob/master/screenshots/step1.png)

5. Running results will be printed in the `Run` tab, go to the end of the first line, and copy the text highlighted in the following figure:

   ![step2-0](https://github.com/Symbolk/RefactoringsInMergeCommits/blob/master/screenshots/Step2-0.png)

   ![step2](https://github.com/Symbolk/RefactoringsInMergeCommits/blob/master/screenshots/step2.png)

5. Edit the running configuration, set the program arguments as follows:

   ![step3](https://github.com/Symbolk/RefactoringsInMergeCommits/blob/master/screenshots/step3.png)

   ![step4](https://github.com/Symbolk/RefactoringsInMergeCommits/blob/master/screenshots/step4.png)

6. Then add a `before launch` task `Run Maven Goal`, paste the text copied in Step 4 as the command line:

   ![step5](https://github.com/Symbolk/RefactoringsInMergeCommits/blob/master/screenshots/step5.png)

   ![step6](https://github.com/Symbolk/RefactoringsInMergeCommits/blob/master/screenshots/step6.png)

8. Run the `Main.main() `again, it should work now:

   ![step8](https://github.com/Symbolk/RefactoringsInMergeCommits/blob/master/screenshots/step7.png)

---

## Run in Linux/macOS

This project analyzes merge commits in git repositories and determines whether refactoring changes are to blame for the conflicts.


### System requirements
* Linux or macOS
* git
* Java 8
* MySQL 5.7

### How to run

#### 1. Create a database configuration file
Edit the `database.properties` file and add your MySQL username and password:
```
development.driver=com.mysql.jdbc.Driver
development.username=USERNAME
development.password=PASSWORD
development.url=jdbc:mysql://localhost/refactoring_analysis
```
If you wish, you can choose a different name for the database (`refactoring_analysis` in the template). Please make sure that another database with the same name doesn't exist, since it'll most likely have a different schema and the program will run into problems.

#### 2. Create a list of repositories
The program requires a text file consisting of the git repositories you want to analyze. Each line in the text file should   include the complete URL of a git repository. We have included the [reposList.txt](reposList.txt) file that contains 2,954 repositories.

#### 3. Build the project
Run the following command to compile the project and create an executable JAR file:
```
./mvnw clean activejdbc-instrumentation:instrument compile assembly:single clean
```
This command will create `refactoring-analysis.jar` file in the root directory.

#### 4. Run the JAR file
You can run the JAR file with the following command:
 ```
 java -jar refactoring-analysis.jar [OPTIONS]
 ```
 Note that none of the options are required. Here is a list of available options:
 ```
 -c,--clonepath <file>        directory to temporarily download repositories (default=projects)
 -d,--dbproperties <file>     database properties file (default=database.properties)
 -h,--help                    print this message
 -p,--parallelism <threads>   number of threads for parallel computing (default=1)
 -r,--reposfile <file>        list of repositories to be analyzed (default=reposList.txt)
 ```
 Here is an example command with all the options:
 ```
  java -jar refactoring-analysis.jar -r list.txt -c downloadedRepos -d mydb.properties -p 8 
 ```
 #### 5. Generate stat summaries
 After the program has finished running, you can use the Python scripts in the [stats](stats) folder to look at the results.
 - [data_resolver.py](stats/data_resolver.py): This script will print a summary of stats, including total number of merge scenarios, merge scenarios with involved refacotirngs, conflicting regions with involved refactorings, etc.
 - [plotter.py](stats/plotter.py): This script can draw a number of different plots.
