package jenny.learn.springboot.excel.support;

public class ExcelException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExcelException(String message) {
		super(message);
	}
}
