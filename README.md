# ![QUIZKI](./src/main/webapp/images/logo-light.png) **README**

A web application to help students of all stripes master their subject matter with questions and answers they create, which are then shared with and validated by other students who are studying the same topics. The interface (will) allow the creation of any question, including, but certainly not limited to, flashcards.

Go ahead, experience [quizki](http://www.quizki.com) live! Afterward take time to read [about quizki](./ABOUT) for a general understanding of how the system works before getting started as a developer or tester.


## Getting Started


The following paragraphs explain how to get a copy of quizki up and running on your local machine for development and testing purposes. 

See the Deployment section for notes on how to deploy the project on a live system.

### Prerequisites

Listed below are the software products you will need resident on your local machine. 

For each product, install the most recent generally available (GA) version. If you live on the edge, feel free to use the latest Alpha or Beta versions. Yet, if you are fairly new to programing, Alpha and Beta versions may contain problematic errors making it difficult to determine if your code or the software you are using is causing an anomaly. Thus, stay with the latest GA version to reduce complexity, as you learn to code.

- [Git](https://git-scm.com/downloads/) - Git and [GitHub](https://github.com/) are used for version control 
- [Apache Tomcat](http://tomcat.apache.org/) - server used to run backend application
- [Apache Maven](http://maven.apache.org/) - used to build the quizki initially and after code changes
- [MySQL Community Edition (GPL)](https://dev.mysql.com/downloads/) - database used with quizki
- browsers - [Google Chrome](http://google.com/chrome/), [Mozilla Foxfire](https://www.mozilla.org/en-US/firefox/), [Microsoft Internet Explorer](http://windows.microsoft.com/ie/) or other browsers
- [Eclipse](https://www.eclipse.org/) - Eclipse is the recommended Integrated Development Environment (IDE) to use. Yet, any IDE will work provided it supports Java with Maven builds.

### Installing

Installion of the tools listed above will vary slightly based upon the operating system you are using. If you are using a Linux/BSD system, check the [DEVELOPER](./DEVELOPER) file. If you are using Windows, check [DEVELOPER_WINDOWS](./DEVELOPER_WINDOWS.txt). If you are using a MAC, check [DEVELOPER_MAC](./DEVELOPER_MAC.txt). Each file can be found in the root directory of Quizki.


## Quizki Overview


Quizki is a full stack application.

### Backend

The backend is written in Java, JSPs, and JavaScript. 

The virtual server is a Tomcat server. The system interfaces with the MySQL to store information for future use. 

### Frontend

The frontend is written with JavaScript, HTML and CSS.

The frontend & backend uses the Backbone.js v1.1.2 (an older version) to dynamically create the HTML/CSS with which the user interacts. To understand backbone.js v1.1.2 access the documentation. Using GitHub, fork [Backbone.js](https://github.com/jashkenas/backbone/). Clone to your local computer, then git checkout 1.1.2 after which you can read the 1.1.2 documentation. 


### Database

The quizki_db is a MySQL database. To investigate the database schema you can use [SchemaSpy](http://schemaspy.sourceforge.net/). If you have installed MySQL Workbench, then you could alternatively reverse engineer then forward engineer to understand the fields and indexes.


## Testing


### Backend tests 

Debuging: You will need to start the Tomcat server with the `-Xdebug -Xrunjdwp ....` parameter set. Add details to the %TOMCAT_HOME%/bin/setenv.bat (windows) or %TOMCAT_HOME%/bin/setenv.sh (Linux/BSD). Here is an example for the setenv.bat:

	set CATALINA_OPTS=%CATALINA_OPTS% -Xdebug -Xrunjdwp:transport=dt_socket,address=8088,server=y,suspend=n

Unit Testing: In your chosen IDE you can select and run individual JUnit tests. Or you can use the command line to run the entire suite of JUnit test available for quizki. From the command line, change to %QUIZKI_HOME% and run "mvn test". Remember to rebuild the war file if before you run "mvn test", if you made any code changes.
	

### Frontend tests

Debuging: Most modern web browsers provide the abiltity to inspect elements and view source code. Just right click an object in the broswers and select Inspect Element at which time you can work with the browser's debug capabilites and set breakpoints.

Unit Testing: No automated tests exist for frontend unit testing.


## Deployment


For instructions on how to deploy quizki on Blue Ocean read the [Install](./Install) file in quizki root directory.


## Contributing

Our Code of Conduct is a very simple rule: "Be nice or be gone."

To contribute to quizki project join the [Denver Mock Program Job MeetUp](https://www.meetup.com/Denver-Mock-Programming-Job-Meetup/). We meet for biweekly Agile planning sessions on Monday evenings, and have 15 min Standup meetings on Tuesday and Thursday mornings. Details are in the MeetUp. After joining you will be invited to access various tools: Slack communications channel, Trello and others. After setting up your Trello board, pick an issue from the "DMPJ - quizki" Trello backlog to work. The issues each refer to the GitHub issues on [haxwell/quizki](https://github.com/haxwell/quizki).

For each issue you work, create a local/featureBranch off your local/devBranch. We work both fixes and new features using featureBranches. After you have tested the issue's solution, push the local/featureBranch to origin/featureBranch on GitHub, then issue a pull request to haxwell/quizki for your origin/featureBranch.


If you are working a complex issue, then feel free to issue an inprogress pull request to discuss the progress and pontential solutions. Corrections may be recommended. If so, you can merge recommendations your local/featureBranch. Just fetch, pull, merge into your local/featureBranch. Afterward push the updated local/featureBrancg to your origin/featureBranch.


## Versioning


The current version is quizki-1.3. Future versions will use [CalVer](https://calver.org/) for versioning and will be tagged. To access versions available, see the [tags](./tags.md) in the root directory of Quizki. 


## Authors


* **Johnathan E. James** - *Creator of quizki 1.3!* - [haxwell](https://github.com/haxwell)

See also the list of [contributors](./contributors) who participated with later versions of this project. 


## License


This project is licensed under the GNU GENERAL PUBLIC LICENSE - see the [LICENSE](./LICENSE) file for details.


## Acknowledgments


* **Billie Thompson** - *for the terrific README.md template* - [PurpleBooth](https://github.com/PurpleBooth)

* **backbonejs.org** - *for the backbone.js version 1.1.2 used on this project* - [backbone.js on GitHub](https://github.com/jashkenas/backbone) 



(c) 2013, 2014, 2018 Johnathan E. James
