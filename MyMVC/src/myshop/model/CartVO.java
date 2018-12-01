package myshop.model;

public class CartVO {
	private int cartno;			// 장바구니 번호
	private String fk_userid;	// 사용자 ID
	private int fk_pnum;		// 제품번호
	private int oqty;			// 수량
	private int status;			// 장바구니에서 해당 물품 내역 삭제 여부
	
	private ProductVO item; 	// 제품정보 객체

	public CartVO() {}

	public CartVO(int cartno, String fk_userid, int fk_pnum, int oqty, int status, ProductVO item) {
		this.cartno = cartno;
		this.fk_userid = fk_userid;
		this.fk_pnum = fk_pnum;
		this.oqty = oqty;
		this.status = status;
		this.item = item;
	}

	public int getCartno() {
		return cartno;
	}

	public void setCartno(int cartno) {
		this.cartno = cartno;
	}

	public String getFk_userid() {
		return fk_userid;
	}

	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}

	public int getFk_pnum() {
		return fk_pnum;
	}

	public void setFk_pnum(int fk_pnum) {
		this.fk_pnum = fk_pnum;
	}

	public int getOqty() {
		return oqty;
	}

	public void setOqty(int oqty) {
		this.oqty = oqty;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ProductVO getItem() {
		return item;
	}

	public void setItem(ProductVO item) {
		this.item = item;
	}
	
	
	
}
