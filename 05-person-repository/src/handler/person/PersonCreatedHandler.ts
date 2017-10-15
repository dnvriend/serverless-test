import {Context} from "aws-lambda";
import {GetRecordsOutput, Record, Data} from "aws-sdk/clients/kinesis"

let decodeData = (data: Data) => {
    return new Buffer(data.toString(), 'base64').toString('utf-8')
};

export let handle = (event: GetRecordsOutput, ctx: Context) => {
    console.log("handling event: " + JSON.stringify(event));
    event.Records
        .map((record: any) => record.kinesis.data)
        .map(decodeData)
        .map((str: string) => JSON.parse(str))
        .forEach((person:any) => console.log("person: " + JSON.stringify(person)));
};