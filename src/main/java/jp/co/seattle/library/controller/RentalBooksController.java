package jp.co.seattle.library.controller;

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
public class RentalBooksController {
	//rentalbooksServiceのインスタンス化
	@Autowired
	private RentalBooksService rentalbooksService;
	
	@Autowired
	private BooksService booksService;
	
	@Transactional
	//全てボタンを押された瞬間の処理
	//value = "rentBook"で詳細画面の本の情報を全てget
	@RequestMapping(value = "/rentBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	//@RequestParamでname属性を取得
	//rentalBook作業スペースを作成
	//@RequestParam"bookId"でブラウザで入力されたデータをint型のbookIdに入れている。
	//@RequestParam"bookId"→int bookId
	//=実際のIDの取得　
	public String rentalBook(Locale locale,@RequestParam("bookId") int bookId, Model model) {
		//
		
		
		//書籍が貸し出されていなかったら、 = rentalsにその本の入っていなかったら　
		//→　詳細画面にある書籍をrentals登録、それを引っ張ってきて、画面に表示
		//rentBookで書籍の登録
		//
		//書籍が貸し出されていたら、= すでにrentalsに入っていたら、→ id = rentid → 貸出済
		//model.addAttribute = 画面に表示
		//書籍情報をrentalBookIdに代入
		//書籍情報　＝　rentalbooksService.rentBook(bookId);
		int rentalBookId = rentalbooksService.getBookInfo(bookId);
		
		if (rentalBookId == 0) {
			rentalbooksService.rentBook(bookId);
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
			
			return "details";
		} else {
			model.addAttribute("rentErrorMessage", "貸出済です");
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
			
			return "details";
		}
	}
}

