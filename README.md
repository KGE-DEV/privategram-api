# PrivateGram API
- This is the api that will handle user and post endpoints.

## Build Instructions
- Set main class to be com.garrettestrin.PrivateGram.app.Main
- Set env variable of PROPERTIES_FILENAME equal to local.properties (you will need to add this file. Ask me for it).
    -- The rest of the env variables will be defined here.
- If you want to run a local mySQL db, you will need to set it up. Migration files exist in /resources/migrations.
- I would suggest setting tests to run before building, so that the build doesn't even start if tests do not pass.


## Eclipse Maven Instructions:

#### Do First:
- Make sure you have this repo cloned: https://github.com/KGE-DEV/privategram-api
- Make sure that your current workspace is not the same directory that privategram-api is in. If you need to change it File -> Change Workspace
- Need to have a MySQL instance running somewhere with the correct schema. Will update program soon to run migrations if necessary.

#### Install or update Maven plugin:
- Open Help -> Install new software
- Paste this url into “work with” text box: http://download.eclipse.org/technology/m2e/releases
- Check “Maven Integration with Eclipse”
- Click next and follow instructions to either install or update.
#### Import privategram-api as a maven project
- Click File > Import
- Type Maven in the search box under Select an import source:
- Select Existing Maven Projects
- Click Next
- Click Browse and select the folder that is the root of the Maven project (probably contains the pom.xml file)
- Click Next or finish
#### Create Run Configuration
- Run -> Run Configurations
- Double click java application
- Under “Project” click browse and select PrivateGram-api
- Under “Main Class” click search. In the search box, type “Main” and select “Main - com.garrettestrin.PrivateGram.app”
- Change Tab at top to Environment and add this env variable by clicking new:
- Name: PROPERTIES_FILENAME
- Value: local.properties
- Click Run
