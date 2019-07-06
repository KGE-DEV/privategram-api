# PrivateGram API
- This is the api that will handle user and post endpoints.

## Build Instructions
- Set main class to be com.garrettestrin.PrivateGram.app.Main
- Set env variable of PROPERTIES_FILENAME equal to local.properties (you will need to add this file).
    -- The rest of the env variables will be defined here (eventually).
- Create an app.yml file that will be used for configuration.  -- This file is not committed yet because it contains passwords and connection strings that will eventually be moved to the .properties files and replaced with placeholder(once I figure that out). 
- I would suggest setting tests to run before building, so that the build doesn't even start if tests do not pass.