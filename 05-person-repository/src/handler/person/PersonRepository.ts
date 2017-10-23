import {DocumentClient} from "aws-sdk/lib/dynamodb/document_client";
import v4 = require("uuid/v4");
import AttributeMap = DocumentClient.AttributeMap;
import lift, {Option} from 'space-lift'
import {NotEmpty} from "validator.ts/decorator/Validation";
import {Validator} from "validator.ts/Validator";

export interface Person {
    id: string; 
    name: string; 
    age: string;
}

const mapAttributes = (attributes: AttributeMap) => {
    return {
        id: attributes.id,
        name: attributes.name,
        age: attributes.age
    }
};

/**
 * Person Repository
 */
export class PersonRepository {
    constructor(private readonly tableName: string, private readonly db: DocumentClient) {
    }

    list(): Promise<never | Array<Person>> {
        return this.db.scan({
            TableName: this.tableName
        }).promise().then(data => {
            return Option(data.Items)
                .map(xs => xs.map(mapAttributes))
                .getOrElse([])
        })
    }

    get(id: string): Promise<never | Option<Person>> {
        return this.db.get({
            TableName: this.tableName,
            Key: {
                id: id
            }
        }).promise().then(data => {
            return Option(data.Item).map(mapAttributes)
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

    update(id: string, person: Person): Promise<never | Option<Person>> {
        return this.db.update({
            TableName: this.tableName,
            Key: {
                id: id
            },
            UpdateExpression: "SET #n = :name, age = :age",
            ExpressionAttributeValues: {
                ":name": person.name,
                ":age": person.age
            },
            ExpressionAttributeNames: {
                "#n": "name"
            },
            ReturnValues: "UPDATED_NEW"
        }).promise().then(data => {
            return Option(data.Attributes).map(mapAttributes)
        })
    }

    put(person: Person): Promise<never | Option<Person>> {
        const id = v4();
        return this.db.put({
            TableName: this.tableName,
            Item: {
                id: id,
                name: person.name,
                age: person.age
            },
        }).promise().then(() => {
            return Option({id: id, name: person.name, age: person.age });
        });
    }
}