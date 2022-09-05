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

        stage('Run tests') {
            steps {
                sh "./gradlew test"
                junit '**/build/test-results/test/*.xml'
            }
        }
    }
}