# Sample app that encrypts data on GF. 

**Simple example on how to use encryption on GF. Don't use in PROD.**

** KEY MANAGEMENT is out of scope here **

Sample uses three different ways of encrypting data (see [tests](src/test/java/io/pivotal/gemfire/encryption/DataEncryptionTest.java)):

- client side: data/attribute/field are encrypted using bouncycastle lib on the client app. 

- cache writer: data/attribute/field are encrypted using bouncycastle lib when client put objects on the region. 

- compressor (as encryption): register a **compressor** on the region and data is encrypted and decrypted on writings and readings, respectively. 


The Boot app uses the compressor 


## To compile

```
$ mvn clean package
```

## To run (client)

```
$  java -jar target/dataencryption-0.0.1-SNAPSHOT.jar --gemfire.locator.address=<locator addr> --gemfire.locator.port=<locator port> --passphrase=<passphrase used to encrypt/decrypt> --gemfire.region.name=<region name>
```

## To run (server)

```
$ ./start-gf.sh <passphrase>
```

## Dependencies

Download BouncyCastle [here](https://www.bouncycastle.org/download/bcprov-jdk15on-154.jar)

Make sure to copy the jar file to your $BASEDIR (per start-gf.sh script). 

Also copy the target/dataencryption-0.0.1-SNAPSHOT-shared.jar to $BASEDIR


## Cache.xml (encryption as compressor)

```xml
<?xml version="1.0" encoding= "UTF-8"?>
<cache xmlns="http://schema.pivotal.io/gemfire/cache"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://schema.pivotal.io/gemfire/cache http://schema.pivotal.io/gemfire/cache/cache-8.1.xsd"
    version="8.1"  is-server="true"  copy-on-read="true">
   <region name="dummyRegion">
      <region-attributes data-policy="partition">
            <compressor>
           <class-name>io.pivotal.gemfire.encryption.shared.EncryptionC</class-name>
                </compressor>
      </region-attributes>
  </region>
</cache>
```

## Cache.xml (cache-writer)

```xml
<?xml version="1.0" encoding= "UTF-8"?>
<cache xmlns="http://schema.pivotal.io/gemfire/cache"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://schema.pivotal.io/gemfire/cache http://schema.pivotal.io/gemfire/cache/cache-8.1.xsd"
    version="8.1"  is-server="true"  copy-on-read="true">
   <region name="dummyRegion">
      <region-attributes data-policy="partition">
            <cache-writer>
              <class-name>io.pivotal.gemfire.encryption.shared.EncryptionWriter</class-name>
           </cache-writer>
      </region-attributes>
  </region>
</cache>
```
