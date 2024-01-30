pipeline {
    agent any



    tools {
    nodejs 'node'
    }

      environment {
            DOCKER_IMAGE_Back_NAME = 'brain99/devops_project_back:spring'
            DOCKER_IMAGE_Front_NAME = 'brain99/devops_project_front:angular'

        }


    stages {

        

        stage('Checkout Backend code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], userRemoteConfigs: [[url: 'https://github.com/manaziz98/Project_Devops.git']]])
            }

        }





        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Junit Test') {
            steps {
                // Run tests and collect test results
                sh 'mvn test' // Modify the test command as needed

                // Archive test results for Jenkins to display
                junit '**/target/surefire-reports/*.xml'
            }
        }
            stage('SonarQube') {
                       steps {
                           script {
                               // Run the SonarQube analysis
                               sh 'mvn clean verify sonar:sonar ' +
                                  '-Dsonar.projectKey=sonar ' +
                                  '-Dsonar.projectName=\'sonar\' ' +
                                  '-Dsonar.host.url=http://192.168.33.10:9000 ' +
                                  '-Dsonar.token=sqp_890d6702edbe35a5b006df8975b5271b01c399d9'
                           }
                       }
                   }

                                               stage("Publish to Nexus Repository Manager") {
                                                   steps {
                                                       script {
                                                           pom = readMavenPom file: "pom.xml";
                                                           filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                                                           echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                                                           artifactPath = filesByGlob[0].path;
                                                           artifactExists = fileExists artifactPath;
                                                           if(artifactExists) {
                                                               echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                                                               nexusArtifactUploader(
                                                                   nexusVersion: 'nexus3',
                                                                   protocol: 'http',
                                                                   nexusUrl: '192.168.33.10:8081',
                                                                   groupId: 'pom.tn.esprit',
                                                                   version: 'pom.1.0',
                                                                   repository: 'maven-central-repo',
                                                                   credentialsId: 'NEXUS_CRED',
                                                                   artifacts: [
                                                                       [artifactId: 'pom.DevOps_Project',
                                                                       classifier: '',
                                                                       file: artifactPath,
                                                                       type: pom.packaging],
                                                                       [artifactId: 'pom.DevOps_Project',
                                                                       classifier: '',
                                                                       file: "pom.xml",
                                                                       type: "pom"]
                                                                   ]
                                                               );
                                                           } else {
                                                               error "*** File: ${artifactPath}, could not be found";
                                                           }
                                                       }
                                                   }
                                               }

                                                stage('Build image spring') {
                                                           steps {
                                                               script {
                                                                   // Build the Docker image for the Spring Boot app
                                                                   sh "docker build -t $DOCKER_IMAGE_Back_NAME ."
                                                               }
                                                           }
                                                       }

         stage('Push image Spring') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'DOCKERHUB_CRED',url: ""]) {
                        // Push the Docker image to Docker Hub
                        sh "docker push $DOCKER_IMAGE_Back_NAME"
                    }
                }
            }
          }                

 

    }





              


    
    post {
        success {

             emailext subject: 'Successful Build Notification',
                body: 'The Jenkins pipeline build was successful.',
                to: 'zizoumanai98@gmail.com',
                from: 'zizoumanai98@gmail.com'
            echo 'Build successful!'
        }

        failure {

             emailext subject: 'Failed Build Notification',
                body: 'The Jenkins pipeline build failed. Please investigate.',
                to: 'zizoumanai98@gmail.com',
                from: 'zizoumanai98@gmail.com'
            echo 'Build failed. Please investigate.'
        }

    }


   }
