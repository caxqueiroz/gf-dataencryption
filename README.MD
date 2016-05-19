# Sample application that encrypt data and save on Gemfire. 

Sample uses three different ways of encrypting data (see tests):

- client side: data/attribute/field are encrypted using bouncycastle lib on the client app. 

- cache writer: data/attribute/field are encrypted using bouncycastle lib when client put objects on the region. 

- compressor (as encryption): register a **compressor** on the region and data is encrypted and decrypted on writings and readings, respectively. 


The Boot app uses the compressor 


## To compile

```
$ mvn clean package
```

## To run

```
$  java -jar target/dataencryption-0.0.1-SNAPSHOT.jar --gemfire.locator.address=<locator addr> --gemfire.locator.port=<locator port> --passphrase=<passphrase used to encrypt/decrypt>
```


