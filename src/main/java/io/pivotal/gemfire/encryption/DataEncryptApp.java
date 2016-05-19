package io.pivotal.gemfire.encryption;

import com.gemstone.gemfire.cache.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataEncryptApp implements CommandLineRunner{

    @Autowired
    private DummyRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(DataEncryptApp.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {

        Dummy dummy0 = new Dummy();
        dummy0.setId(1L);
        dummy0.setData1("Hello World!");
        dummy0.setData2("555-555-555-555");

        repository.save(dummy0);

        Dummy dummy1 = new Dummy();
        dummy1.setId(2L);
        dummy1.setData1("My data");
        dummy1.setData2("777-777-777-777");

        repository.save(dummy1);

        Dummy returnedObject = repository.getDummy(2L);

        System.out.println(returnedObject);


    }



}
