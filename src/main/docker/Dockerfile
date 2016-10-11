FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD expensetracker-cb-0.1.0.jar app.jar
RUN sh -c 'touch /app.jar'
ADD test_hello.sh .
RUN chmod +x test_hello.sh
CMD sh test_hello.sh


