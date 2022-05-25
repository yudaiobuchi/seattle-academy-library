package jp.co.seattle.library.dto;

import java.util.Date;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class LendHistoryBookInfo {
	
	    private int rentId;
	    
	    private String title;

	    private Date lendDate;
	    
	    private Date returnDate;
	    
	    public LendHistoryBookInfo() {
			
		}

	    public LendHistoryBookInfo(int rentId,String title,java.sql.Date lendDate,java.sql.Date returnDate) {
	    	
	        this.rentId = rentId;
	        
	        this.title = title;
	        
	        this.lendDate = lendDate;
	        
	        this.returnDate = returnDate;
	        
	        
	    }
}
