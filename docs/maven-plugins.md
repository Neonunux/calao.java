## Maven plugins infos
  * [Apache Maven Source Plugin](https://maven.apache.org/plugins/maven-source-plugin/)
   * The Source Plugin creates a jar archive of the source files of the current project. The jar file is, by default, created in the project's target directory.
  * [Apache Maven Deploy Plugin]( https://maven.apache.org/plugins/maven-deploy-plugin/) 
   * The deploy plugin is primarily used during the deploy phase, to add your artifact(s) to a remote repository for sharing with other developers and projects. This is usually done in an integration or release environment. It can also be used to deploy a particular artifact (e.g. a third party jar like Sun's non redistributable reference implementations).

### Artifact 
A Maven build produces one or more artifacts, such as a compiled JAR and a "sources" JAR.

Each artifact has a group ID (usually a reversed domain name, like com.example.foo), an artifact ID (just a name), and a version string. The three together uniquely identify the artifact.

A project's dependencies are specified as artifacts.

### Lifecycle (default)
 * validate - validate the project is correct and all necessary information is available
 * compile - compile the source code of the project
 * test - test the compiled source code using a suitable unit testing framework. These tests should not require the code be packaged or deployed
 * package - take the compiled code and package it in its distributable format, such as a JAR.
 * integration-test - process and deploy the package if necessary into an environment where integration tests can be run
 * verify - run any checks to verify the package is valid and meets quality criteria
 * install - install the package into the local repository, for use as a dependency in other projects locally
 * deploy - done in an integration or release environment, copies the final package to the remote repository for sharing with other developers and projects.
 
 stage
 phase
 goals
 Un goal est une tâche précise que Maven est en mesure de réaliser à partir des informations qu'il pourra trouver dans le fichier pom.xml.
 
  A partir de la compréhension qu'il a du projet, il est capable d'exécuter énormément d'opérations dessus, chaque opération correspondant à un goal.

  