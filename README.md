# notifier-webapp

Notifier Web App GitHub Repository Setup

## Run in Docker

1. Build image

    ```bash
    docker build -t notifier-webapp:latest .
    ```

2. Run application

    ```bash
    docker run \
    -e MYSQL_DB_HOST=xinyu-main-1.cvefazkbfyp3.us-east-1.rds.amazonaws.com \
    -e MYSQL_DB_PORT=3306 \
    -e MYSQL_DB_NAME=csye7125 \
    -e MYSQL_DB_USERNAME=root \
    -e MYSQL_DB_PASSWORD=thepassword \
    -e NOTIFIER_MYSQL_DB_HOST=xinyu-notifier-1.cvefazkbfyp3.us-east-1.rds.amazonaws.com \
    -e NOTIFIER_MYSQL_DB_PORT=3306 \
    -e NOTIFIER_MYSQL_DB_NAME=notifier \
    -e NOTIFIER_MYSQL_DB_USERNAME=root \
    -e NOTIFIER_MYSQL_DB_PASSWORD=thepassword \
    -e ELASTICSEARCH_HOST=elasticsearch-master.default \
    -e ELASTICSEARCH_PORT=9200 \
    -e AWS_ACCESS_KEY_ID=XXXXXXXX \
    -e AWS_SECRET_KEY=XXXXXXXX \
    -e NOTIFIER_SCAN_PERIOD= 10000 \
    -p 8080:8080 \
    notifier-webapp
    ```

## User Stories

1. The notifier application is a separate microservice.

2. The notifier application is not exposed to external users. It is an internal service.

3. The notifier application will periodically (every 5 mins, configurable) look at active alerts set by users and query data from Elasticsearch to see if any stories match user's criteria.

    1. Make this scale out. Design and implementation details are to be decided by students.

4. The notifier application will send email to the user when a story matches their search criteria.

5. The application should NOT alert on stories already alerted i.e. do not send emails with same stories. The application will have to track email send, stories user is notified on, and email sent.

6. The application must have it's own RDS instance to store its stateful data.

7. Use AWS SES to send email. AWS creds should be provided to the application using Kubernetes Secret.

    1. You may configure SES in your ROOT AWS account and use in DEV and PROD accounts.