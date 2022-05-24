package jp.co.seattle.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.RentalBooksService;

@Controller //APIの入り口
public class LendHistoryBooksController {
	
@Autowired
private RentalBooksService rentalBooksService;
   

	  @RequestMapping(value = "/lendHistoryBook", method = RequestMethod.GET) //value＝actionで指定したパラメータ
	    //RequestParamでname属性を取得
	    public String lendHistoryBook(Model model) {
		  
		  model.addAttribute("lendHistoryBookInfo",rentalBooksService.lendHistoryBookInfo());
		 
	        return "lendHistory";
	    }
}

