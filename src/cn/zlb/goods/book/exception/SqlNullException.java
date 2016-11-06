package cn.zlb.goods.book.exception;

public class SqlNullException extends Exception {

	public SqlNullException() {
		super();
	}
 
	public SqlNullException(String message, Throwable cause) {
		super(message, cause);
	}

	public SqlNullException(String message) {
		super(message);
	}

	public SqlNullException(Throwable cause) {
		super(cause);
	}

}
