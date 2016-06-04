package com.expensetrakcer.cb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExpenseTrackerBoot {

	static final String DATE_FORMAT = "yyyyy-mm-dd";
	
	private static final Logger log = LoggerFactory.getLogger(ExpenseTrackerBoot.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerBoot.class);

	}

    @Bean
    public CommandLineRunner loadData(
            final ExpenseDetailsRepository repository) {
        return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				
				//repository.deleteAll();
				
			    //repository.save(new ExpenseDetail("Credit Card",  getFormattedDate("2016-03-12"), new Double(93.0), "JC Penny", "Memorial Day Shopping"));
			    //repository.save(new ExpenseDetail("Cash",  getFormattedDate("2016-04-21"), new Double(120.7), "Taj Grocers", "Grocery Shopping"));
			    //repository.save(new ExpenseDetail("Credit Card",  getFormattedDate("2016-05-09"), new Double(36), "Carters", "Kid's clothes Shopping"));
			    
			    //log.info("*****  Expense Details Saved   ****"); 
			    
			    /*for (ExpenseDetail ed : repository.findByTransactionDate(getFormattedDate("2016-03-12"))) {  
			    	log.info("********       Expense Details :=         "+ed);  
		        }*/			    
			    
			    for (ExpenseDetail ed : repository.findAll()) {  
			    	log.info("********       Expense Details :=         "+ed);  
		        }
			}
		};
    }	
	
    /**
     * 
     * @param date_s
     * @return
     * @throws ParseException
     */
    private Date getFormattedDate (String date_s) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    	Date newDate = sdf.parse(date_s); 
    	return newDate;
    }
	
}
