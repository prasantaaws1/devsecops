def call(String repoUrl){
  pipeline {
       agent any
        // tools {
        //     maven 'Maven 3.6.3'
        //     jdk 'OpenJDK11'
        // }

      environment{
        SONAR_USER_HOME="\${WORKSPACE}/.sonar"
        
      }
       stages {
           stage("Tools initialization") {
               steps {
                   sh "mvn --version"
                   sh "java -version"
               }
           }
           stage("Clone Repository") {
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
           
           stage("Maven Install") {
              steps {
                   sh '''mvn -B -e org.jacoco:jacoco-maven-plugin:0.8.5:prepare-agent clean install -Dmaven.wagon.http.ssl.insecure=true -f pom.xml -Dmaven.test.skip='true' \
                    -DjvmArgs="-Xmx1G -XX:PermSize=128m -XX:MaxPermSize=256m" '''
               }
           }
         
         stage('Sonar') {
            steps {
              sh '''
              curl -k -o /tmp/sample.txt https://reqbin.com/echo
              chmod 777 /tmp/sample.txt  
              mvn clean verify sonar:sonar \
                -Dsonar.projectKey=demo \
                -Dsonar.projectName='demo' \
                -Dsonar.host.url=http://54.219.167.251:9000 \
                -Dsonar.token=sqp_d4a203a12b9a2d2e3ec5731b4be5887beae4e1ec
              '''
            }
        }
       }
   }

}
