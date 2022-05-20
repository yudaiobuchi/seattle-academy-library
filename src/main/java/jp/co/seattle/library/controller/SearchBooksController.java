package jp.co.seattle.library.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

@Controller //APIの入り口
public class SearchBooksController {
	 @Autowired
	    private BooksService booksService;
	 
	 

	@RequestMapping(value = "/searchBook", method = RequestMethod.POST)
	 public String searchBooks(Locale locale,@RequestParam("search") String search, @RequestParam("radio") String radio,Model model) {
		
		if (radio.equals("部分一致")) {
			model.addAttribute("bookList",booksService.searchBookList(search));
		} else {
			model.addAttribute("bookList",booksService.AllSearchBookList(search));
		}
		return "home";
	}
	
	
}
