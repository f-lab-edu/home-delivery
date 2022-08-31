def remote = [:]

pipeline {
    agent any

    environment {
        app = ''
    }

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
    }
}