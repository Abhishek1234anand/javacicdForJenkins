pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials-id')
        DOCKER_IMAGE = "YOUR_DOCKERHUB_USERNAME/demo-app"
        KUBECONFIG_CREDENTIALS = credentials('kubeconfig-credentials-id')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn -B clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageTag = "latest"
                    bat """
                    docker build -t %DOCKER_IMAGE%:${imageTag} .
                    """
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withEnv(["DOCKERHUB_USER=${DOCKERHUB_CREDENTIALS_USR}", "DOCKERHUB_PASS=${DOCKERHUB_CREDENTIALS_PSW}"]) {
                        bat """
                        echo %DOCKERHUB_PASS% | docker login -u %DOCKERHUB_USER% --password-stdin
                        docker push %DOCKER_IMAGE%:latest
                        """
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withEnv(["KUBECONFIG=${KUBECONFIG_CREDENTIALS}"]) {
                        writeFile file: 'kubeconfig', text: KUBECONFIG_CREDENTIALS
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

