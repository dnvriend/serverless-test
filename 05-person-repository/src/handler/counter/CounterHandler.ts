import { CounterRepository } from './CounterRepository';
import {DynamoDB} from "aws-sdk";
import {Callback, Context} from "aws-lambda";
import lift, {Option} from 'space-lift'

// node.js, which is available everywhere defines some vars like
// process, global and console.

const tableName = `${process.env.SLS_STAGE}-counters-table`;
const docClient = new DynamoDB.DocumentClient();
const repo = new CounterRepository(tableName, docClient);

// let createCounterAndRespond = (counterName: string, cb: Callback) => {
//     console.log(`Creating counter: ${counterName}`);
//     repo.put(counterName).then(() => {
//         cb(null, { statusCode: 200, body: JSON.stringify({counterName: counterName, counterValue: 1})});
//     }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}));
// };
//
// let incrementCounterAndRespond = (counterName: string, cb: Callback) => {
//     console.log(`Incrementing counter: ${counterName}`);
//     repo.update(counterName).then((maybeNumber: Option<any>) => {
//         maybeNumber.forEach(number => {
//             cb(null, { statusCode: 200, body: JSON.stringify({counterName: counterName, counterValue: number})});
//         });
//     }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}));
// };

export const nextValue = (event: any, ctx: Context, cb: Callback) => {
    console.log("handling event: ", JSON.stringify(event));
    const counterName = event.pathParameters.counterName;
    repo.update(counterName).then((maybeNumber: Option<any>) => {
        maybeNumber.fold(() => {
            cb(null, {statusCode: 500, body: JSON.stringify(`No counter found for name ${counterName}`)});
        }, number => {
            cb(null, { statusCode: 200, body: JSON.stringify({counterName: counterName, counterValue: number}) });
        });
    });
    // repo.get(counterName).then((maybeNumber: Option<any>) => {
    //     console.log(`maybeNumber for counterName: ${counterName}: ${maybeNumber}`);
    //     maybeNumber.fold(() => createCounterAndRespond(counterName, cb), _ => incrementCounterAndRespond(counterName, cb));
    // }).catch(err => cb(null, {statusCode: 500, body: JSON.stringify(err)}));
};