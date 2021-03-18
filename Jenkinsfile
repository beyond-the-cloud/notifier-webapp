pipeline {
    agent any
    triggers {
        githubPush()
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building the application...'
                build job: 'BuildNotifierWebapp'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying the application...'
                build job: 'DeployNotifierWebapp'
            }
        }
    }
}