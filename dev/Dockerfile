FROM sgrio/java:jre_8_ubuntu 
WORKDIR /home
RUN apt-get update
RUN apt-get install -y net-tools
RUN apt-get install -y telnet
RUN apt-get install -y wget xvfb unzip libgtk-3-dev
# Set locale
RUN apt-get clean && apt-get update && apt-get install -y locales
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
RUN wget http://www.eclipse.org/external/technology/epp/downloads/release/2019-09/R/eclipse-jee-2019-09-R-linux-gtk-x86_64.tar.gz
RUN tar xzf eclipse-jee-2019-09-R-linux-gtk-x86_64.tar.gz
ADD run.sh /home
ADD runTest.sh /home
RUN chmod 755 /home/run.sh
RUN chmod 755 /home/runTest.sh

CMD ./run.sh
