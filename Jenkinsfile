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
                  REPO_NAME="codewind-openapi-eclipse"
                  OUTPUT_DIR="$WORKSPACE/dev/ant_build/artifacts"
                  DOWNLOAD_AREA_URL="https://download.eclipse.org/codewind/$REPO_NAME/"
                  LATEST_DIR="latest"
                  
                  sh '''
                      export sshHost="genie.codewind@projects-storage.eclipse.org"
                      export deployDir="/home/data/httpd/download.eclipse.org/codewind/$REPO_NAME"

                      if [ -z $CHANGE_ID ]; then
		          UPLOAD_DIR="$GIT_BRANCH/$BUILD_ID"
		          BUILD_URL="$DOWNLOAD_AREA_URL/$UPLOAD_DIR"
		          
		          ssh $sshHost rm -rf $deployDir/$GIT_BRANCH/$LATEST_DIR
                  ssh $sshHost mkdir -p $deployDir/$GIT_BRANCH/$LATEST_DIR
    			  cp $OUTPUT_DIR/$REPO_NAME-*.zip $OUTPUT_DIR/$REPO_NAME.zip
    			  scp $OUTPUT_DIR/$REPO_NAME.zip $sshHost:$deployDir/$GIT_BRANCH/$LATEST_DIR/$REPO_NAME.zip
                   		
                  echo "$BUILD_URL" >> $OUTPUT_DIR/build.info
                  sha256sum $OUTPUT_DIR/$REPO_NAME.zip >> $OUTPUT_DIR/build.info
                  
                  rm $OUTPUT_DIR/$REPO_NAME.zip
                  	  
    			  unzip $OUTPUT_DIR/$REPO_NAME-*.zip -d $OUTPUT_DIR/repository
		      else
    			  UPLOAD_DIR="pr/$CHANGE_ID/$BUILD_ID"
    			  BUILD_URL="$DOWNLOAD_AREA_URL/$UPLOAD_DIR"
                  
                  ssh $sshHost rm -rf $deployDir/$UPLOAD_DIR/$LATEST_DIR
                  ssh $sshHost mkdir -p $deployDir/$UPLOAD_DIR/$LATEST_DIR
                  cp $OUTPUT_DIR/$REPO_NAME-*.zip $OUTPUT_DIR/$REPO_NAME.zip
                  scp $OUTPUT_DIR/$REPO_NAME.zip $sshHost:$deployDir/$UPLOAD_DIR/$LATEST_DIR/$REPO_NAME.zip
                  
                  echo "$BUILD_URL" >> $OUTPUT_DIR/build.info
                  sha256sum $OUTPUT_DIR/$REPO_NAME.zip >> $OUTPUT_DIR//build.info
                  
                  rm $OUTPUT_DIR/$REPO_NAME.zip      
                      
                  unzip $OUTPUT_DIR/$REPO_NAME-*.zip -d $OUTPUT_DIR/repository
		      fi
 		      
		      ssh $sshHost rm -rf $deployDir/${UPLOAD_DIR}
                      ssh $sshHost mkdir -p $deployDir/${UPLOAD_DIR}
                      scp -r $OUTPUT_DIR/* $sshHost:$deployDir/${UPLOAD_DIR}

                  '''
                }
            }
        }       
    }    
}
