import {DocumentClient} from "aws-sdk/lib/dynamodb/document_client";
import v4 = require("uuid/v4");
import AttributeMap = DocumentClient.AttributeMap;
import lift, {Option} from 'space-lift'

export interface PersonDistinct {
    name: string;
}

export class PersonDistinctRepository {
    constructor(private readonly tableName: string, private readonly db: DocumentClient) {
    }

    list(): Promise<never | Array<PersonDistinct>> {
        return this.db.scan({
            TableName: this.tableName
        }).promise().then(data => {
            return Option(data.Items)
                .map(xs => xs.map(attr => attr.name))
                .getOrElse([])
        })
    }

    put(pd: PersonDistinct): Promise<never | Option<PersonDistinct>> {
        const id = v4();
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