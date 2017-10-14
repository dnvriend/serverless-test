import {DocumentClient} from "aws-sdk/lib/dynamodb/document_client";
import v4 = require("uuid/v4");
import AttributeMap = DocumentClient.AttributeMap;
import lift, {Option} from 'space-lift'
import {NotEmpty} from "validator.ts/decorator/Validation";
import {Validator} from "validator.ts/Validator";

export class Person {
    constructor(id: string, name: string, age: string) {
        this.id = id
        this.name = name
        this.age = age
    }

    static validate(str: string): Promise<Person> {
        let js = JSON.parse(str)
        let validator = new Validator()
        return validator.validateAsync(new Person(
            Option(js.id).getOrElse(""),
            Option(js.name).getOrElse(""),
            Option(js.age).getOrElse("")))
    }

    id: string;

    @NotEmpty()
    name: string;

    @NotEmpty()
    age: string;
}

let attributesToPerson = (attributes: AttributeMap) => {
    return new Person(
        attributes.id,
        attributes.name,
        attributes.age
    )
};

/**
 * Person Repository
 */
export class PersonRepository {
    constructor(public readonly tableName: string, public readonly db: DocumentClient) {
    }

    list(): Promise<never | Array<Person>> {
        return this.db.scan({
            TableName: this.tableName
        }).promise().then(data => {
            return Option(data.Items)
                .map(xs => xs.map(attributesToPerson))
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
            return Option(data.Item).map(attributesToPerson)
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
            return Option(data.Attributes).map(attributesToPerson)
        })
    }

    put(person: Person): Promise<never | Option<Person>> {
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
            return Option(data.Attributes).map(attributesToPerson)
        })
    }
}