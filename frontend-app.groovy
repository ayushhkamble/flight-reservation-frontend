pipeline{
    agent any
    stages{
        stage('Code-Pull'){
            steps{
                git branch: 'main', url: 'https://github.com/mayurmwagh/flight-reservation-frontend.git'    
            }
        }
        stage('Code-Build'){
            steps{
                sh '''
                    npm install
                    npm run build
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh '''
                aws s3 sync dist/ s3://cblkdfdnewcjdnd-project-bux/ 
               '''  
            }
        }
    }
}