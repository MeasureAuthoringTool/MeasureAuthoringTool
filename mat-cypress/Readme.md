## Setup

https://docs.cypress.io/guides/getting-started/installing-cypress.html#System-requirements
run npm install

to run the cypress command that is included with this project use the npx command

---

## Secrets

Set your secrets in a shell files _E.G. local_env.sh dev.sh

```
export CYPRESS_MAT_USERNAME="your_mat_user_name"

export CYPRESS_MAT_PASSWORD="your_mat_password"

export CYPRESS_UMLS_API_KEY="your_vsac_key"
```

Make sure these values are set E.G. by sourcing the file **(make sure you don't push the secrets to GIT).**, 
Or a user could maintain the state of the environment vars in a shell rc file

---

## Config 

In the config directory is where the configuration params are set up (non secrets).

to run specify the command line paramater as shown below

```
npx cypress open --env configFile=dev
```

if no command param is set the system will load the local.json as default.




