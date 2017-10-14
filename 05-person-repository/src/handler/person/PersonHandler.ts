import {DynamoDB} from "aws-sdk";
import {Callback, Context} from "aws-lambda";
import {Person, PersonRepository} from "./PersonRepository";

// node.js, which is available everywhere defines some vars like
// process, global and console.

let tableName = `${process.env.SLS_STAGE}-persons`;
let docClient = new DynamoDB.DocumentClient();
let repo = new PersonRepository(tableName, docClient);

export let list = (event: any, ctx: Context, cb: Callback) => {
    repo.list().then(xs => {
        cb(null, {statusCode: 200, body: JSON.stringify(xs)})
    }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}))
};

export let get = (event: any, ctx: Context, cb: Callback) => {
    let id = event.pathParameters.id
    repo.get(id).then(maybePerson => {
        maybePerson
            .map(person => cb(null, {statusCode: 200, body: JSON.stringify(person)}))
            .getOrElse(cb(null, {statusCode: 404, body: `Nothing found for id: ${id}`}))
    }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}))
};

export let post = (event: any, ctx: Context, cb: Callback) => {
    Person.validate(event.body).then(person =>
        repo.put(JSON.parse(event.body)).then(maybePerson => {
            maybePerson.forEach(person => cb(null, {statusCode: 200, body: JSON.stringify(maybePerson)}))
        }).catch(err => cb(null, {statusCode: 400, body: JSON.stringify(err)})))
};

export let patch = (event: any, ctx: Context, cb: Callback) => {
    Person.validate(event.body).then(person =>
        repo.update(event.pathParameters.id, person).then(maybePerson => {
            maybePerson.forEach(person => cb(null, {statusCode: 200, body: JSON.stringify(maybePerson)}))
        }).catch(err => cb(null, {statusCode: 400, body: JSON.stringify(err)}))
    );
};

export let remove = (event: any, ctx: Context, cb: Callback) => {
    repo.remove(event.pathParameters.id).then(() => {
        cb(null, {statusCode: 204})
    }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}))
};