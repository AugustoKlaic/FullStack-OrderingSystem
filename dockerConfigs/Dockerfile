FROM mysql:latest

# Environment variables that created db and user
ENV MYSQL_ROOT_PASSWORD secretadmin
ENV MYSQL_USER backendUser
ENV MYSQL_PASSWORD backendpassword
ENV MYSQL_DATABASE database_fullstack

COPY ./database_fullstack_test.sql /docker-entrypoint-initdb.d/database_fullstack_test.sql

#docker build --tag mysql .
#docker run -p 3306:3306 mysql