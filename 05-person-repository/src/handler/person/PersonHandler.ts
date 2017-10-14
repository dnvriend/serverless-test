import {DynamoDB} from "aws-sdk";
import {Callback, Context} from "aws-lambda";
import {PersonRepository} from "./PersonRepository";

// node.js, which is available everywhere defines some vars like
// process, global and console.

let tableName = `${process.env.SLS_STAGE}-persons`;
let docClient = new DynamoDB.DocumentClient();
let repo = new PersonRepository(tableName, docClient);

export let list = (event: any, ctx: Context, callback: Callback) => {
    repo.list().then(xs => {
        callback(null, {statusCode: 200, body: JSON.stringify(xs)})
    }).catch(err => callback(err))
};

export let get = (event: any, ctx: Context, callback: Callback) => {
    let id = event.pathParameters.id
    repo.get(id).then(person => {
        if (person) {
            callback(null, {statusCode: 200, body: JSON.stringify(person)})
        } else {
            callback(null, {statusCode: 404, body: `Nothing found for id: ${id}`})
        }
    }).catch(err => callback(err))
};

export let post = (event: any, ctx: Context, cb: Callback) => {
    repo.put(JSON.parse(event.body))
        .then(person => {
            cb(null, {statusCode: 200, body: JSON.stringify(person)})
        }).catch(err => cb(err))
};

export let patch = (event: any, ctx: Context, cb: Callback) => {
    repo.update(JSON.parse(event.body))
        .then(person => {
            cb(null, {statusCode: 200, body: JSON.stringify(person)})
        }).catch(err => cb(err))
};

export let remove = (event: any, ctx: Context, cb: Callback) => {
    repo.remove(event.pathParameters.id).then(() => {
        cb(null, {statusCode: 200})
    }).catch(err => cb(err))
};

