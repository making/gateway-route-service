# Cloud Foundry Route Service by Spring Cloud Gateway

## Test locally

```
$ curl localhost:8080 -H "X-CF-Forwarded-Url: http://httpbin.org/headers" -H "X-CF-Proxy-Metadata: a" -H "X-CF-Proxy-Signature: a"

{
  "headers": {
    "Accept": "*/*", 
    "Connection": "close", 
    "Forwarded": "proto=http;host=\"localhost:8080\";for=\"0:0:0:0:0:0:0:1:59298\"", 
    "Host": "httpbin.org", 
    "User-Agent": "curl/7.54.0", 
    "X-Cf-Forwarded-Url": "http://httpbin.org/headers", 
    "X-Cf-Proxy-Metadata": "a", 
    "X-Cf-Proxy-Signature": "a", 
    "X-Forwarded-Host": "localhost:8080"
  }
}
```

## Deploy to Cloud Foundry

```
./mvnw clean package -DskipTests=true
cf push
cf create-user-provided-service demo-gateway -r https://gateway-route-service.cfapps.io
```

```
cf bind-route-service cfapps.io demo-gateway --hostname your-app
```