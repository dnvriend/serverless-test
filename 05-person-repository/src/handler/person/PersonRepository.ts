import {DocumentClient} from "aws-sdk/lib/dynamodb/document_client";
import v4 = require("uuid/v4");

/**
 * Person Repository
 */
export class PersonRepository {
    constructor(public readonly tableName: string, public readonly db: DocumentClient) {
    }

    private mapItem(item: any) {
        return {
            id: item.id,
            name: item.name,
            age: item.age
        }
    }

    list() {
        return this.db.scan({
            TableName: this.tableName
        }).promise().then(data => {
            if (data.Items) {
                return data.Items.map(this.mapItem)
            }
        })
    }

    get(id: string) {
        return this.db.get({
            TableName: this.tableName,
            Key: {
                id: id
            }
        }).promise().then(data => {
            if (data.Item) {
                return this.mapItem(data.Item)
            }
        })
    }

    remove(id: string) {
        return this.db.delete({
            TableName: this.tableName,
            Key: {
                id: id
            }
        }).promise()
    }

    update(person: any) {
        return this.db.update({
            TableName: this.tableName,
            Key: {
                id: person.id
            },
            AttributeUpdates: {
                name: person.name,
                age: person.age
            },
            ReturnValues: "ALL_NEW"
        }).promise().then(data => {
            if(data.Attributes) return {id: data.Attributes.id, name: data.Attributes.name, age: data.Attributes.age}
        })
    }

    put(person: any) {
        let id = v4();
        return this.db.put({
            TableName: this.tableName,
            Item: {
                id: id,
                name: person.name,
                age: person.age
            },
            ReturnValues: "ALL_NEW"
        }).promise().then(data => {
            if(data.Attributes) return {id: data.Attributes.id, name: data.Attributes.name, age: data.Attributes.age}
        })
    }
}