@Library("My_Shared_Library") _

pipeline {
  agent any

  stages {
    stage('From Shared Library') {
            steps {
              hello()
            }
        }
      stage('Build Artifact') {
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
                  -Dsonar.host.url=http://3.101.147.129:9000 \
                  -Dsonar.token=sqp_0eb57b1396b6e9d806f627216211de0952398738"
            }
        }
    }
}
