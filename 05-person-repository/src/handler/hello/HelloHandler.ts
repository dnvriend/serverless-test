import {Context, Callback} from 'aws-lambda';
import {v4} from "uuid"

export const get = (event: any, ctx: Context, callback: Callback) => {
    callback(null, {
        statusCode: 200,
        body: JSON.stringify(v4() + "hello world! " + JSON.stringify(event))
    });
};