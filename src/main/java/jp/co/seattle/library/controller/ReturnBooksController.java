package jp.co.seattle.library.controller;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalBooksService;

@Controller
public class ReturnBooksController {
	
	@Autowired
	private RentalBooksService rentalBooksService;
	
	@Autowired
	private BooksService booksService;
	
	//map関連づけ

	@Transactional
	@RequestMapping(value = "/returnBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	
	public String rentalBook(Locale locale,@RequestParam("bookId") int bookId, Model model) {
		//本一冊（id）returnBookId
		int returnBookId = rentalBooksService.getBookInfo(bookId);
		Date lendDate = rentalBooksService.getLendDateInfo(bookId);
		
		if (returnBookId == 0 || lendDate == null) {
			//貸し出されてないメッセージ
			model.addAttribute("ErrorMessage", "貸し出されていません");
			
		} else {
			//カラムの値を更新　lend_dateをnullに
			rentalBooksService.returnBook(bookId);
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		
		return "details";
	}
	
}
