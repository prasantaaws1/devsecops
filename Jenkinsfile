@Library("my-shared-library") _

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
      
    }
}
