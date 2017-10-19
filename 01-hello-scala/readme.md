# 01-hello-scala

## Package and install
You can install the service with the following commands:

```
sbt assembly
sls deploy
```

## Tools
- awscala:
  - home: https://github.com/seratch/AWScala
  - sbt: libraryDependencies += "com.github.seratch" % "awscala_2.12" % "0.6.1"



## Invoke the function
You can invoke the function with:

```
sls invoke -f method-handler --data '{"key1":"a", "key2":"b", "key3":"c"}' --log
{
    "message": "Hi, this function worked!",
    "request": {
        "key1": "a",
        "key2": "b",
        "key3": "c"
    }
}
--------------------------------------------------------------------
START RequestId: bb9dbab1-af75-11e7-a518-2561357d018d Version: $LATEST
invoked with: Request(a,b,c)END RequestId: bb9dbab1-af75-11e7-a518-2561357d018d
REPORT RequestId: bb9dbab1-af75-11e7-a518-2561357d018d	Duration: 354.33 ms	Billed Duration: 400 ms 	Memory Size: 1024 MB	Max Memory Used: 89 MB
```

or

```
$ sls invoke -f interface-handler --data '{"key1":"a", "key2":"b", "key3":"c"}' --log
{
    "message": "Hi, this function worked!",
    "request": {
        "key1": "a",
        "key2": "b",
        "key3": "c"
    }
}
--------------------------------------------------------------------
START RequestId: f4612487-afc3-11e7-af86-fbfc779740e3 Version: $LATEST
invoked with: MyRequest(a,b,c)END RequestId: f4612487-afc3-11e7-af86-fbfc779740e3
REPORT RequestId: f4612487-afc3-11e7-af86-fbfc779740e3	Duration: 0.68 ms	Billed Duration: 100 ms 	Memory Size: 1024 MB	Max Memory Used: 77 MB
```

or the stream handler:

```
$ sls invoke -f stream-handler --data '{"name":"dennis", "age":42}' --log
{
    "name": "dennis",
    "age": 42
}
--------------------------------------------------------------------
START RequestId: 813ab34f-afc7-11e7-b547-2b45933dfa49 Version: $LATEST
END RequestId: 813ab34f-afc7-11e7-b547-2b45933dfa49
REPORT RequestId: 813ab34f-afc7-11e7-b547-2b45933dfa49	Duration: 78.37 ms	Billed Duration: 100 ms 	Memory Size: 1024 MB	Max Memory Used: 99 MB
```

## Uninstall
You can uninstall the service with:

```
$ sls remove
Serverless: Getting all objects in S3 bucket...
Serverless: Removing objects in S3 bucket...
Serverless: Removing Stack...
Serverless: Checking Stack removal progress...
....
Serverless: Stack removal finished...
```

## Resources
- http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model.html
- http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model-handler-types.html
- http://docs.aws.amazon.com/lambda/latest/dg/java-handler-using-predefined-interfaces.html
- http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model-req-resp.html
- http://docs.aws.amazon.com/lambda/latest/dg/java-handler-io-type-stream.html
- https://stackoverflow.com/questions/13812172/how-can-i-create-an-instance-of-a-case-class-with-constructor-arguments-with-no
- https://stackoverflow.com/questions/22756542/how-do-i-add-a-no-arg-constructor-to-a-scala-case-class-with-a-macro-annotation
