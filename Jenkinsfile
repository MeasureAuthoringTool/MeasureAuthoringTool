pipeline {
  agent any

  stages {
    stage('Notify'){
      steps {
              slackSend  color: "#439FE0", message: "Build Started: ${env.JOB_NAME} [${env.BUILD_NUMBER}] (<${env.BUILD_URL}|Open>)"
            }
    }
    stage('Build MAT Image') {
      steps {
              sh "mvn clean compile package -DskipTests"   
            }
    }
    stage('Push to ECR') {
      steps {
            sh '''
               aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ${ECR_URL}/${ECR_REPO}
               docker build -t ${ECR_REPO} --build-arg STOREPASS='${STOREPASS}' --build-arg NR_APM_NAME='${NR_APM_NAME}' --build-arg NR_APM_KEY='${NR_APM_KEY}'  .
               docker tag ${ECR_REPO}:latest ${ECR_URL}/${ECR_REPO}:latest
               docker tag ${ECR_REPO}:latest ${ECR_URL}/${ECR_REPO}:${GIT_COMMIT:0:8}
               docker push ${ECR_URL}/${ECR_REPO}:latest
               docker push ${ECR_URL}/${ECR_REPO}:${GIT_COMMIT:0:8}
               '''
            }   
      }
    stage("Deploy") {
      steps {
            // --no-deregister, this lets us easily rollback by having old versions present
            // --task is defined, this tells it to base new deployment off latest task definition, not current in use task def.
            //   required as a result of changes that may occur from terraform which will be new tasks definitions
            //   that are not deployed.
            sh '''
               ecs deploy -t ${GIT_COMMIT:0:8} --no-deregister --region us-east-1 --timeout 300 --task ${SERVICE} ${CLUSTER_NAME} ${SERVICE}
               aws ecs wait services-stable --cluster ${CLUSTER_NAME} --services ${SERVICE} --region us-east-1
               '''
          }
      }
    }
    post {
        success{
            slackSend  color: "#439FE0", message: "Build Completed Successfully: ${env.JOB_NAME} [${env.BUILD_NUMBER}] (<${env.BUILD_URL}|Open>)"
        }
        failure{
            slackSend  color: "#ff0000", message: "Build Failed: ${env.JOB_NAME} [${env.BUILD_NUMBER}] (<${env.BUILD_URL}|Open>)"
        }
    }
}

