pipeline {
    agent any

    parameters {
        string(
            name: 'TARGET_DATE',
            defaultValue: '',
            description: '정산 대상 날짜 (yyyy-MM-dd). 미입력 시 어제 날짜로 실행'
        )
    }

    stages {
        stage('Build') {
            steps {
                dir('/home/project') {
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Run Batch') {
            steps {
                dir('/home/project') {
                    script {
                        def targetDate = params.TARGET_DATE ?: sh(
                            script: "date -d 'yesterday' '+%Y-%m-%d'",
                            returnStdout: true
                        ).trim()

                        echo "정산 대상 날짜: ${targetDate}"

                        sh """
                            java -jar build/libs/batch-system-0.0.1-SNAPSHOT.jar \
                                targetDate=${targetDate} \
                                time=${System.currentTimeMillis()}
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "배치 성공: ${currentBuild.displayName}"
        }
        failure {
            echo "배치 실패: ${currentBuild.displayName}"
        }
    }
}
