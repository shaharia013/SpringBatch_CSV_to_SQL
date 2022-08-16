package com.shaharia.SpringBatch1_CSV_to_MySQL.Config;

import org.springframework.batch.item.ItemProcessor;
import com.shaharia.SpringBatch1_CSV_to_MySQL.Model.User;


public class Processor implements ItemProcessor<User, User> {

	@Override
	public User process(User item) throws Exception {
		
		return item;
	}
    
}
