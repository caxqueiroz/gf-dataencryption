package io.pivotal.gemfire.encryption;

import io.pivotal.gemfire.encryption.shared.Dummy;
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


        Dummy dummy1 = new Dummy();
        dummy1.setId(2L);
        dummy1.setData1("My data");
        dummy1.setData2("777-777-777-777");

        service.saveData(dummy1);

        Dummy returnedObject = service.getData(2L);

        logger.info("returned object: {}", returnedObject);

    }



}
