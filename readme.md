# serverless-test
A small study project on the serverless-framework.

## Resources
- [The Serverless Releases](https://github.com/serverless/serverless/releases)
- [The Serverless Changelog](https://github.com/serverless/serverless/blob/master/CHANGELOG.md)
- [The Serverless Github Page](https://github.com/serverless/serverless)

## Installing Serverless
First install [brew](https://brew.sh/), then install npm `brew install npm` and then install serverless using npm: `npm install -g serverless`.

## Installing examples
A Serverless projects consists of a `serverless.yml` that will be converted to a Cloudformation template and is seen as a deployment unit and that deployment unit is called a `service`. The service is made up of lambdas and backend service eg. from AWS. These services can be put on github and can be downloaded to your local directory using the command `serverless install --url <service-github-url> --name <place-a-name-here`. 

For example `serverless install --url https://github.com/pmuens/serverless-crud --name my-crud` will download the .zip file of the serverless-crud service from GitHub, create a new directory with the name my-crud in the current working directory and unzips the files in this directory and renames the service to my-crud if serverless.yml exists in the service root.