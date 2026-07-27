pipeline {
    agent any

    environment {
        S3_BUCKET = "cbz-frontend-project-bucket-flight-reservation-app"
        AWS_REGION = "eu-north-1"   // Change if your bucket is in another region
    }

    stages {
        stage('Code-Pull') {
            steps {
                git branch: 'main', url: 'https://github.com/ayushhkamble/flight-reservation-frontend.git'
            }
        }

        stage('Code-Build') {
            steps {
                sh '''
                    npm install
                    npm run build
                '''
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([aws(
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                    credentialsId: 'aws_creds',
                    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                )]) {
                    sh '''
                        aws s3 sync dist/ s3://${S3_BUCKET}/
                    '''
                }
            }
        }

        stage('Display Website URL') {
            steps {
                script {
                    echo "=============================================="
                    echo "Frontend deployed successfully!"
                    echo "S3 Static Website URL:"
                    echo "http://${S3_BUCKET}.s3-website-${AWS_REGION}.amazonaws.com"
                    echo "=============================================="
                }
            }
        }
    }
}