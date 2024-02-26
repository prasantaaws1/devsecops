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
           // stage("Running Testcase") {
           //    steps {
           //         sh "mvn test"
           //     }
           //   post{
           //     always{
           //       junit 'target/surefire-reports/*.xml'
           //       jacoco execPattern: 'target/jacoco.exec'
           //     }
           //   }
           // }
 stage("Running Testcase") {
              steps {
                   sh "mvn -B -e org.jacoco:jacoco-maven-plugin:0.7.4:prepare-agent clean install -Dmaven.wagon.http.ssl.insecure=true"
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
              sh "mvn clean verify sonar:sonar \
  -Dsonar.projectKey=demo \
  -Dsonar.projectName='demo' \
  -Dsonar.host.url=http://54.219.167.251:9000 \
  -Dsonar.token=sqp_d4a203a12b9a2d2e3ec5731b4be5887beae4e1ec"
            }
        }
       }
   }

}
