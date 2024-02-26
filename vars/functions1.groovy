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
           // stage("Checkout Code") {
           //     steps {
           //         git branch: 'main',
           //             url: "${repoUrl}"
           //     }
           // }
         stage("Clone Repo") {
               steps {
                   checkout changelog: false, poll: false, scm: [ $class: 'GitSCM', branches: [[ name: 'main' ]], extensions: [[ $class: 'CloneOption', shallow: true, depth: 1, timeout: 10, noTags: true ]], userRemoteConfigs: [[ url: ${repoUrl} ]] ]
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
                   sh '''mvn -B -e org.jacoco:jacoco-maven-plugin:0.8.5:prepare-agent clean install -Dmaven.wagon.http.ssl.insecure=true -f pom.xml -Dmaven.test.skip='true' \
                    -DjvmArgs="-Xmx1G -XX:PermSize=128m -XX:MaxPermSize=256m" '''
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
              sh "mvn -B -f pom.xml "
              // org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar "
            }
        }
       }
   }

}
