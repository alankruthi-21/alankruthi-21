pipeline{
    agent any
   parameters{
    string(name: 'BRANCH_NAME', defaultValue: '', description: '')
    string(name: 'BUILD_NUM', defaultValue: '', description: '')
    string(name: 'SERVER_IP', defaultValue: '', description: '')

  }
    stages{
       
        stage("deploy to multiple servers") {
            steps{
               
                sh '''
                aws s3 cp s3://alankruthiart/application2/${BUILD_NUM}/hello-${BUILD_NUM}.war .
                
                IFS=',' read -r -a ip  <<< "${SERVER_IP}"
                 for i in \"${ip[@]}\"
                 do
                 echo $i
                 
                 scp -o StrictHostKeyChecking=no -i /tmp/alankruthi21.pem hello-${BUILD_NUM}.war ec2-user@$i:/tmp
                 ssh -i /tmp/alankruthi21.pem ec2-user@$i "sudo cp /tmp/hello-${BUILD_NUM}.war /var/lib/tomcat/webapps"
               done
                 '''
            }
        }
    }
}