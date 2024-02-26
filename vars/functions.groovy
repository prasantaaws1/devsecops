// vars
// | --- functions.groovy

def call(String repoUrl){
  pipeline {
       agent any
       // tools {
       //     maven 'Maven 3.6.3'
       //     jdk 'jdk8'
       // }
       stages {
           stage("Tools initialization") {
               steps {
                   sh "mvn --version"
                   sh "java -version"
               }
           }
           stage("Checkout Code") {
               steps {
                   git branch: 'main',
                       url: "${repoUrl}"
               }
           }
           stage("Cleaning workspace") {
               steps {
                   sh "mvn clean"
               }
           }
           stage("Running Testcase") {
              steps {
                   sh "mvn test"
               }
           }
           stage("Packing Application") {
               steps {
                   sh "mvn package -DskipTests"
               }
           }
         stage("Build Artifact") {
               steps {
              sh "mvn clean package -DskipTests=true"
              archive 'target/*.jar' //so that they can be downloaded later
               }
           }
         stage('SonarQube') {
            steps {
              sh "mvn sonar:sonar \
                  -Dsonar.projectKey=demo \
                  -Dsonar.projectName='demo' \
                  -Dsonar.host.url=http://3.101.147.129:9000 \
                  -Dsonar.token=sqp_0eb57b1396b6e9d806f627216211de0952398738"
            }
        }
       }
   }

}
