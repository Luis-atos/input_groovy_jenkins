import com.cwctravel.hudson.plugins.extended_choice_parameter.ExtendedChoiceParameterDefinition
// url de iconos y colores --> https://wilsonmar.github.io/jenkins2-pipeline/
node {
    stage(' Clone sources') {
         git url: 'git@git.servdev.mdef.es:lmunma1/experimento-impoexpo.git'
         sleep(10)
         echo "\u001B Salida correcta **** \u2705"
         sleep(4)
        def componentList = []

def componentMapEntry1 = [:]
componentMapEntry1['componentName']="Dashboard_Core"
componentList << componentMapEntry1

def componentMapEntry2 = [:]
componentMapEntry2['componentName']="Dashboard_Equities"
componentList << componentMapEntry2

def cme3 = [:]
cme3["componentName"] = "home"
componentList << cme3

def userInput = input(
                    id: 'directory', message: 'input parameters', parameters: [
                        [
                            $class: 'ChoiceParameterDefinition',
                            name: 'builddir',
                            choices: componentList,
                            description: 'selection for all DIRECTORY to BUILD',
                        ]
                    ])
                    
MyUSERNAME = input message: 'Please enter the username', parameters: [string(defaultValue: '', description: '', name: 'Username')]
MyPASSWORD = input message: 'Please enter the password', parameters: [password(defaultValue: '', description: '', name: 'Password')]
        
echo "Username: ${MyUSERNAME}"
echo "Password: ${MyPASSWORD}"
    }

     stage('a master') {
        sh "git branch -a"
        sh "git checkout master"
       // propsApp = readJSON file: 'source/pom.xml'
        propsApp = readMavenPom file: "source/pom.xml"
        Version_master = propsApp['version']
        echo "\u001B[31m ---- ${Version_master}  ------ "
     }
        stage("foo") {
           
                script {
                    env.RELEASE_SCOPE = input message: 'User input required', ok: 'Release!',
                            parameters: [choice(name: 'RELEASE_SCOPE', choices: 'patch\nminor\nmajor', description: 'What is the release scope?')]
                }
                echo "${env.RELEASE_SCOPE}"
        }
    stage ('a Develop'){
              sh "git branch -a"
              sh "git checkout develop"
        propsApp = readMavenPom file: "source/pom.xml"
        Version_develop = propsApp['version']
        echo "---- ${Version_develop}  ------ "
    }
        
    
    stage('versiones'){
         def (val1,val2,val3)= Version_develop.tokenize('\\.')
         def INPUT_PARAMS = input(
                            id: 'env.VERSION', 
                            message: 'Versión a desplegar', 
                            parameters: [
                             [$class: 'StringParameterDefinition', defaultValue: "${val1}", description: 'Version Major', name: 'MAJOR'],
                             [$class: 'StringParameterDefinition', defaultValue: "${val2}", description: 'Version Minor', name: 'MINOR'],
                             [$class: 'StringParameterDefinition', defaultValue: "${val3}", description: 'Version parcheo de bugfix', name: 'PATCH']
                             ]);
                                            
                                            MAJOR = INPUT_PARAMS.MAJOR;
                                            MINOR = INPUT_PARAMS.MINOR;
                                            PATCH = INPUT_PARAMS.PATCH;

    }
    stage('opcion dos'){
        def (val1,val2,val3)= Version_develop.tokenize('\\.')
        def userInput = input(
                 id: 'userInput', message: 'Esta tarea no despliega en PRODUCCIÓN\n\n Si no existen errores y acepta continuar', 
                 parameters: [
                  [$class: 'TextParameterDefinition', defaultValue: "${val1}", description: 'Version Major', name: 'MAJOR'],
                  [$class: 'TextParameterDefinition', defaultValue: "${val2}", description: 'Version Minor', name: 'MINOR'],
                  [$class: 'TextParameterDefinition', defaultValue: "${val3}", description: 'Version parcheo de bugfix', name: 'PATCH']
                ]) 
                echo " **** ${userInput} ****"

    }
    stage('multi'){
        def multiSelect= new ExtendedChoiceParameterDefinition("name", "PT_MULTI_SELECT", "blue,green,yellow,blue", "project name",
            "", 
            "",
            "", 
            "", 
            "", 
            "", 
            "", 
            "", 
            "", 
            "", 
            "", 
            "", 
            "", 
            "blue,green,yellow,blue", 
            "", 
            "", 
            "", 
            "", 
            "", 
            "", 
            "", 
            "", 
            false,
            false, 
            3, 
            "multiselect", 
            ",") 

   def userInput = input  id: 'customID', message: 'Let\'s promote?', ok: 'Release!', parameters:  [multiSelect]


echo "Hello: "+ userInput
    }
    stage('multi22'){
      parameters {
       extendedChoice( 
        defaultValue: 'One,Two,Three,Four', 
        description: '', 
        multiSelectDelimiter: ',', 
        name: 'SAMPLE_EXTENDED_CHOICE', 
        quoteValue: false, 
        saveJSONParameterToFile: false, 
        type: 'PT_CHECKBOX', 
        value:'One,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten', 
        visibleItemCount: 10)
     }
    }
    
    stage('table'){   --> https://deephaven.io/core/groovy/docs/how-to-guides/new-table/
        result = newTable(
           stringCol("NameOfStringCol", "Data String 1", 'Data String 2', "Data String 3"),
           intCol("NameOfIntCol", 4, 5, 6)
        )
    }
}
