pipeline {
    agent {
        docker {
            image 'maven:3.9.9-eclipse-temurin-17'
            // Persistent volume for maven dependencies
            args '-v maven-repo:/root/.m2'
        }
    }

    parameters {
        choice(
            name: 'TEST_ENV',
            choices: ['dev', 'qa', 'prod'],
            description: 'Select environment to run Rest Assured tests'
        )
        string(
            name: 'TAGS',
            defaultValue: '@smoke',
            description: 'Enter Cucumber tags to execute (e.g., @smoke, @regression)'
        )
    }

    environment {
        // Update these with your current project details
        GIT_URL = 'https://github.com/Poulomi0789/RestAssuredEnterprise.git'
        GIT_BRANCH = 'main'
        EMAIL_RECIPIENTS = 'poulomidas89@gmail.com'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timeout(time: 60, unit: 'MINUTES')
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: "${GIT_BRANCH}", url: "${GIT_URL}"
            }
        }

        stage('Run Rest Assured Tests') {
            steps {
                retry(2) {
                    // -Dcucumber.filter.tags passes the tag from Jenkins UI to Cucumber
                    // -Dmaven.test.failure.ignore=true ensures we reach the report generation stage
                    sh """
                    mvn clean test \
                    -Denv=${params.TEST_ENV} \
                    -Dcucumber.filter.tags="${params.TAGS}" \
                    -Dmaven.test.failure.ignore=true
                    """
                }
            }
        }

        stage('Generate & Zip Report') {
            steps {
                // Generates the static HTML site in target/site/allure-maven-plugin
                sh 'mvn io.qameta.allure:allure-maven:report'

                script {
                    if (fileExists('target/site/allure-maven-plugin')) {
                        zip zipFile: 'allure-report.zip',
                            dir: 'target/site/allure-maven-plugin',
                            archive: true
                    } else {
                        echo "Warning: Allure report directory not found, skipping zip."
                    }
                }
            }
        }

        stage('Publish to Jenkins') {
            steps {
                // Tells Jenkins Allure plugin where to find the raw results
                allure includeProperties: false, results: [
                    [path: 'target/allure-results']
                ]
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/*.log', allowEmptyArchive: true
            junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
        }

        success {
            emailext(
                subject: "✅ API Tests Passed | ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """<h2>Build Successful 🚀</h2>
                         <b>Environment:</b> ${params.TEST_ENV} <br>
                         <b>Tags Run:</b> ${params.TAGS} <br>
                         <b>Allure Report:</b> <a href="${env.BUILD_URL}allure">View Online</a>""",
                attachmentsPattern: 'allure-report.zip',
                mimeType: 'text/html',
                to: "${EMAIL_RECIPIENTS}"
            )
        }

        unstable {
            emailext(
                subject: "⚠️ API Tests Unstable | ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """<h2>Tests Failed ⚠️</h2>
                         <b>Environment:</b> ${params.TEST_ENV} <br>
                         <b>Check Allure for details:</b> <a href="${env.BUILD_URL}allure">View Report</a>""",
                attachmentsPattern: 'allure-report.zip',
                mimeType: 'text/html',
                to: "${EMAIL_RECIPIENTS}"
            )
        }

        failure {
            emailext(
                subject: "❌ API Build Failed | ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """<h2>Pipeline Error ❌</h2>
                         <b>The build crashed before finishing.</b><br>
                         <a href="${env.BUILD_URL}console">Console Output</a>""",
                to: "${EMAIL_RECIPIENTS}"
            )
        }
    }
}