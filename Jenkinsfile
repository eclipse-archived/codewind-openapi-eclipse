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
                    export REPO_NAME="codewind-openapi-eclipse"
                    export OUTPUT_DIR="$WORKSPACE/dev/ant_build/artifacts"
                    export DOWNLOAD_AREA_URL="https://download.eclipse.org/codewind/$REPO_NAME"
                    export LATEST_DIR="latest"
                  
                      export sshHost="genie.codewind@projects-storage.eclipse.org"
                      export deployDir="/home/data/httpd/download.eclipse.org/codewind/$REPO_NAME"

                      if [ -z $CHANGE_ID ]; then
		          UPLOAD_DIR="$GIT_BRANCH/$BUILD_ID"
		          BUILD_URL="$DOWNLOAD_AREA_URL/$UPLOAD_DIR"
		          
		          ssh $sshHost rm -rf $deployDir/$GIT_BRANCH/$LATEST_DIR
                  ssh $sshHost mkdir -p $deployDir/$GIT_BRANCH/$LATEST_DIR
    			  cp $OUTPUT_DIR/$REPO_NAME-*.zip $OUTPUT_DIR/$REPO_NAME.zip
    			  scp $OUTPUT_DIR/$REPO_NAME.zip $sshHost:$deployDir/$GIT_BRANCH/$LATEST_DIR/$REPO_NAME.zip
                   		
                  echo "# Build Url :" >> $OUTPUT_DIR/build.info
                  echo "$BUILD_URL" >> $OUTPUT_DIR/build.info
                  echo "" >> $OUTPUT_DIR/build.info
                  echo "# SHA-1 :" >> $OUTPUT_DIR/build.info
                  sha1sum $OUTPUT_DIR/$REPO_NAME.zip >> $OUTPUT_DIR/build.info
                  
                  rm $OUTPUT_DIR/$REPO_NAME.zip
                  	  
    			  unzip $OUTPUT_DIR/$REPO_NAME-*.zip -d $OUTPUT_DIR/repository
    			  
    			  scp -r $OUTPUT_DIR/* $sshHost:$deployDir/$GIT_BRANCH/$LATEST_DIR   
		      else
    			  UPLOAD_DIR="pr/$CHANGE_ID/$BUILD_ID"
    			  BUILD_URL="$DOWNLOAD_AREA_URL/$UPLOAD_DIR"
                  
                  ssh $sshHost rm -rf $deployDir/pr/$CHANGE_ID/$LATEST_DIR
                  ssh $sshHost mkdir -p $deployDir/pr/$CHANGE_ID/$LATEST_DIR
                  cp $OUTPUT_DIR/$REPO_NAME-*.zip $OUTPUT_DIR/$REPO_NAME.zip
                  scp $OUTPUT_DIR/$REPO_NAME.zip $sshHost:$deployDir/pr/$CHANGE_ID/$LATEST_DIR/$REPO_NAME.zip
                  
                  echo "# Build Url :" >> $OUTPUT_DIR/build.info
                  echo "$BUILD_URL" >> $OUTPUT_DIR/build.info
                  echo "" >> $OUTPUT_DIR/build.info
                  
                  echo "# SHA-1 :" >> $OUTPUT_DIR/build.info
                  sha1sum $OUTPUT_DIR/$REPO_NAME.zip >> $OUTPUT_DIR/build.info
                  
                  rm $OUTPUT_DIR/$REPO_NAME.zip      
                  unzip $OUTPUT_DIR/$REPO_NAME-*.zip -d $OUTPUT_DIR/repository
                  scp -r $OUTPUT_DIR/* $sshHost:$deployDir/pr/$CHANGE_ID/$LATEST_DIR   
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
