pipeline {
    agent any

    environment {
        app = ''
    }

    stages {
        stage('Build Stage') {
            steps {
                sh 'chmod +x ./gradlew'
                sh "./gradlew clean bootJar"
            }
        }

/*         stage('Run tests') {
            steps {
                sh "./gradlew test"
                junit '**//* build/test-results/test *//*.xml'
            }
        } */

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
            }
        }

        stage('Deploy') {
            steps([$class: 'BapSshPromotionPublisherPlugin']) {
                sshPublisher(
                    continueOnError: false, failOnError: true,
                    publishers: [
                        sshPublisherDesc(
                            configName: "home-delivery-deploy",
                            verbose: true,
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "",
                                    removePrefix: "",
                                    remoteDirectory: "",
                                    execCommand: "sh ~/script/deploy.sh"
                                )
                            ]
                        )
                    ]
                )
            }
        }
    }
}