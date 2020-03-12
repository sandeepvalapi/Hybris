pipeline { 
    agent any 
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('System Check') { 
            steps { 
                sh 'echo $JAVA_HOME' 
                sh 'cd /home/SW_HYBRIS/COMM_1905/hybris/bin/platform'
                sh './setantenv.sh'
                sh 'cd /var/jenkins_home/workspace/Training/bin/custom'
                sh 'zip -r training.zip .'
            }
        }
        stage('Copying artifacts'){
            steps {
                sh 'rm -rf /home/SW_HYBRIS/COMM_1905/hybris/bin/custom'
                sh 'mkdir -p /home/SW_HYBRIS/COMM_1905/hybris/bin/custom'
                sh 'cp training.zip /home/SW_HYBRIS/COMM_1905/hybris/bin/custom/'
                sh 'cd /home/SW_HYBRIS/COMM_1905/hybris/bin/custom/'
                sh 'unzip training.zip'
                sh 'rm training.zip'
                sh 'cp /var/jenkins_home/workspace/Training/config/local.properties /home/SW_HYBRIS/COMM_1905/hybris/config'
                sh 'cp /var/jenkins_home/workspace/Training/config/localextensions.xml /home/SW_HYBRIS/COMM_1905/hybris/config'

            }
        }
        stage('Deploy') {
            steps {
                sh 'echo Ready for Deployment'
            }
        }
    }
}