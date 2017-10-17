import {Context} from "aws-lambda";
import {GetRecordsOutput} from "aws-sdk/clients/dynamodbstreams"

// {
//     "Records": [{
//         "eventID": "76cf10791c99dbefa151911a78e20273",
//         "eventName": "INSERT",
//         "eventVersion": "1.1",
//         "eventSource": "aws:dynamodb",
//         "awsRegion": "eu-central-1",
//         "dynamodb": {
//             "ApproximateCreationDateTime": 1508209260,
//             "Keys": {"id": {"S": "97b4b074-4160-41cd-927d-970c6e11170e"}},
//             "NewImage": {
//                 "name": {"S": "dennis"},
//                 "id": {"S": "97b4b074-4160-41cd-927d-970c6e11170e"},
//                 "age": {"S": "42"}
//             },
//             "SequenceNumber": "100000000002733545241",
//             "SizeBytes": 91,
//             "StreamViewType": "NEW_IMAGE"
//         },
//         "eventSourceARN": "arn:aws:dynamodb:eu-central-1:436740350302:table/dev-persons-table/stream/2017-10-17T02:57:00.704"
//     }]
// };
/**
 * Handles DynamoDBStreams GetRecordsOutput
 * @param {DynamoDBStreams.GetRecordsOutput} event
 * @param {Context} ctx
 */
export let handle = (event: GetRecordsOutput, ctx: Context) => {
    console.log(`handling event: ${JSON.stringify(event)}`);
};