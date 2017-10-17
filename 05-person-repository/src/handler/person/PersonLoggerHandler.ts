import {Context} from "aws-lambda";
import {GetRecordsOutput} from "aws-sdk/clients/dynamodbstreams"

export let handle = (event: GetRecordsOutput, ctx: Context) => {
    console.log(`handling event: ${JSON.stringify(event)}`);    
};