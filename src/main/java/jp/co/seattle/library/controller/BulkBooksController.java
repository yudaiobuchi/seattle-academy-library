package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

@Controller
public class BulkBooksController {
	
	@Autowired
	private BooksService booksService;

	@RequestMapping(value = "/bulkBook", method = RequestMethod.GET)
	public String login(Model model) {
		return "bulkBook";
	}

	@Transactional
	@RequestMapping(value = "/bulkRegistBook", method = RequestMethod.POST) // value＝actionで指定したパラメータ
	// nameはRequestParam（"　"）に関連
	public String bulkBook(Locale locale, @RequestParam("csvFile") MultipartFile bulkfile, Model model) {
		
		// CSVファイルに入っている情報をbrへ格納
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(bulkfile.getInputStream(), StandardCharsets.UTF_8))) {
			// 変数lineを指定
			String line;
			//冊数のカウント変数
			int count = 0;
			//何番目にエラーが起きたかを記録する変数errorListを作成
			List<Integer> errorList = new ArrayList<Integer>();
			//StringのリストであるbookListを作成
			List<String[]> booksList = new ArrayList<String[]>();
			
			//データがない時{}の処理
			if(!br.ready()) {
				model.addAttribute("addErrorMessage","CSVに書籍情報がありません。");
				return "bulkBook";
			}
			
			// CSVファイルに入っている情報brを1行ずつ取り出して、変数lineに格納して、要素がなくなるまで（nullではなくなるまで）繰り返し処理
			while ((line = br.readLine()) != null) {
				count = count + 1;
				// 配列を保持できるsplitという変数に、CSVファイルに入っている情報を ,で要素を区切って格納
				// 文字列を、指定した文字列で分割した配列を返す。
				final String[] split = line.split(",", -1);
				//タイトル、著者、出版社のどれかの要素が入っていない、また、ISBNが入力されているかつ、10桁もしくは13桁ではない時、
				//カウントをerrorListに追加
				if (StringUtils.isEmpty(split[0]) || StringUtils.isEmpty(split[1]) || StringUtils.isEmpty(split[2])
						|| !(split[3].matches("^[0-9]{8}"))
						|| split[4].length() != 0 && !(split[4].matches("^[0-9]{10}|[0-9]{13}"))) {
					errorList.add(count);
				} else {
					//エラーがなければ一冊のデータの配列を変数bookListに設置
					booksList.add(split);
				}
				
			}
			
			//errorListの要素数が１つでもあった時
			if (errorList.size() > 0) {
				//addErrorMessageリストを作成
				List<String> addErrorMessage = new ArrayList<String>();
				//errorListの要素がある限りfor文処理
				for (int i = 0; i < errorList.size(); i++) {
					//errorListの要素（数字）をgetで取って、エラー文を作り、addErrorMessageリストにストック
					addErrorMessage.add(errorList.get(i) + "行目にエラーがあります。");
				}
				//"addErrorMessage"でjspにエラー文を表示
				model.addAttribute("addErrorMessage",addErrorMessage);
				//bulkBook.jspに返す
				return "bulkBook";
			}
			
			for (int i = 0; i < booksList.size(); i++) {
				String[] bookList = booksList.get(i);
				
				BookDetailsInfo bookInfo = new BookDetailsInfo();
				bookInfo.setTitle(bookList[0]);
				bookInfo.setAuthor(bookList[1]);
				bookInfo.setPublisher(bookList[2]);
				bookInfo.setPublishDate(bookList[3]);
				bookInfo.setISBN(bookList[4]);
				
				booksService.registBook(bookInfo);
				model.addAttribute("resultMessage","登録完了");
				
			}
			
		} catch (IOException e) {
			throw new RuntimeException("ファイルが読み込めません", e);
		}
		return "redirect:home";

	}

}
