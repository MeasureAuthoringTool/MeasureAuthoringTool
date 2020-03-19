pipeline {
  agent any

  stages {
    stage('Build MAT Image') {
      steps {  
            sh "mvn install:install-file -Dfile=./lib/CQLtoELM-1.4.6.54.jar -DgroupId=mat -DartifactId=CQLtoELM -Dversion=1.4.6.54 -Dpackaging=jar"
            sh "mvn install:install-file -Dfile=./lib/vsac-1.0.jar -DgroupId=mat -DartifactId=vsac -Dversion=1.0 -Dpackaging=jar"
            sh "mvn install:install-file -Dfile=./lib/vipuserservices-test-client-1.0.jar -DgroupId=mat -DartifactId=vipuserservices -Dversion=1.0 -Dpackaging=jar"

            sh "mvn clean compile package -DskipTests"   
      }
    }
    stage('Push to ECR') {
      steps {
            sh "aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ${ECR_URL}/${ECR_REPO}"
            sh "docker build -t ${ECR_REPO} ."
            sh "docker tag ${ECR_REPO}:latest ${ECR_URL}/${ECR_REPO}:latest"
            sh "docker push ${ECR_URL}/${ECR_REPO}:latest"
            }   
      }
    }
}

