pipeline {
    agent any

    environment {
        app = ''
    }

    stages {

        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build'
                junit '**/build/test-results/test/*.xml'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
    }
}