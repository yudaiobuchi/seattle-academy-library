package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.UsersService;

/**
 * ログインコントローラー
 */
@Controller /** APIの入り口 */
public class LoginController {
    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String first(Model model) {
        return "login"; //jspファイル名
    }

    /**
     * ログイン処理
     *
     * @param email メールアドレス
     * @param password パスワード
     * @param model
     * @return　ホーム画面に遷移
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        // TODO 下記のコメントアウトを外してサービスクラスを使用してください。
    	//UserInfo クラス　select
              UserInfo selectedUserInfo = usersService.selectUserInfo(email, password);

        // TODO パスワードとメールアドレスの組み合わせ存在チェック実装
        //protected Connection conn = null;
        //データが入ってきたらログイン可能、データが入らなかったら



}

        // 本の情報を取得して画面側に渡す
        model.addAttribute("bookList", booksService.getBookList());
        return "home";

    }
    
    //DBを取ってくる処理、照合
    //存在しないときにサーバー実行して、その文言にエラーの表示
    //ログインできた時に一蘭画面に遷移
    
    
}