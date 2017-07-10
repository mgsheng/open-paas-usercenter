package cn.com.open.user.app.entiy;

public class Page{
	
	private int pageNumber;//第几页
	private int pageSize; //每页显示数
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
