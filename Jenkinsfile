def ssh_publisher(SERVER_CONFIG) {
    sshPublisher(
        continueOnError: false,
        failOnError: true,
        publishers:[
            sshPublisherDesc(
                configName: "${SERVER_CONFIG}",
                verbose: true,
                transfers: [
                    sshTransfer(
                        execCommand: "sh ~/script/deploy.sh"
                    )
                ]
            )
        ]
    )
}

pipeline {
    agent any

    environment {
        SERVER_LIST = 'server1,server2'
    }

    stages {
        stage('Build Stage') {
            steps {
                sh 'chmod +x ./gradlew'
                sh "./gradlew clean bootJar"
            }
        }

         stage('Run tests') {
            steps {
                sh "./gradlew test"
                 junit '**/build/test-results/test/*.xml'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    app = docker.build('sukeun/home-delivery')
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com/', 'docker-hub') {
                        app.push('latest')
                    }
                }
            }
        }

        stage('Remove Docker Image') {
            steps {
                sh 'docker rmi sukeun/home-delivery'
                sh 'docker rmi registry.hub.docker.com/sukeun/home-delivery'
            }
        }

        stage('Deploy') {

/*
            when {
                branch 'develop'
            }
 */

            steps {
              script {
                SERVER_LIST.tokenize(',').each {
                  echo "SERVER: ${it}"
                  ssh_publisher("${it}")
                }
              }
            }
        }
    }
}