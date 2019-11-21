pipeline {
  agent {
    kubernetes {
      label 'mat'  
      idleMinutes 5
      yamlFile 'pod.yaml' 
      defaultContainer 'maven'  
    }
  }
  stages {
    stage('Build Mvn Project') {
      steps {  
        container('maven') {
            sh "mvn install:install-file -Dfile=./lib/CQLtoELM-1.4.6.54.jar -DgroupId=mat -DartifactId=CQLtoELM -Dversion=1.4.6.54 -Dpackaging=jar"
            sh "mvn install:install-file -Dfile=./lib/vsac-1.0.jar -DgroupId=mat -DartifactId=vsac -Dversion=1.0 -Dpackaging=jar"
            sh "mvn install:install-file -Dfile=./lib/vipuserservices-test-client-1.0.jar -DgroupId=mat -DartifactId=vipuserservices -Dversion=1.0 -Dpackaging=jar"
            sh "mvn clean compile package -DskipTests"   
        }
      }
    }
    stage('Build Docker Image and push') {
      steps {
        container('docker') {  
            script {
                def dockerImage = docker.build("mat-dev:${env.BUILD_ID}")
                docker.withRegistry('https://498284886784.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:sb-bmat-jenkins') {
                    dockerImage.push()
                    dockerImage.push('latest')
                }
            }   
        }
      }
    }
  }
}
