import {DocumentClient} from "aws-sdk/lib/dynamodb/document_client";
import AttributeMap = DocumentClient.AttributeMap;
import lift, {Option} from 'space-lift'

export class CounterRepository {
    constructor(private readonly tableName: string, private readonly db: DocumentClient) {
    }

    update(counterName: string) {
        return this.db.update({
            TableName: this.tableName,
            Key: {
                counter: counterName
            },
            UpdateExpression: "ADD countervalue :incr",
            ExpressionAttributeValues: {
                ":incr": 1
            },
            ReturnValues: "UPDATED_NEW"
        }).promise().then(data => {
            return Option(data.Attributes)
                .map(items => items.countervalue)
        })
    }

    // cli command
    // aws dynamodb update-item \
    // --table-name dev-counters-table \
    // --key '{"counter":{"S":"c3"}}' \
    // --update-expression "ADD countervalue :incr" \
    // --expression-attribute-values '{":incr": { "N": "1"}}' \
    // --return-values ALL_NEW

    // update(counterName: string) {
    //     return this.db.update({
    //         TableName: this.tableName,
    //         Key: {
    //             counter: counterName
    //         },
    //         UpdateExpression: "SET countervalue = countervalue + :incr",
    //         ExpressionAttributeValues: {
    //             ":incr": 1
    //         },
    //         ReturnValues: "UPDATED_NEW"
    //     }).promise().then(data => {
    //         return Option(data.Attributes)
    //             .map(items => items.countervalue)
    //     })
    // }
    //
    // get(counterName: string) {
    //     return this.db.get({
    //         TableName: this.tableName,
    //         Key: {
    //             counter: counterName
    //         }
    //     }).promise().then(data => {
    //         return Option(data.Item)
    //             .map(items => items.countervalue)
    //     })
    // }
    //
    // put(counterName: string) {
    //     return this.db.put({
    //         TableName: this.tableName,
    //         Item: {
    //             counter: counterName,
    //             countervalue: 1
    //         }
    //     }).promise()
    // }
}