package io.pivotal.gemfire.encryption;

import com.gemstone.gemfire.cache.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataEncryptApp implements CommandLineRunner{

    Logger logger = LoggerFactory.getLogger(DataEncryptApp.class);

    @Autowired
    private DataEncryptionService service;

	public static void main(String[] args) {
		SpringApplication.run(DataEncryptApp.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {

        Dummy dummy0 = new Dummy();
        dummy0.setId(1L);
        dummy0.setData1("Hello World!");
        dummy0.setData2("555-555-555-555");

        service.saveData(dummy0);

        Dummy dummy1 = new Dummy();
        dummy1.setId(2L);
        dummy1.setData1("My data");
        dummy1.setData2("777-777-777-777");

        service.saveData(dummy1);

        Dummy returnedObject = service.getData(1L);

        logger.info("returned object (NOT encrypted): {}", returnedObject);


        service.saveDataAndEncrypt(dummy1);

        Dummy returnedObjectWithEncryption = service.getData(2L);

        logger.info("returned object (with encryption): {}", returnedObjectWithEncryption);

        returnedObjectWithEncryption = service.getDataAndDecrypt(2L);

        logger.info("returned object (with encryption and decryption): {}", returnedObjectWithEncryption);



    }



}
