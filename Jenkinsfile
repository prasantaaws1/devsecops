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
              buildArtifacts()
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
