pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "abhishek0000111/demo-app"   // ✅ Correct repo name
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Ensure Maven is installed and in PATH
                bat 'mvn -B clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageTag = "latest"
                    bat "docker build -t ${DOCKER_IMAGE}:${imageTag} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials-id',
                                                 usernameVariable: 'DOCKER_USER',
                                                 passwordVariable: 'DOCKER_PASS')]) {
                    bat """
                    echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                    docker push ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([string(credentialsId: 'kubeconfig-credentials-id', variable: 'KUBECONFIG_CONTENT')]) {
                    script {
                        writeFile file: 'kubeconfig', text: KUBECONFIG_CONTENT
                        withEnv(["KUBECONFIG=${pwd()}\\kubeconfig"]) {
                            bat """
                            kubectl apply -f k8s/deployment.yaml
                            kubectl apply -f k8s/service.yaml
                            """
                        }
                    }
                }
            }
        }
    }
}
