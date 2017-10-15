import {DocumentClient} from "aws-sdk/lib/dynamodb/document_client";
import v4 = require("uuid/v4");
import AttributeMap = DocumentClient.AttributeMap;
import lift, {Option} from 'space-lift'

export class PersonDistinct {
    constructor(public readonly name: String) {}
}

let mapAttributes = (attributes: AttributeMap) => {
    return new PersonDistinct(attributes.name)
};

export class PersonDistinctRepository {
    constructor(public readonly tableName: string, public readonly db: DocumentClient) {
    }

    list(): Promise<never | Array<PersonDistinct>> {
        return this.db.scan({
            TableName: this.tableName
        }).promise().then(data => {
            return Option(data.Items)
                .map(xs => xs.map(mapAttributes))
                .getOrElse([])
        })
    }

    put(pd: PersonDistinct): Promise<never | Option<PersonDistinct>> {
        let id = v4();
        return this.db.put({
            TableName: this.tableName,
            Item: {
                name: pd.name
            }
        }).promise().then(data => {
            return Option(pd)
        })
    }
}