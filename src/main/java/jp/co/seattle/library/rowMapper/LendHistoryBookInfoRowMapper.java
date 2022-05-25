package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.LendHistoryBookInfo;
@Configuration
public class LendHistoryBookInfoRowMapper implements RowMapper<LendHistoryBookInfo>{
	
	    @Override
	    public LendHistoryBookInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
	    
	    LendHistoryBookInfo lendHistoryBookInfo = new LendHistoryBookInfo();

	    lendHistoryBookInfo.setRentId(rs.getInt("rent_id"));
	    
	    lendHistoryBookInfo.setTitle(rs.getString("title"));
	    
	    lendHistoryBookInfo.setLendDate(rs.getDate("lend_date"));
	    
	    lendHistoryBookInfo.setReturnDate(rs.getDate("return_date"));
	    
	        return lendHistoryBookInfo;  

	    }
}
