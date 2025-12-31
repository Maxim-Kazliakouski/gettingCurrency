pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M2"
        // The naming should be coincide with naming from Jenkins/Settings/Tools
        gradle "Gradle_Jenkins"
    }

    parameters {
        gitParameter ( branch: '',
        branchFilter: 'origin/(.*)',
        defaultValue: 'master',
        description: '',
        name: 'BRANCH',
        quickFilterEnabled: true,
        selectedValue: 'NONE',
        sortMode: 'NONE',
        tagFilter: '*',
        type: 'PT_BRANCH' )
    }

    environment {
        LAUNCH_TYPE = "${params.LAUNCH_TYPE ?: 'TEST'}"  // Added default value
    }

    stages {
        stage('Performing tests...') {
            steps {
                script {
                    try {
                        // Get code from GitHub repository
                        git branch: "${params.BRANCH}", url: 'https://github.com/Maxim-Kazliakouski/gettingCurrency.git'

                        // Make gradlew executable
                        sh 'chmod +x ./gradlew'

                        // For Unix/macOS agents
                        sh '''
                            echo "PATH: $PATH"
                            gradle --version
                            ./gradlew --version || echo "Gradle wrapper not found"
                        '''

                        // Run tests with gradlew (wrapper)
                        sh 'gradle clean test --quiet -DlaunchType=$LAUNCH_TYPE'

                        // If gradlew doesn't work, try with system gradle
                        // sh 'gradle clean test --quiet -DlaunchType=$LAUNCH_TYPE'

                    } catch (Exception error) {
                        unstable('Testing failed...')
                    }
                }
            }
        }

        stage('Sending message via Telegram...') {
            steps {
                script {
                echo "Checking credentials access..."
                        withCredentials([string(credentialsId: 'chatID', variable: 'TEST_VAR')]) {
                            echo "Found chatID = [${TEST_VAR.take(4)}...]" // Покажет первые 4 символа
                        }
                    try {
                        withCredentials ([
                            string(credentialsId: 'chatID', variable: 'CHAT_ID'),
                            string(credentialsId: 'telegramBotToken', variable: 'BOT_TOKEN')
                        ]) {
                            // Read currency files
                            def fileContentsUSD = readFile('currency_USD.txt').trim()
                            def fileContentsRUB = readFile('currency_RUB.txt').trim()
                            def fileContentsEUR = readFile('currency_EUR.txt').trim()
                            def lines = '-' * 30

                            // Prepare message - different formats to try
                            def message1 = "${fileContentsEUR}\n${lines}\n${fileContentsUSD}\n${fileContentsRUB}"
                            def message2 = "${fileContentsEUR}\\n${lines}\\n${fileContentsUSD}\\n${fileContentsRUB}"

                            // Try different approaches for curl
                            echo "Sending Telegram message..."

//                             // Approach 1: Using heredoc (most reliable)
//                             sh '''
//                                 cat << EOF > /tmp/telegram_message.txt
//                                 ${fileContentsEUR}
//                                 ${lines}
//                                 ${fileContentsUSD}
//                                 ${fileContentsRUB}
//                                 EOF
//
//                                 MESSAGE_TEXT=$(cat /tmp/telegram_message.txt | jq -sRr @uri)
//                                 curl -s -X POST "https://api.telegram.org/bot$BOT_TOKEN/sendMessage" \
//                                      -d "chat_id=$CHAT_ID" \
//                                      -d "text=$MESSAGE_TEXT" \
//                                      -d "parse_mode=HTML"
//                             '''

                            // Alternative approach 2: Direct command
                            sh """
                                curl -s -X POST "https://api.telegram.org/bot$BOT_TOKEN/sendMessage" \\
                                     -d chat_id="$CHAT_ID" \\
                                     -d text="${fileContentsEUR}%0A${lines}%0A${fileContentsUSD}%0A${fileContentsRUB}" \\
                                     -d parse_mode="HTML"
                            """

                            // Alternative approach 3: Using environment variables
                            // sh '''
                            //     curl -s -X POST "https://api.telegram.org/bot$BOT_TOKEN/sendMessage" \
                            //          -d chat_id="$CHAT_ID" \
                            //          -d text="$TELEGRAM_MESSAGE" \
                            //          -d parse_mode="HTML"
                            // '''
                        }
                    } catch (Exception error) {
                        echo "Failed to send Telegram message: ${error}"
                        unstable('Telegram notification failed')
                    }
                }
            }
        }

        stage('Archive results') {
            steps {
                // Archive test results if they exist
                sh 'find . -name "*.xml" -type f | head -5'

                // Archive HTML reports if generated
                sh 'ls -la build/reports/tests/test/ 2>/dev/null || echo "No test reports found"'

                // Optional: Archive artifacts
                // archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
                // junit '**/build/test-results/**/*.xml'
            }
        }
    }

    post {
        always {
            // Cleanup or final notifications
            echo "Build ${currentBuild.result ?: 'SUCCESS'} - ${env.JOB_NAME} #${env.BUILD_NUMBER}"

            // Clean workspace (optional)
            // cleanWs()
        }
        success {
            echo "The pipeline completed successfully!"
        }
        failure {
            echo "The pipeline failed."
        }
        unstable {
            echo "The pipeline is unstable."
        }
    }
}