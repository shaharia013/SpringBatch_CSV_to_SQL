package com.shaharia.SpringBatch1_CSV_to_MySQL.Config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


import com.shaharia.SpringBatch1_CSV_to_MySQL.Model.User;


        // ************************  This is the main file where everything will be configured ********************  //

@Configuration

public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;  //for accessing the database
	
	
	
	// ******************* Reader **************************//
	
	@Bean
	 FlatFileItemReader<User> reader(){
		
	  FlatFileItemReader<User> read = new FlatFileItemReader<>();
	  
	  read.setResource(new ClassPathResource("User.csv")); // defining the csv file path
	  read.setLineMapper(getLineMapper());
	  read.setLinesToSkip(1); // If there is a error there function will skip a given argument line
	  
	   
	 return read;
		
	}

	private LineMapper<User> getLineMapper() {
		
		DefaultLineMapper<User> dfDefaultLineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(); // it will read the csv file and separate the file data based on the 
		                                                                                // given tokenizer for ex = for csv it will be comma
		
		delimitedLineTokenizer.setNames(new String[]{"id","name","sex","salary"}); // Column names
		delimitedLineTokenizer.setIncludedFields(new int[] {0,1,2,3}); // column numbers to be included
		
		BeanWrapperFieldSetMapper<User> fieldSetter = new BeanWrapperFieldSetMapper<>();
		// The above object will  read  the file by column and set the data in the class mentioned
		fieldSetter.setTargetType(User.class); // giving the  database class where data to be stored 
		
		// Adding linetokenixer and fildsetMapper to the lineMapper
		
		dfDefaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		dfDefaultLineMapper.setFieldSetMapper(fieldSetter);
		

		
		return dfDefaultLineMapper;
	}
	
	
	// *************************** Processor ***************************** // 
	
	@Bean
	 Processor processor() {
		return new Processor();
	}
	
	
	// ************************** Writer ******************************** //
	
	@Bean
	 JdbcBatchItemWriter<User> writer(){
		
		JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<>();
		
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
		// It is used to the set the property of the class as a parameter in the data base like id , name 
		
		writer.setSql("insert into user(id,name,sex,salary) values(:id , :name , :sex, :salary)"); // creating the table with column name and values
		writer.setDataSource(this.dataSource); // writing the data in the dataSource
		return writer;
	}
	
	
	
	// ********************** Creating Job ***************************** //
	
	@Bean
	 Job job() {
		
		return  this.jobBuilderFactory.get("USER-IMPORT-JOB")
				.incrementer(new RunIdIncrementer())   // increment it by Id
				.flow(step1())    // if there is a single step then use start() other wise flow() and with flow we need to use end()       
				.end()
				.build();
		
	}

	@Bean
	 Step step1() {
		
		return this.stepBuilderFactory.get("Step - 1")
				.<User,User> chunk(10)//chunk() is used to tell how much data to be read write and process at the same time
				.reader(reader())
				.processor(processor())      // A step consist of read ,  process and write 
				.writer(writer())
				.build();
	}
}
