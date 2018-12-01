package memo.model;

public class MemoVO {
//	#DB 컬럼, form name에 맞춰서 변수 선언
//	>> 테이블컬럼명 == VO 변수명 == form name
	private int	 idx;        // 글번호(시퀀스로 입력)
	private String fk_userid;  // 회원아이디
	private String name;       // 작성자이름
	private String msg;        // 메모내용
	private String writedate;  // 작성일자
	private String cip;        // 클라이언트 IP 주소
	private int status;        // 글삭제유무 or 글 공개 유무(1: 공개/ 0: 비공개)
	  
	// 기본생성자가 있어야만 자바규격서에 따른 java bean으로 사용될 수 있다.
	// >> <jsp:useBean ~~ />
	public MemoVO() {
		  
	  }
	public MemoVO(int idx, String fk_userid, String name, String msg, String writedate, String cip, int status) {
		super();
		this.idx = idx;
		this.fk_userid = fk_userid;
		this.name = name;
		this.msg = msg;
		this.writedate = writedate;
		this.cip = cip;
		this.status = status;
	}
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getWritedate() {
		return writedate;
	}
	public void setWritedate(String writedate) {
		this.writedate = writedate;
	}
	public String getCip() {
		return cip;
	}
	public void setCip(String cip) {
		this.cip = cip;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	  
	
}
