# Sample application that encrypt data and save on Gemfire. 

## To compile

```
$ mvn clean package
```

## To run

```
$ mvn spring-boot:run --gemfire.locator.address=<locator addr> --gemfire.locator.port=<locator port> --passphrase=<passphrase used to encrypt/decrypt>
```


