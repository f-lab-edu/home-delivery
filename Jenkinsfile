pipeline {
    agent any

    environment {
        app = ''
    }

    stages {

        stage('Build') {
            steps {
                sh 'chmod +x -R ${env.WORKSPACE}'
                sh './gradlew clean build'
                archiveArtifacts 'build/libs/*.jar'
            }
        }
    }
}