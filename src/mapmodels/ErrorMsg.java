package mapmodels;

/**
 * This is the class used to return result of a method, 
 * if getResult() is false, getMsg() will include the error message.
 */
public class ErrorMsg {
	private int result;
	private String Msg;
		
	/**
	 * Constructor for the class ErrorMsg.
	 * @param result result of the method
	 * @param Msg error message if result is false
	 */
	public ErrorMsg(int result, String Msg){
		this.result = result;
		this.Msg = Msg;
	}
	
	/**
	 * Get the result of the called method.
	 * @return succeed or not
	 */
	public boolean isResult() {
		return result == 0;
	}
	
	/**
	 * Get the detail result of the called method, used in unitTest or other case when need error details.
	 * @return 0-succeed or error number greater than 0
	 */
	public int getResult() {
		return result;
	}	

	/**
	 * Get the error message if the result of the called method is false.
	 * @return error message or null if succeed
	 */
	public String getMsg() {
		return Msg;
	}


}
