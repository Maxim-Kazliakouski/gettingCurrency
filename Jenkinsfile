pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M2"
    }
//     triggers {
//         cron('0 8 * * *')
//     }
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

                        //withCredentials ([
                        //    string(credentialsId: 'qase_token',
                        //variable: 'TOKEN_CREDENTIALS'),
                        //    string(
                        //        credentialsId: 'qase_password',
                        //        variable: 'PASSWORD_CREDENTIALS')
                        //])


                            // Run Maven on a Unix agent.
                            bat "gradle clean -DlaunchType=$LAUNCH_TYPE test"
                    } catch (Exception error)
                    {
                        unstable('Testing failed')
                    }
                }
            }
        }

            // To run Maven on a Windows agent, use
            // bat "mvn -Dmaven.test.failure.ignore=true clean package"

        //stage('Reading file...') {
        //    steps {
        //        script {
        //            def fileContents = readFile('currency.txt')
        //                echo "Содержимое файла: ${fileContents}"
        //       }
        //   }
        //}

        stage('Sending email...') {
            steps {
                     script {
                     //bat "FILE=allure-notifications-4.2.1.jar
                     //     if [ ! -f "$FILE" ]; then
                     //        wget https://github.com/qa-guru/allure-notifications/releases/download/4.2.1/allure-notifications-4.2.1.jar
                     //     fi"
                     //bat 'java "-DconfigFile=notifications/config.json" -jar ../allure-notifications-4.6.1.jar'
                          //def fileContents = readFile('currency.txt')
//                           def fileContentsUSD = '"' + readFile('currency_USD.txt') + '"'
//                           def fileContentsRUB = '"' + readFile('currency_RUB.txt') + '"'
                          def fileContentsUSD = readFile('currency_USD.txt')
                          def fileContentsRUB = readFile('currency_RUB.txt')
                          //def fileContents = '"' + readFile('currency_USD.txt') + "                                                                               " + readFile('currency_RUB.txt') + '"'
                          def lines = '-' * 35
                          def spaces = ' ' * 80
                          def fileContents = '"' + readFile('currency_USD.txt') + spaces + readFile('currency_RUB.txt') + spaces + lines + '"'
                          //echo "File content: ${fileContents}"
                          //String text = fileContents
                          //env.FILE_CONTENTS = fileContents
//                           echo "REQUEST: curl -s -X POST https://api.telegram.org/bot6719433369:AAHn17_HLVBk23lvh42QkUBqvRh3ZEAGaDs/sendMessage -d chat_id=968002806 -d text=${fileContentsUSD}      ${fileContentsRUB}"
                          bat "curl -s -X POST https://api.telegram.org/bot6719433369:AAHn17_HLVBk23lvh42QkUBqvRh3ZEAGaDs/sendMessage -d chat_id=968002806 -d text=${fileContents}"

//                           bat "curl -s -X POST https://api.telegram.org/bot6719433369:AAHn17_HLVBk23lvh42QkUBqvRh3ZEAGaDs/sendMessage -d chat_id=968002806 -d text=${fileContentsUSD}"
//                           bat "curl -s -X POST https://api.telegram.org/bot6719433369:AAHn17_HLVBk23lvh42QkUBqvRh3ZEAGaDs/sendMessage -d chat_id=968002806 -d text=${fileContentsRUB}"


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