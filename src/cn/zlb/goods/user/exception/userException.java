package cn.zlb.goods.user.exception;
/**
 * 自定义的用户异常 ，只需要从父类继承四个构造方法即可
 * @author Bingo
 *
 */
public class userException extends Exception {

	public userException() {
		super();
	}

	public userException(String message, Throwable cause) {
		super(message, cause);
	}

	public userException(String message) {
		super(message);
	}

	public userException(Throwable cause) {
		super(cause);
	}
	

}
