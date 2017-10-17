import {DynamoDB} from "aws-sdk";
import {APIGatewayEvent, Callback, Context} from "aws-lambda";
import {Person, PersonRepository} from "./PersonRepository";
import lift, {Option} from 'space-lift'

// node.js, which is available everywhere defines some vars like
// process, global and console.

let tableName = `${process.env.SLS_STAGE}-persons-table`;
let docClient = new DynamoDB.DocumentClient();
let repo = new PersonRepository(tableName, docClient);

// {
//     "resource": "/person",
//     "path": "/person",
//     "httpMethod": "GET",
//     "headers": {
//         "Accept": "*/*",
//         "Accept-Encoding": "gzip, deflate",
//         "CloudFront-Forwarded-Proto": "https",
//         "CloudFront-Is-Desktop-Viewer": "true",
//         "CloudFront-Is-Mobile-Viewer": "false",
//         "CloudFront-Is-SmartTV-Viewer": "false",
//         "CloudFront-Is-Tablet-Viewer": "false",
//         "CloudFront-Viewer-Country": "NL",
//         "Host": "d2yytbn0e3.execute-api.eu-central-1.amazonaws.com",
//         "User-Agent": "HTTPie/0.9.9",
//         "Via": "1.1 528e50fb19578ca598eb8f9e2157ef09.cloudfront.net (CloudFront)",
//         "X-Amz-Cf-Id": "SWw5J6pC9GoPZ4YxiGa4ZrfNC33qG-xm6u-OQqNtFiTtnt2fysq6HQ==",
//         "X-Amzn-Trace-Id": "Root=1-59e5756e-5f9fe138261c36a25f5d29c0",
//         "X-Forwarded-For": "82.197.193.105, 54.239.167.18",
//         "X-Forwarded-Port": "443",
//         "X-Forwarded-Proto": "https"
//     },
//     "queryStringParameters": null,
//     "pathParameters": null,
//     "stageVariables": null,
//     "requestContext": {
//         "path": "/dev/person",
//         "accountId": "436740350302",
//         "resourceId": "bnm17w",
//         "stage": "dev",
//         "requestId": "325dedb3-b2e9-11e7-bddb-0b92f26c0a9c",
//         "identity": {
//             "cognitoIdentityPoolId": null,
//             "accountId": null,
//             "cognitoIdentityId": null,
//             "caller": null,
//             "apiKey": "",
//             "sourceIp": "82.197.193.105",
//             "accessKey": null,
//             "cognitoAuthenticationType": null,
//             "cognitoAuthenticationProvider": null,
//             "userArn": null,
//             "userAgent": "HTTPie/0.9.9",
//             "user": null
//         },
//         "resourcePath": "/person",
//         "httpMethod": "GET",
//         "apiId": "d2yytbn0e3"
//     },
//     "body": null,
//     "isBase64Encoded": false
// }
export let list = (event: APIGatewayEvent, ctx: Context, cb: Callback) => {
    console.log(`handling event: ${JSON.stringify(event)}`);
    console.log("Getting list of names");
    repo.list().then(xs => {
        cb(null, {statusCode: 200, body: JSON.stringify(xs)})
    }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}));
};

// {
//     "resource": "/person/{id}",
//     "path": "/person/97b4b074-4160-41cd-927d-970c6e11170e",
//     "httpMethod": "GET",
//     "headers": {
//         "Accept": "*/*",
//         "Accept-Encoding": "gzip, deflate",
//         "CloudFront-Forwarded-Proto": "https",
//         "CloudFront-Is-Desktop-Viewer": "true",
//         "CloudFront-Is-Mobile-Viewer": "false",
//         "CloudFront-Is-SmartTV-Viewer": "false",
//         "CloudFront-Is-Tablet-Viewer": "false",
//         "CloudFront-Viewer-Country": "NL",
//         "Host": "d2yytbn0e3.execute-api.eu-central-1.amazonaws.com",
//         "User-Agent": "HTTPie/0.9.9",
//         "Via": "1.1 c76a5a41a8483a9e5dcccdfeb87a16ca.cloudfront.net (CloudFront)",
//         "X-Amz-Cf-Id": "noWvHybJXlT1X1bgDl9W0h_lBm8WwEycrk8If1shfmhnVT80H9pQNA==",
//         "X-Amzn-Trace-Id": "Root=1-59e57481-1cf030a6673445821e07159b",
//         "X-Forwarded-For": "82.197.193.105, 54.239.167.13",
//         "X-Forwarded-Port": "443",
//         "X-Forwarded-Proto": "https"
//     },
//     "queryStringParameters": null,
//     "pathParameters": {"id": "97b4b074-4160-41cd-927d-970c6e11170e"},
//     "stageVariables": null,
//     "requestContext": {
//         "path": "/dev/person/97b4b074-4160-41cd-927d-970c6e11170e",
//         "accountId": "436740350302",
//         "resourceId": "ppv24w",
//         "stage": "dev",
//         "requestId": "a569556b-b2e8-11e7-98a8-23b55e17a7e6",
//         "identity": {
//             "cognitoIdentityPoolId": null,
//             "accountId": null,
//             "cognitoIdentityId": null,
//             "caller": null,
//             "apiKey": "",
//             "sourceIp": "82.197.193.105",
//             "accessKey": null,
//             "cognitoAuthenticationType": null,
//             "cognitoAuthenticationProvider": null,
//             "userArn": null,
//             "userAgent": "HTTPie/0.9.9",
//             "user": null
//         },
//         "resourcePath": "/person/{id}",
//         "httpMethod": "GET",
//         "apiId": "d2yytbn0e3"
//     },
//     "body": null,
//     "isBase64Encoded": false
// }
export let get = (event: any, ctx: Context, cb: Callback) => {
    console.log(`handling event: ${JSON.stringify(event)}`);
    let id = event.pathParameters.id
    console.log(`Getting person for id ${id}`)
    repo.get(id).then(maybePerson => {
        maybePerson
            .map(person => cb(null, {statusCode: 200, body: JSON.stringify(person)}))
            .getOrElse(cb(null, {statusCode: 404, body: `Nothing found for id: ${id}`}))
    }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}));
};

// {
//     "resource": "/person",
//     "path": "/person",
//     "httpMethod": "POST",
//     "headers": {
//         "Accept": "application/json, */*",
//         "Accept-Encoding": "gzip, deflate",
//         "CloudFront-Forwarded-Proto": "https",
//         "CloudFront-Is-Desktop-Viewer": "true",
//         "CloudFront-Is-Mobile-Viewer": "false",
//         "CloudFront-Is-SmartTV-Viewer": "false",
//         "CloudFront-Is-Tablet-Viewer": "false",
//         "CloudFront-Viewer-Country": "NL",
//         "Content-Type": "application/json",
//         "Host": "d2yytbn0e3.execute-api.eu-central-1.amazonaws.com",
//         "User-Agent": "HTTPie/0.9.9",
//         "Via": "1.1 fb7ff691963d3e3600808dccbe4422d2.cloudfront.net (CloudFront)",
//         "X-Amz-Cf-Id": "8HiR6yXcTgm1oBt181CcrOy49zqbPxmgtowW8rUyXjHMWF-MQywTjQ==",
//         "X-Amzn-Trace-Id": "Root=1-59e572a0-3a3ae35d0ea1c1b47e49fd25",
//         "X-Forwarded-For": "82.197.193.105, 54.239.167.18",
//         "X-Forwarded-Port": "443",
//         "X-Forwarded-Proto": "https"
//     },
//     "queryStringParameters": null,
//     "pathParameters": null,
//     "stageVariables": null,
//     "requestContext": {
//         "path": "/dev/person",
//         "accountId": "436740350302",
//         "resourceId": "bnm17w",
//         "stage": "dev",
//         "requestId": "86ca64e3-b2e7-11e7-8f0f-b35047b62432",
//         "identity": {
//             "cognitoIdentityPoolId": null,
//             "accountId": null,
//             "cognitoIdentityId": null,
//             "caller": null,
//             "apiKey": "",
//             "sourceIp": "82.197.193.105",
//             "accessKey": null,
//             "cognitoAuthenticationType": null,
//             "cognitoAuthenticationProvider": null,
//             "userArn": null,
//             "userAgent": "HTTPie/0.9.9",
//             "user": null
//         },
//         "resourcePath": "/person",
//         "httpMethod": "POST",
//         "apiId": "d2yytbn0e3"
//     },
//     "body": "{\"name\": \"dennis\", \"age\": \"42\"}",
//     "isBase64Encoded": false
// }
export let post = (event: any, ctx: Context, cb: Callback) => {
    console.log(`handling event: ${JSON.stringify(event)}`);
    Person.validate(event.body).then(person => {
        repo.put(JSON.parse(event.body)).then(maybePerson => {
            maybePerson.forEach(person => cb(null, {statusCode: 200, body: JSON.stringify(maybePerson)}))
        }).catch(err => cb(null, {statusCode: 400, body: JSON.stringify(err)}))
    });
};

// {
//     "resource": "/person/{id}",
//     "path": "/person/97b4b074-4160-41cd-927d-970c6e11170e",
//     "httpMethod": "PATCH",
//     "headers": {
//         "Accept": "application/json, */*",
//         "Accept-Encoding": "gzip, deflate",
//         "CloudFront-Forwarded-Proto": "https",
//         "CloudFront-Is-Desktop-Viewer": "true",
//         "CloudFront-Is-Mobile-Viewer": "false",
//         "CloudFront-Is-SmartTV-Viewer": "false",
//         "CloudFront-Is-Tablet-Viewer": "false",
//         "CloudFront-Viewer-Country": "NL",
//         "Content-Type": "application/json",
//         "Host": "d2yytbn0e3.execute-api.eu-central-1.amazonaws.com",
//         "User-Agent": "HTTPie/0.9.9",
//         "Via": "1.1 2b7e0587e76bdc8afc2d63bea659b942.cloudfront.net (CloudFront)",
//         "X-Amz-Cf-Id": "EiNoMW-ThoeiofFNx6kX7SrZ_IuJsI6Jz7s3U9AQSH8WKcYRKHmqhg==",
//         "X-Amzn-Trace-Id": "Root=1-59e575b3-63c580c656d67de864da6c57",
//         "X-Forwarded-For": "82.197.193.105, 54.239.167.13",
//         "X-Forwarded-Port": "443",
//         "X-Forwarded-Proto": "https"
//     },
//     "queryStringParameters": null,
//     "pathParameters": {"id": "97b4b074-4160-41cd-927d-970c6e11170e"},
//     "stageVariables": null,
//     "requestContext": {
//         "path": "/dev/person/97b4b074-4160-41cd-927d-970c6e11170e",
//         "accountId": "436740350302",
//         "resourceId": "ppv24w",
//         "stage": "dev",
//         "requestId": "5b90a69d-b2e9-11e7-bddb-0b92f26c0a9c",
//         "identity": {
//             "cognitoIdentityPoolId": null,
//             "accountId": null,
//             "cognitoIdentityId": null,
//             "caller": null,
//             "apiKey": "",
//             "sourceIp": "82.197.193.105",
//             "accessKey": null,
//             "cognitoAuthenticationType": null,
//             "cognitoAuthenticationProvider": null,
//             "userArn": null,
//             "userAgent": "HTTPie/0.9.9",
//             "user": null
//         },
//         "resourcePath": "/person/{id}",
//         "httpMethod": "PATCH",
//         "apiId": "d2yytbn0e3"
//     },
//     "body": "{\"name\": \"dennis\", \"age\": \"43\"}",
//     "isBase64Encoded": false
// }
export let patch = (event: any, ctx: Context, cb: Callback) => {
    console.log(`handling event: ${JSON.stringify(event)}`);
    let id = event.pathParameters.id;
    console.log(`updating person for id: ${id}: ` + JSON.stringify(event))
    Person.validate(event.body).then(person =>
        repo.update(id, person).then(maybePerson => {
            maybePerson.forEach(person => cb(null, {statusCode: 200, body: JSON.stringify(maybePerson)}))
        }).catch(err => cb(null, {statusCode: 400, body: JSON.stringify(err)}))
    );
};

// {
//     "resource": "/person/{id}",
//     "path": "/person/97b4b074-4160-41cd-927d-970c6e11170e",
//     "httpMethod": "DELETE",
//     "headers": {
//         "Accept": "*/*",
//         "Accept-Encoding": "gzip, deflate",
//         "CloudFront-Forwarded-Proto": "https",
//         "CloudFront-Is-Desktop-Viewer": "true",
//         "CloudFront-Is-Mobile-Viewer": "false",
//         "CloudFront-Is-SmartTV-Viewer": "false",
//         "CloudFront-Is-Tablet-Viewer": "false",
//         "CloudFront-Viewer-Country": "NL",
//         "Host": "d2yytbn0e3.execute-api.eu-central-1.amazonaws.com",
//         "User-Agent": "HTTPie/0.9.9",
//         "Via": "1.1 d5e8c461ea4d131327b2ba97a2d7f473.cloudfront.net (CloudFront)",
//         "X-Amz-Cf-Id": "MxX6kRv3c9rvPuTTwW-Ke9qankqj1kMMS-ckVH-6qNT0S8WAB7K8Pw==",
//         "X-Amzn-Trace-Id": "Root=1-59e575e7-566069da38aa22f40792dba9",
//         "X-Forwarded-For": "82.197.193.105, 54.239.167.18",
//         "X-Forwarded-Port": "443",
//         "X-Forwarded-Proto": "https"
//     },
//     "queryStringParameters": null,
//     "pathParameters": {"id": "97b4b074-4160-41cd-927d-970c6e11170e"},
//     "stageVariables": null,
//     "requestContext": {
//         "path": "/dev/person/97b4b074-4160-41cd-927d-970c6e11170e",
//         "accountId": "436740350302",
//         "resourceId": "ppv24w",
//         "stage": "dev",
//         "requestId": "7b03f36b-b2e9-11e7-b40a-05f2e3da3536",
//         "identity": {
//             "cognitoIdentityPoolId": null,
//             "accountId": null,
//             "cognitoIdentityId": null,
//             "caller": null,
//             "apiKey": "",
//             "sourceIp": "82.197.193.105",
//             "accessKey": null,
//             "cognitoAuthenticationType": null,
//             "cognitoAuthenticationProvider": null,
//             "userArn": null,
//             "userAgent": "HTTPie/0.9.9",
//             "user": null
//         },
//         "resourcePath": "/person/{id}",
//         "httpMethod": "DELETE",
//         "apiId": "d2yytbn0e3"
//     },
//     "body": null,
//     "isBase64Encoded": false
// }
export let remove = (event: APIGatewayEvent, ctx: Context, cb: Callback) => {
    console.log(`handling event: ${JSON.stringify(event)}`);
    Option(event.pathParameters)
        .map(params => params.id)
        .fold(() => {
            cb(null, {statusCode: 500, body: JSON.stringify("No pathParameters found")})
        }, id => {
            console.log(`Deleting person for id: ${id}`)
            repo.remove(id).then(() => {
                cb(null, {statusCode: 204});
            }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}));
        });
};