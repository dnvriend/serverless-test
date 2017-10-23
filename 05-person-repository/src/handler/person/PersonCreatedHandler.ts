import {Context} from "aws-lambda";
import {GetRecordsOutput, Record, Data} from "aws-sdk/clients/kinesis"

const decodeData = (data: Data) => {
    return new Buffer(data.toString(), 'base64').toString('utf-8')
};

// {
//     "Records": [{
//         "kinesis": {
//             "kinesisSchemaVersion": "1.0",
//             "partitionKey": "1",
//             "sequenceNumber": "49578004267304126490250764745524898857376013314355626018",
//             "data": "eyJpZCI6ICIxIiwgIm5hbWUiOiJkZW5uaXMiLCAiYWdlIjoiNDIifQ==",
//             "approximateArrivalTimestamp": 1508209853.067
//         },
//         "eventSource": "aws:kinesis",
//         "eventVersion": "1.0",
//         "eventID": "shardId-000000000002:49578004267304126490250764745524898857376013314355626018",
//         "eventName": "aws:kinesis:record",
//         "invokeIdentityArn": "arn:aws:iam::436740350302:role/person-repository-dev-eu-central-1-lambdaRole",
//         "awsRegion": "eu-central-1",
//         "eventSourceARN": "arn:aws:kinesis:eu-central-1:436740350302:stream/PersonCreatedStream"
//     }, {
//         "kinesis": {
//             "kinesisSchemaVersion": "1.0",
//             "partitionKey": "1",
//             "sequenceNumber": "49578004267304126490250764745526107783195628012249808930",
//             "data": "eyJpZCI6ICIxIiwgIm5hbWUiOiJkZW5uaXMiLCAiYWdlIjoiNDIifQ==",
//             "approximateArrivalTimestamp": 1508209854.338
//         },
//         "eventSource": "aws:kinesis",
//         "eventVersion": "1.0",
//         "eventID": "shardId-000000000002:49578004267304126490250764745526107783195628012249808930",
//         "eventName": "aws:kinesis:record",
//         "invokeIdentityArn": "arn:aws:iam::436740350302:role/person-repository-dev-eu-central-1-lambdaRole",
//         "awsRegion": "eu-central-1",
//         "eventSourceARN": "arn:aws:kinesis:eu-central-1:436740350302:stream/PersonCreatedStream"
//     }]
// }
/**
 * Handles Kinesis Streams GetRecordsOutput
 * @param {Kinesis.GetRecordsOutput} event
 * @param {Context} ctx
 */
export const handle = (event: GetRecordsOutput, ctx: Context) => {
    console.log("handling event: ", JSON.stringify(event))
    event.Records
        .map((record: any) => record.kinesis.data)
        .map(decodeData)
        .map((str: string) => JSON.parse(str))
        .forEach((person: any) => console.log(`person: ${JSON.stringify(person)}`));
};