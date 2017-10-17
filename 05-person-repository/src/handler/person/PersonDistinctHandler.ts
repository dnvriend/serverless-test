import {DynamoDB} from "aws-sdk";
import {Callback, Context} from "aws-lambda";
import {AttributeMap, GetRecordsOutput, Record} from "aws-sdk/clients/dynamodbstreams"
import {PersonDistinct, PersonDistinctRepository} from "./PersonDistinctRepository";
import lift, {Option} from 'space-lift';

let tableName = `${process.env.SLS_STAGE}-persons-distinct-table`;
let docClient = new DynamoDB.DocumentClient();
let repo = new PersonDistinctRepository(tableName, docClient);

let newImageToPersonDistinct: (image: AttributeMap) => PersonDistinct = (image: AttributeMap) => {
    console.log(`mapping image: ${JSON.stringify(image)}`);
    let name = Option(image.name.S).getOrElse("unknown");
    console.log(`extracted name:  ${name}`);
    return new PersonDistinct(name);
};

export let handle = (event: GetRecordsOutput, ctx: Context) => {
    console.log(`handling event: ${JSON.stringify(event)}`);
    Option(event.Records).forEach(records =>
        records.map((record: Record) => record!.dynamodb!.NewImage)
        .map(newImageToPersonDistinct)
        .forEach((pd: PersonDistinct) => repo.put(pd)));    
};

export let list = (event: any, ctx: Context, cb: Callback) => {
    console.log(`getting list of distinct names: ${JSON.stringify(event)}`);
    repo.list().then(xs => {
        cb(null, {statusCode: 200, body: JSON.stringify(xs)})
    }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}));
};
