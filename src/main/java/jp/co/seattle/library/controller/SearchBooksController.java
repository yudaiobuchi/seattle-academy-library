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
	 public String searchBooks(Locale locale,@RequestParam("search") String search, Model model) {
		
		model.addAttribute("bookList",booksService.searchBookList(search));
		//booksService.searchBookList(search);
		//model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "home";
	}
	
	
}
