#!groovy​

pipeline {
    agent any
    
    tools {
        jdk 'oracle-jdk8-latest'
    }
    
    options {
        timestamps() 
        skipStagesAfterUnstable()
    }
    
    stages {

        stage('Build') {
            steps {
                script {
                    println("Starting codewind-openapi-eclipse build ...")
                        
                    def sys_info = sh(script: "uname -a", returnStdout: true).trim()
                    println("System information: ${sys_info}")
                    println("JAVE_HOME: ${JAVA_HOME}")
                    
                    sh '''
                        java -version
                        which java    
                    '''
                    
                    dir('dev') { sh './gradlew --stacktrace' }
                }
            }
        } 
        
        stage('Deploy') {
            steps {
                sshagent ( ['projects-storage.eclipse.org-bot-ssh']) {
                  println("Deploying codewind-openapi-eclipse to downoad area...")
                  
                  sh '''
                      export sshHost="genie.codewind@projects-storage.eclipse.org"
                      export deployDir="/home/data/httpd/download.eclipse.org/codewind/codewind-openapi-eclipse"

                      if [ -z $CHANGE_ID ]; then
		          UPLOAD_DIR="$GIT_BRANCH/$BUILD_ID"
		          BUILD_URL="https://download.eclipse.org/codewind/codewind-openapi-eclipse/$UPLOAD_DIR"
		          
		          ssh $sshHost rm -rf $deployDir/$GIT_BRANCH/latest
                  ssh $sshHost mkdir -p $deployDir/$GIT_BRANCH/latest
    			  cp $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse-*.zip $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse.zip
    			  scp $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse.zip $sshHost:$deployDir/$GIT_BRANCH/latest/codewind-openapi-eclipse.zip
                  rm $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse.zip 		
                  
                  echo "BUILD_URL=$BUILD_URL" >> build_info.properties
                  	  
    			  unzip $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse-*.zip -d $WORKSPACE/dev/ant_build/artifacts/repository/codewind
		      else
    			  UPLOAD_DIR="pr/$CHANGE_ID/$BUILD_ID"
                  BUILD_URL="https://download.eclipse.org/codewind/codewind-openapi-eclipse/$UPLOAD_DIR"
                  
                  ssh $sshHost rm -rf $deployDir/$GIT_BRANCH/latest
                  ssh $sshHost mkdir -p $deployDir/$GIT_BRANCH/latest
                  cp $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse-*.zip $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse.zip
                  scp $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse.zip $sshHost:$deployDir/$GIT_BRANCH/latest/codewind-openapi-eclipse.zip
                  rm $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse.zip      
                  
                  echo "BUILD_URL=$BUILD_URL" >> build_info.properties
                      
                  unzip $WORKSPACE/dev/ant_build/artifacts/codewind-openapi-eclipse-*.zip -d $WORKSPACE/dev/ant_build/artifacts/repository/codewind
		      fi
 		      
		      ssh $sshHost rm -rf $deployDir/${UPLOAD_DIR}
                      ssh $sshHost mkdir -p $deployDir/${UPLOAD_DIR}
                      scp -r $WORKSPACE/dev/ant_build/artifacts/* $sshHost:$deployDir/${UPLOAD_DIR}

                  '''
                }
            }
        }       
    }    
}
