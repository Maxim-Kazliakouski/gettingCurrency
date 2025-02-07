pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M2"
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

    stages {

        stage('Performing tests...') {

            steps {

                script {

                    try {

                        // Get some code from a GitHub repository
                        git branch: "${params.BRANCH}",  url: 'https://github.com/Maxim-Kazliakouski/gettingCurrency.git'
                            // Run Maven on a Unix agent.
//                             bat "gradle -v"
                            bat "gradle clean -DlaunchType=$LAUNCH_TYPE test"
                    } catch (Exception error)
                    {
                        unstable('Testing failed...')
                    }
                }
            }
        }

        stage('Sending message via Telegram...') {
            steps {
                     script {
                                try {
                                /*
                                withCredentials need to use exactly in step, where you wnat to
                                use variables from secure Jenkins
                                */
                                     withCredentials ([
                                        string(credentialsId: 'chatID',
                                        variable: 'CHAT_ID'),
                                        string(credentialsId: 'telegramBotToken',
                                        variable: 'BOT_TOKEN')
                                     ])
                                          {
                                          // Run Maven on a Unix agent.
                                          def fileContentsUSD = readFile('currency_USD.txt')
                                          def fileContentsRUB = readFile('currency_RUB.txt')
                                          def lines = '-' * 35
                                          def spaces = ' ' * 80
                                          def fileContents = '"' + readFile('currency_USD.txt') + spaces + readFile('currency_RUB.txt') + spaces + lines + '"'
                                          //echo "REQUEST: curl -s -X POST https://api.telegram.org/bot$BOT_TOKEN/sendMessage -d chat_id=$CHAT_ID -d text=${fileContentsUSD}      ${fileContentsRUB}"
                                          bat "curl -s -X POST https://api.telegram.org/bot$BOT_TOKEN/sendMessage -d chat_id=$CHAT_ID -d text=${fileContents}"
                                          }
                                }
                                catch (Exception error) {
                                unstable('Testing failed')
                                }
                     }
               //emailext to: "maxim.kazliakouski@gmail.com",
               //!!!subject: "Jenkins build === ${currentBuild.currentResult} === ${env.JOB_NAME}",
               //subject: "${env.JOB_NAME}",
                //!!body: "${currentBuild.currentResult}: Job ${env.JOB_NAME}\nMore Info about build can be found here: ${env.BUILD_URL}.\nNEW FORECAST --> ${env.FORECAST}.\nNEW CURRENCY COURSE --> ${env.currencyText}"
               //body: "${env.FILE_CONTENTS}"
            }
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                //success {
                //    junit '**/target/surefire-reports/TEST-*.xml'
                //}
        }
    }
}