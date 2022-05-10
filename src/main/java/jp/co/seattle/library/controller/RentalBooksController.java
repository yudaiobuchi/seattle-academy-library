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
	//@RequestParam"bookId"→int bookId	=実際のIDの取得　
	public String rentalBook(Locale locale,@RequestParam("bookId") int bookId, Model model) {
		//書籍情報をrentalBookIdに代入
		//書籍情報　＝　rentalbooksService.rentBook(bookId);
		//rentBookで書籍の登録		
		//書籍が貸し出されていなかったら、rentalbooksに登録
		//書籍が貸し出されていたら、= すでにrentalbooksに入っていたら、貸出済メッセージ
		//model.addAttribute = 画面に表示
		//どちらの場合も、書籍情報を詳細画面に表示し、詳細画面に返す
		int rentalBookId = rentalbooksService.getBookInfo(bookId);
		
		if (rentalBookId == 0) {
			rentalbooksService.rentBook(bookId);
		} else {
			model.addAttribute("rentErrorMessage", "貸出済です");
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		
		return "details";
	}
}

