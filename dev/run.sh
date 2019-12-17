#!/bin/sh
cd /home

chmod 755 eclipse/eclipse

echo "Downloading eclipse..."

eclipse/eclipse -nosplash -application org.eclipse.equinox.p2.director -repository "https://download.eclipse.org/technology/swtbot/releases/2.8.0/" -installIU "org.eclipse.swtbot.eclipse.gef.feature.group, org.eclipse.swtbot.generator.feature.feature.group, org.eclipse.swtbot.ide.feature.group, org.eclipse.swtbot.eclipse.feature.group, org.eclipse.swtbot.forms.feature.group, org.eclipse.swtbot.feature.group, org.eclipse.swtbot.eclipse.test.junit.feature.group"

echo "Unzipping features..."

cd /development/ant_build/artifacts/

unzip codewind-openapi-eclipse-0*.zip -d code

unzip codewind-openapi-eclipse-test*.zip -d test

cd /home

echo "Installing codewind and codewind test..."

eclipse/eclipse -nosplash -application org.eclipse.equinox.p2.director -repository "file:/development/ant_build/artifacts/code" -installIU "org.eclipse.codewind.openapi.feature.feature.group"
eclipse/eclipse -nosplash -application org.eclipse.equinox.p2.director -repository "file:/development/ant_build/artifacts/test" -installIU "org.eclipse.codewind.openapi.feature.test.feature.group"

echo "Run junit tests"

xvfb-run ./runTest.sh

return_code=$?

echo "Copying test result to /development/codewind-openapi-junit-results.xml"
cp /tmp/results.xml /development/codewind-openapi-junit-results.xml

echo "Test finished with return code $return_code"
return $return_code


