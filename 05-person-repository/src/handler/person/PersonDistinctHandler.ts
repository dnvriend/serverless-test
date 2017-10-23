import {DynamoDB} from "aws-sdk";
import {Callback, Context} from "aws-lambda";
import {AttributeMap, GetRecordsOutput, Record} from "aws-sdk/clients/dynamodbstreams"
import {PersonDistinct, PersonDistinctRepository} from "./PersonDistinctRepository";
import lift, {Option} from 'space-lift';

const tableName = `${process.env.SLS_STAGE}-persons-distinct-table`;
const docClient = new DynamoDB.DocumentClient();
const repo = new PersonDistinctRepository(tableName, docClient);

const newImageToPersonDistinct: (image: AttributeMap) => PersonDistinct = (image: AttributeMap) => {
    console.log("mapping image: ", JSON.stringify(image));
    const name = Option(image.name.S).getOrElse("unknown");
    console.log("extracted name: ", name);
    return { name: name };
};

export const handle = (event: GetRecordsOutput, ctx: Context) => {
    console.log("handling event: ", JSON.stringify(event));
    Option(event.Records).forEach(records =>
        records.map((record: Record) => record!.dynamodb!.NewImage)
        .map(newImageToPersonDistinct)
        .forEach((pd: PersonDistinct) => repo.put(pd)));    
};

export const list = (event: any, ctx: Context, cb: Callback) => {
    console.log("getting list of distinct names: ", JSON.stringify(event));
    repo.list().then(xs => {
        cb(null, {statusCode: 200, body: JSON.stringify(xs)})
    }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}));
};
