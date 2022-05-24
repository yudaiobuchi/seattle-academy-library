package jp.co.seattle.library.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.LendHistoryBookInfo;
import jp.co.seattle.library.rowMapper.LendHistoryBookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * rentalsテーブルに関する処理を実装する
 */

@Service
public class RentalBooksService {

	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * ①書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 貸出書籍ID
	 */
	public int getBookInfo(int bookId) {

		// rentalbooksにbooksと同じ本が入っているとき、＝本が借りられている時、rent_idを取得
		// JSPに渡すデータを設定する
		String sql = "SELECT rent_id FROM rentalbooks where rent_id =" + bookId;

		try {
			int rentId = jdbcTemplate.queryForObject(sql, Integer.class);
			return rentId;
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * 書籍をDBに登録する 書籍をrentalbooksに登録する rent_id,lend_date,return_dateを挿入
	 *
	 * @param bookId 書籍情報
	 */
	public void rentBook(int bookId) {

		String sql = "INSERT INTO rentalbooks (rent_id,lend_date,return_date) VALUES (" + bookId + ",now(),null);";

		jdbcTemplate.update(sql);

	}

	/**
	 * 返すボタンを押された時の処理 書籍をrentalbooksをには残して、return_dateにnowを入れたい。
	 *
	 * @param bookId 書籍情報
	 */
	public void returnBook(int bookId) {

		String sql = "update rentalbooks set lend_date = null ,return_date = now() where rentalbooks.rent_id = "
				+ bookId + ";";

		jdbcTemplate.update(sql);

	}

	/**
	 * 2回目以降の借りるボタンを押された時の処理 書籍をrentalbooksをには残して、return_dateにnowを入れたい。
	 *
	 * @param bookId 書籍情報
	 */
	public void rentbook2(int bookId) {

		String sql = "update rentalbooks set lend_date = now() ,return_date = null where rentalbooks.rent_id = "
				+ bookId + ";";

		jdbcTemplate.update(sql);

	}

	/*
	 * lendDateを取得するメソッド
	 * 
	 * @param bookId 書籍情報
	 */
	public Date getLendDateInfo(int bookId) {

		String sql = "SELECT lend_date FROM rentalbooks where rent_id =" + bookId;

		try {
			Date lendDate = jdbcTemplate.queryForObject(sql, Date.class);
			return lendDate;
		} catch (Exception e) {
			return null;
		}

	}

	/*
	 * タイトル、貸出日、返却日の複数のレコード取得
	 */
	public List<LendHistoryBookInfo> lendHistoryBookInfo() {

		// TODO 取得したい情報を取得するようにSQLを修正
		// データの取得と String sql = と実行文を１つにまとめたもの
		List<LendHistoryBookInfo> lendHistoryBookInfo = jdbcTemplate.query(
				"SELECT rent_id, title, lend_date, return_date FROM books LEFT OUTER JOIN rentalbooks ON books.id = rentalbooks.rent_id;",
				new LendHistoryBookInfoRowMapper());
		
		 return lendHistoryBookInfo;
	}


	/*
	 * rentalbooksから書籍を削除するメソッド
	 * 
	 * @param bookId 書籍情報
	 */
	public void rantalDeleteBook(int bookId) {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "DELETE FROM rentalbooks WHERE rent_id = " + bookId + ";";
		jdbcTemplate.update(sql);
	}

	
	
	
	/**
	 * 借りるボタンを押された時の処理
	 * 書籍をDBから削除する
	 * 書籍をrentalbooksから削除
	 * 
	 * @param bookId 書籍ID
	 */
//	public void deleteBook(int bookId) {
//		String sql = "DELETE FROM rentalbooks WHERE rent_id = " + bookId + ";";
//		jdbcTemplate.update(sql);
//	}
	

}
