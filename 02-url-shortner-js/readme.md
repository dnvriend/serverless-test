# 02-url-shortner-js

## Service info
After deploying the service with `sls deploy` and querying information about the service with `sls info` we get the following information:

```
$ sls info
Service Information
service: urlshortener
stage: dev
region: us-east-1
stack: urlshortener-dev
api keys:
  None
endpoints:
  GET - https://tdhzd7kxtl.execute-api.us-east-1.amazonaws.com/dev
  POST - https://tdhzd7kxtl.execute-api.us-east-1.amazonaws.com/dev
functions:
  main: urlshortener-dev-main
  create: urlshortener-dev-create
```

The service is deployed to `us-east-1` and there are two functions, `main` and `create` that and there are two endpoints that are listed above with the operations `GET` and `POST` that are made available on stage `dev` as you can see on the url.

## Invoking the functions
We can still invoke the service by hand:

```
$ sls invoke -f main -log
{
    "statusCode": 200,
    "body": "<html>\n<h1>Hi!</h1>\n  <form method=\"POST\" action=\"\">\n    <label for=\"uri\">Link:</label>\n    <input type=\"text\" id=\"link\" name=\"link\" size=\"40\" autofocus />\n    <br/>\n    <br/>\n    <input type=\"submit\" value=\"Shorten it!\" />\n  </form>\n</html>",
    "headers": {
        "Content-Type": "text/html"
    }
}
--------------------------------------------------------------------
START RequestId: b433bd48-af7a-11e7-8f67-a1b4d671ca78 Version: $LATEST
2017-10-12 20:25:20.238 (+02:00)	b433bd48-af7a-11e7-8f67-a1b4d671ca78	{}
END RequestId: b433bd48-af7a-11e7-8f67-a1b4d671ca78
REPORT RequestId: b433bd48-af7a-11e7-8f67-a1b4d671ca78	Duration: 0.37 ms	Billed Duration: 100 ms 	Memory Size: 1024 MB	Max Memory Used: 21 MB
```

## Accessing the service by HTTP
We can use the `httpie` tool to use eg. the GET endpoint:

```
$ http https://tdhzd7kxtl.execute-api.us-east-1.amazonaws.com/dev
HTTP/1.1 200 OK
Connection: keep-alive
Content-Length: 241
Content-Type: text/html
Date: Thu, 12 Oct 2017 18:26:36 GMT
Via: 1.1 5f27ca52729763588bba68f65c5cb11d.cloudfront.net (CloudFront)
X-Amz-Cf-Id: doLNFlKonpzBJiZQPsNXu2X-oEQGNs3Ql57aOAZlm8tN6Qa4xa6rZw==
X-Amzn-Trace-Id: sampled=0;root=1-59dfb3dc-b15e7dcf5fcc0180e6947e49
X-Cache: Miss from cloudfront
x-amzn-RequestId: e16fa315-af7a-11e7-86fb-83c469fe2cbf

<html>
<h1>Hi!</h1>
  <form method="POST" action="">
    <label for="uri">Link:</label>
    <input type="text" id="link" name="link" size="40" autofocus />
    <br/>
    <br/>
    <input type="submit" value="Shorten it!" />
  </form>
</html>
```

## Uninstall
To uninstall the service type: `sls remove`.

## Resources
- http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-set-up-simple-proxy.html#api-gateway-simple-proxy-for-lambda-input-format
