package jp.co.seattle.library.controller;

//外部packageの中のクラスのメソッドを使いたい時は、importする。
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

	// @Autowiredを使うと別クラスのメソッドを使うことができる。インスタンス化の簡略化みたいな感じ
	// 今回はBooksServiceクラスを使いたい。このクラス中で使えるようにするため、別名booksServiceと名付ける。
	@Autowired
	private BooksService booksService;

	// home.jspの<form action="">を@RequestMapping(value =
	// "/bulkBook")で受け取り、ここが処理が行われる場所となる。
	// public String login(Model model)
	// これはaddBooksControllerから別クラスに返す時に同じような処理を書いていたから書いた。
	// login(Model model)の意味はわからない。
	@RequestMapping(value = "/bulkBook", method = RequestMethod.GET)
	public String login(Model model) {
		return "bulkBook";
	}

	// bulkBook.jspの<form action="blukRegistBook">で処理先を指定されたものを@RequestMapping(value
	// = "/bulkRegistBook"で受け取り、ここが処理が行われる場所
	@Transactional
	@RequestMapping(value = "/bulkRegistBook", method = RequestMethod.POST) // value＝actionで指定したパラメータ
	// nameはRequestParam（" "）に関連
	public String bulkBook(Locale locale, @RequestParam("csvFile") MultipartFile bulkfile, Model model) {

		// CSVファイルに入っている情報をbrへ格納
		// try中で、書籍を一冊一冊取り出す処理（readLineメソッド）を使いたい。
		// readLineメソッドが書いている外部パッケージのBufferedReaderクラスを使いたいから引数にインスタンス化して記述
		// new InputStreamReader(bulkfile.getInputStream(), StandardCharsets.UTF_8)))
		// おそらくここで、CSVファイルを文字で扱えるようにしている
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(bulkfile.getInputStream(), StandardCharsets.UTF_8))) {
			// 変数lineを指定
			String line;
			// 冊数のカウント変数
			int count = 0;
			// 何番目にエラーが起きたかを記録する変数errorListを作成、リストだから何冊も記録されていく「1,5,8」みたいな感じ
			List<Integer> errorList = new ArrayList<Integer>();
			// StringのリストであるbookListを作成
			List<String[]> booksList = new ArrayList<String[]>();

			// brにデータがなければ、ifの中の処理
			// brにデータがセットされていなければ、bulkBook.jspにaddErrorMessageを渡して、"CSVに書籍情報がありません。"を表示
			// 画面を一括登録画面に戻す
			if (!br.ready()) {
				model.addAttribute("addErrorMessage", "CSVに書籍情報がありません。");
				return "bulkBook";
			}

			// CSVファイルに入っている情報readLineでbrを1行ずつ取り出して、変数lineに格納して、要素がなくなるまで（nullになるまで）繰り返し処理
			// while文
			// 一周目すぐにcount = 1
			while ((line = br.readLine()) != null) {
				count = count + 1;
				// 配列を保持できるsplitという変数に、CSVファイルに入っている一冊分の情報を ,で要素を区切って格納
				// 文字列を、指定した文字列で分割した配列を返す。
				final String[] split = line.split(",", -1);
				/*
				 * 
				 * if(条件) { //条件に合致した時の処理 } else { //それ以外の処理 } 一周目のsplit[0]とは、一冊目のタイトル
				 * 一周目のsplit[3]とは、一冊目の出版日 タイトル、著者、出版社のどれかの要素が入っていない、または、 出版日が半角数字8桁でない、または、
				 * ISBNが入力されているかつ、10桁もしくは13桁ではない時、（ISBNは任意だから） カウント(何冊目か int型)をerrorListに追加
				 */
				if (StringUtils.isEmpty(split[0]) || StringUtils.isEmpty(split[1]) || StringUtils.isEmpty(split[2])
						|| !(split[3].matches("^[0-9]{8}"))
						|| split[4].length() != 0 && !(split[4].matches("^[0-9]{10}|[0-9]{13}"))) {
					errorList.add(count);
				} else {
					// エラーがなければ一冊のデータの配列をbooksListリストに追加
					booksList.add(split);
				}

			}

			// errorListの要素数が１つでもあった時
			// .get(2)でそのリストの３番目の要素をgetする。
			if (errorList.size() > 0) {
				// addErrorMessageリストを作成
				List<String> addErrorMessage = new ArrayList<String>();
				// errorListの要素がある限りfor文処理
				for (int i = 0; i < errorList.size(); i++) {
					// errorListの要素をget、◯行目にエラーがありますというエラー文を作り、addErrorMessageリスト追加
					addErrorMessage.add(errorList.get(i) + "行目にエラーがあります。");
				}
				// jspに"addErrorMessage"を渡して、addErrorMessageを表示
				model.addAttribute("addErrorMessage", addErrorMessage);
				// bulkBook.jspに返す
				return "bulkBook";
			}

			// booksListの要素数が入っている限り、以下の処理
			// booksListのi番目（i冊目の本のデータ）をgetして、文字列型の配列であるbookListに代入
			// BookDetails型の変数bookInfoを作成 インスタンス化 〜型＝形式のルールを決定
			// 配列bookListの０番目（実際のタイトル）を変数bookInfoにセット
			// bookInfoには実際のデータがセットされた。BooksServiceの書籍登録メソッドregistBookメソッドに引数bookInfoを入れ、書籍登録
			// 後々どこかしらの画面に"登録完了"という文言を表示させるために、model.addAttributeに第一引数と第二引数を入れた。
			for (int i = 0; i < booksList.size(); i++) {
				String[] bookList = booksList.get(i);

				BookDetailsInfo bookInfo = new BookDetailsInfo();
				bookInfo.setTitle(bookList[0]);
				bookInfo.setAuthor(bookList[1]);
				bookInfo.setPublisher(bookList[2]);
				bookInfo.setPublishDate(bookList[3]);
				bookInfo.setISBN(bookList[4]);

				booksService.registBook(bookInfo);
				model.addAttribute("resultMessage", "登録完了");

			}
			// ファイルが読み込めない、ファイルが入っていない時にcatch以降の処理を実行
		} catch (IOException e) {
			throw new RuntimeException("ファイルが読み込めません", e);
		}
		// 一覧画面に遷移
		return "redirect:home";

	}

}
