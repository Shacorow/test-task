@echo off

set db_url=jdbc:postgresql://localhost:5432/task_list_data
set db_user=admin
set db_password=

java -jar test-task-1.0-SNAPSHOT.jar
