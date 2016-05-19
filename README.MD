# Sample application that encrypt data and save on Gemfire. 

Very BASIC application. **Not using best practices for security**. 

## To compile

```
$ mvn clean package
```

## To run

```
$ mvn spring-boot:run --gemfire.locator.address=<locator addr> --gemfire.locator.port=<locator port> --passphrase=<passphrase used to encrypt/decrypt>
```


