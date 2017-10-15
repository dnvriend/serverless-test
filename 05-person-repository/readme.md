# 05-person-repository
A serverless person repository

## How to deploy
- Create table (todo: use serverless.yml for this)
- install npm, typescript, serverless-framework, serverless-webpack (read below)
- run: serverless deploy

## DynamoDB table
Replace the dynamodb table ARN in serverless.yml with your own table ARN!

Schema:
- id: S -> HASH key
- name: S
- age: S

## Tools
- npm: Nodejs Package Manager
  - home: https://www.npmjs.com/
  - install: brew install npm
  - help:
    - quick help on command: $ npm <command> -h
    - quick help on term: $ npm help <term>
    - full usage info: $ npm -l
    - involved overview: $ npm help npm
  - dirs:
    - global: /usr/local/lib/npm
    - local: ./node_modules
  - config:
    - help: https://docs.npmjs.com/getting-started/using-a-package.json
    - file: package.json
    - create an init package.json: npm init
- typescript:
  - home: https://www.typescriptlang.org/
  - install: npm install -g typescript
  - help:
    - compile: tsc helloworld.tsc
    - doc: https://www.typescriptlang.org/docs/home.html
  - config:
    - help: https://www.typescriptlang.org/docs/handbook/tsconfig-json.html
    - file: tsconfig.json
- serverless framework:
  - home: https://serverless.com/framework/
  - install: npm install serverless -g
  - help:
    - aws provider help: https://serverless.com/framework/docs/providers/aws/guide/intro/
    - workflow: https://serverless.com/framework/docs/providers/aws/guide/workflow/
  - config:
    - help: https://serverless.com/framework/docs/providers/aws/guide/serverless.yml/
    - file: serverless.yml
- serverless-webpack
  - home: https://github.com/serverless-heaven/serverless-webpack
  - install: npm install serverless-webpack --save-dev
  - config:
    - file: webpack.config.js
- webpack
  - home: https://webpack.github.io/
  - install: npm i webpack -g
  - config:
    - help: https://webpack.js.org/concepts/configuration/
    - file: webpack.config.js
- dynamodb:
  - dev-guide:
    - js-api: http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.NodeJs.html
  - dynasty:
    - description: Dynasty is a clean and simple Amazon DynamoDB client for Node with baked in Promise support.
    - home: http://dynastyjs.com/index.html
- space-lift:
  - home: https://github.com/AlexGalays/spacelift
  - install: npm install space-lift
- validator.ts:
  - home: https://github.com/pleerock/class-validator
  - install: npm install validator.ts --save
- yaml-lint:
  - home: https://github.com/rasshofer/yaml-lint
  - install: npm install -g yaml-lint
  - usage: yamllint serverless.yaml
- aws:
  - [Systems Manager Parameter Store (SSM)](http://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-paramstore.html)



## Resources
- http://definitelytyped.org/
- https://www.npmjs.com/package/@types/aws-lambda
- https://www.npmjs.com/package/@types/aws-sdk
- https://github.com/aws/aws-sdk-js
- https://github.com/serverless-heaven/serverless-webpack
- https://webpack.github.io/docs/configuration.html#externals
- https://github.com/asprouse/serverless-webpack-plugin
- https://webpack.js.org/guides/caching/
- https://vsavkin.com/functional-typescript-316f0e003dc6
- https://www.sitepen.com/blog/2013/12/31/typescript-cheat-sheet/

Note: You don't need this lib "@types/aws-sdk": "2.7.0` anymore as `"aws-sdk": "^2.133.0",` comes with its own lib, you have to put it in `devDependencies` though.