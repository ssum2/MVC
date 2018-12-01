package member.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jdbc.util.AES256;
import jdbc.util.SHA256;
import my.util.MyKey;

public class MemberDAO implements InterMemberDAO {
//	#아파치톰캣이 제공하는 DBCP 객체변수 ds 생성 
	private DataSource ds = null; // import javax.sql.DataSource
	
//	#Connection, preparedStatement, ResultSet객체 생성
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
//	#암호화
	AES256 aes = null;

//	#MemberDAO 기본생성자
	public MemberDAO() {
		try {
			Context initContext = new InitialContext();	//	import javax.naming.*;
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/myoracle");
			
			String key =MyKey.key;
			aes = new AES256(key);
			
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println(">>> key값은 17자 이상이어야 합니다.");
			e.printStackTrace();
		} 
	} // end of default constructor
	
	//#사용한 자원을 반납하는 close() 메소드
	public void close() {
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
			if(pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	} // end of close()

//	#로그인 메소드(로그인 성공시 MemberVO객체 반환)
	@Override
	public MemberVO loginOKmemberInfo(String userid, String pwd) throws SQLException {
		MemberVO membervo = null;
		
		try {
//			1) DB Connection Pool(ds)에서 Connection을 가져옴
//			>> tomcat context.xml에 설정된 Connection객체를 빌려옴
			conn = ds.getConnection();
			
			conn.setAutoCommit(false);
			
			String sql = " select idx, userid, name, coin, point "+
						"        , trunc( months_between(sysdate, lastpwdchangedate) ) as pwdchangegap "+
						"        , trunc( months_between(sysdate, lastlogindate) ) as lastlogingap "+
						" from jsp_member "+
						" where userid = ? and pwd = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setString(2, SHA256.encrypt(pwd));
			
			rs = pstmt.executeQuery();
			
			boolean bool = rs.next();

			
			if(bool) {
				// select된 회원이 존재하는 경우
				int idx = rs.getInt("idx");
				String v_userid = rs.getString("userid");
				String name = rs.getString("name");
				int coin = rs.getInt("coin");
				int point = rs.getInt("point");
				int pwdchangegap = rs.getInt("pwdchangegap");
				int lastlogingap = rs.getInt("lastlogingap");
				
				membervo = new MemberVO();
				membervo.setIdx(idx);
				membervo.setUserid(v_userid);
				membervo.setName(name);
				membervo.setCoin(coin);
				membervo.setPoint(point);
				
				if(pwdchangegap >= 6) {
					membervo.setRequirePwdChange(true);
				}
				if(lastlogingap>=12) { // 휴면계정일 때
					membervo.setRequireCertify(true);
				}
				else {
	//				#마지막 로그인 한 일시 기록
					sql = " update jsp_member set lastlogindate=sysdate "
					+ "where userid = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, userid);
					pstmt.executeUpdate();
				}
			}
			else {
				// 회원이 존재하지 않는 경우 or status가 0인 회원
			}
		} finally {
			close();
		}
		
		return membervo;
	}

	
//	#회원가입 메소드
	@Override
	public int registerMember(MemberVO membervo) throws SQLException{
//				>> 호출하는 곳에서 처리하면서 어떤 부분이 잘못됐는지 한번에 보기 위해서 throws
		
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = " insert into jsp_member (IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY, COIN, POINT, REGISTERDAY, STATUS)\n "+
						 " values(seq_jsp_member.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, default, default, default, default) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, membervo.getUserid());
			pstmt.setString(2, membervo.getName());
			pstmt.setString(3, SHA256.encrypt(membervo.getPwd()));	// SHA256 단방향 암호화
			pstmt.setString(4, aes.encrypt(membervo.getEmail()));	// AES256 양방향 암호화
			pstmt.setString(5, membervo.getHp1());
			pstmt.setString(6, aes.encrypt(membervo.getHp2()));		// AES256 양방향 암호화
			pstmt.setString(7, aes.encrypt(membervo.getHp3()));		// AES256 양방향 암호화
			pstmt.setString(8, membervo.getPost1());
			pstmt.setString(9, membervo.getPost2());
			pstmt.setString(10, membervo.getAddr1());
			pstmt.setString(11, membervo.getAddr2());
			pstmt.setString(12, membervo.getGender());
			pstmt.setString(13, membervo.getBirthday());	
			result = pstmt.executeUpdate();
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of registerMember()

	
//	#아이디 중복검사 메소드
	@Override
	public boolean idDuplicateCheck(String userid) throws SQLException {
		
		try {
			conn = ds.getConnection();
			String sql = " select count(*) AS CNT "+
						 " from jsp_member "+
						 " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			rs.next();
			
			int cnt = rs.getInt("CNT");
			if(cnt==1) {	// 입력한 아이디와 일치하는 기존아이디가 있는 경우
				return false;
			}
			else {
				return true;
			}
			
		}  finally {
			close();	
		}
	
	}
	
//	#검색 및 날짜구간이 있는 총 회원수 구하는 메소드
	@Override
	public int getTotalCount(String searchType, String searchWord, int period) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			String sql = " select count(*) as CNT "+
						 " from jsp_member "+
						 " where 1=1 ";
		
			if("email".equals(searchType)) {
				searchWord = aes.encrypt(searchWord);
			}
			
			if(period == -1) { // period가 전체(-1)일 떄 sql구문 그대로 실행
				sql+=" and "+ searchType + " like '%'||?||'%' ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
			}
			else { // period가 전체(-1)설정이 아닐 때 기존 sql구문에 조건절 추가
				
				sql += " and "+ searchType + " like '%'||?||'%' "
						+"and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd')-to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, period);
			}
	
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
		
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {

		} finally {
			close();	
		}
		return count;
	}
	
//	#검색 및 날짜구간이 있고, 일반회원이 볼 수 있는 총 회원수 구하기
	@Override
	public int getTotalCountMember(String searchType, String searchWord, int period) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			String sql = " select count(*) as CNT "+
						 " from jsp_member "+
						 " where status=1 "
						 + "and  MONTHS_BETWEEN (add_months(sysdate, -6), lastlogindate ) < 12 ";
		
			if("email".equals(searchType)) {
				searchWord = aes.encrypt(searchWord);
			}
			
			if(period == -1) { // period가 전체(-1)일 떄 sql구문 그대로 실행
				sql+=" and "+ searchType + " like '%'||?||'%' ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
			}
			else { // period가 전체(-1)설정이 아닐 때 기존 sql구문에 조건절 추가
				
				sql += " and "+ searchType + " like '%'||?||'%' "
						+"and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd')-to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, period);
			}
	
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
		
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {

		} finally {
			close();	
		}
		return count;
	}
	
//	#페이징 처리가 완료된 상태에서 날짜구간기능까지 포함하여 memberList에 전체회원을 넣어주는 메소드
	@Override
	public List<MemberVO> getAllMember(int sizePerPage, int currentShowPageNo, String searchType, String searchWord,int period) throws SQLException {
		List<MemberVO> memberList = null;
		
		try {
			conn = ds.getConnection();
			String sql = "select   RNO,\n"+
					"              IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, \n"+
					"              POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY,\n"+
					"              COIN, POINT, REGISTERDAY, STATUS, lastlogindate, lastPwdChangeDate,  lastlogingap "+
					"from\n"+
					"(\n"+
					"    select rownum AS RNO,\n"+
					"              IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, \n"+
					"              POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY,\n"+
					"              COIN, POINT, REGISTERDAY, STATUS, lastlogindate, lastPwdChangeDate, lastlogingap\n"+
					"    from\n"+
					"        (\n"+
					"        select IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, \n"+
					"                  POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY,\n"+
					"                  COIN, POINT, to_char(REGISTERDAY, 'yyyy-mm-dd') as REGISTERDAY, STATUS "+
					"				, lastlogindate, lastPwdChangeDate" +
					"				, trunc( months_between(sysdate, lastlogindate) ) as lastlogingap "+
					"        from jsp_member\n"+
					"        where 1=1 "+
							" and " + searchType + " like '%'||?||'%' ";

			String sql2 =	"        order by idx desc\n"+
							"        ) V\n"+
							") T\n"+
							"where T.RNO between ? and ?  ";  
			
			if("email".equals(searchType)) {
				searchWord = aes.encrypt(searchWord);
			}
			
			if(period == -1) {
				sql += sql2;
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage -1));
				pstmt.setInt(3, (currentShowPageNo*sizePerPage));
				
			}
			else {
				sql += 	" and to_date(to_char(sysdate, 'yyyy-mm-dd'), 'yyyy-mm-dd')-to_date(to_char(registerday, 'yyyy-mm-dd'), 'yyyy-mm-dd') <= ? "+
						sql2;
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, searchWord);
				pstmt.setInt(2, period);
				pstmt.setInt(3, (currentShowPageNo*sizePerPage)-(sizePerPage -1));
				pstmt.setInt(4, (currentShowPageNo*sizePerPage));
			}

			rs = pstmt.executeQuery();
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt==1)
					memberList = new ArrayList<MemberVO>();
				
				int idx = rs.getInt("IDX");
				String userid = rs.getString("USERID");
				String name = rs.getString("NAME");
				String pwd = rs.getString("PWD");
				String email = aes.decrypt(rs.getString("EMAIL"));  // AES256 복호화	
				
				String hp1 = rs.getString("HP1");
				String hp2 = aes.decrypt(rs.getString("HP2"));	// AES256 복호화
				String hp3 = aes.decrypt(rs.getString("HP3"));	// AES256 복호화
						
				String post1 = rs.getString("POST1");
				String post2 = rs.getString("POST2");
				String addr1 = rs.getString("ADDR1");
				String addr2 = rs.getString("ADDR2");
				String gender = rs.getString("GENDER");
				String birthday = rs.getString("BIRTHDAY");
				int coin = rs.getInt("COIN");
				int point = rs.getInt("POINT");
				String registerday = rs.getString("REGISTERDAY");
				int status = rs.getInt("STATUS");
				int lastlogingap = rs.getInt("lastlogingap");
				String lastlogindate = rs.getString("lastlogindate");
				String lastPwdChangeDate = rs.getString("lastPwdChangeDate");
				
				boolean requireCertify = false;
				if(lastlogingap>=12) { // 휴면계정일 때
					requireCertify = true;
				}
				// MemberVO 생성자; VO객체를 만들어서 회원정보를 담아줌
				MemberVO membervo = new MemberVO(idx, userid, name, pwd, email, hp1, hp2, hp3,
						post1, post2, addr1, addr2, gender,
						birthday.substring(0, 4), birthday.substring(4, 6), birthday.substring(6), birthday,
						coin, point, registerday, status, requireCertify, lastlogindate, lastPwdChangeDate) ;
				// 가입된 회원수 만큼 객체가 리스트업
				memberList.add(membervo);
			} // end of while
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}  finally {
			close();	
		}
		return memberList;
	}
	
//	#회원 상세정보를 보여주는 메소드	
	public MemberVO getOneMember(int idx) throws SQLException {
		MemberVO membervo = null;
		try {
			conn = ds.getConnection();
			String sql = " select IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, "+
						 " POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY, "+
					     " COIN, POINT, to_char(REGISTERDAY, 'yyyy-mm-dd') as REGISTERDAY, STATUS,"
					     + "to_char(lastlogindate, 'yyyy-mm-dd') as lastlogindate , to_char(lastPwdChangeDate, 'yyyy-mm-dd') as lastPwdChangeDate "+
					     " from jsp_member "+
					     " where status = 1 "+
					     " and idx = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int idx2 = rs.getInt("IDX");
				String userid = rs.getString("USERID");
				String name = rs.getString("NAME");
				
//				String pwd = rs.getString("PWD");
				String email = aes.decrypt(rs.getString("EMAIL"));  // AES256 복호화	
				
				String hp1 = rs.getString("HP1");
				String hp2 = aes.decrypt(rs.getString("HP2"));	// AES256 복호화
				String hp3 = aes.decrypt(rs.getString("HP3"));	// AES256 복호화
						
				String post1 = rs.getString("POST1");
				String post2 = rs.getString("POST2");
				String addr1 = rs.getString("ADDR1");
				String addr2 = rs.getString("ADDR2");
				String gender = rs.getString("GENDER");
				String birthday = rs.getString("BIRTHDAY");
				int coin = rs.getInt("COIN");
				int point = rs.getInt("POINT");
				String registerday = rs.getString("REGISTERDAY");
				int status = rs.getInt("STATUS");
				String lastlogindate = rs.getString("lastlogindate");
				String lastPwdChangeDate = rs.getString("lastPwdChangeDate");

				membervo = new MemberVO();
				
				membervo.setIdx(idx2);
				membervo.setName(name);
				membervo.setUserid(userid);
				membervo.setEmail(email);
				membervo.setHp1(hp1);
				membervo.setHp2(hp2);
				membervo.setHp3(hp3);
				membervo.setPost1(post1);
				membervo.setPost2(post2);
				membervo.setAddr1(addr1);
				membervo.setAddr2(addr2);
				membervo.setGender(gender);
				membervo.setBirthday(birthday);
				membervo.setCoin(coin);
				membervo.setPoint(point);
				membervo.setRegisterday(registerday);
				membervo.setStatus(status);
				membervo.setLastlogindate(lastlogindate);
				membervo.setLastpwdchangedate(lastPwdChangeDate);
			}
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}  finally {
			close();	
		}
		return membervo;
	} 
	

//	#회원 정보 수정 메소드
	@Override
	public int updateMember(MemberVO membervo) throws SQLException{
//											>> 호출하는 곳에서 처리하면서 어떤 부분이 잘못됐는지 한번에 보기 위해서 throws
		int result = 0;
		System.out.println("updateMember 1/4 success");
		try {
			conn = ds.getConnection();
			String sql = " update jsp_member set name= ?, pwd=?, email=?, hp1=?, hp2=?, hp3=?, "+
						 " post1=?, post2=?, addr1=?, addr2=?, lastpwdchangedate=sysdate"+
						 " where idx = ? ";
			
			pstmt = conn.prepareStatement(sql);
			System.out.println("updateMember 2/4 success");
			
			pstmt.setString(1, membervo.getName());
			pstmt.setString(2, SHA256.encrypt(membervo.getPwd()));	// SHA256 단방향 암호화
			pstmt.setString(3, aes.encrypt(membervo.getEmail()));	// AES256 양방향 암호화
			pstmt.setString(4, membervo.getHp1());
			pstmt.setString(5, aes.encrypt(membervo.getHp2()));		// AES256 양방향 암호화
			pstmt.setString(6, aes.encrypt(membervo.getHp3()));		// AES256 양방향 암호화
			pstmt.setString(7, membervo.getPost1());
			pstmt.setString(8, membervo.getPost2());
			pstmt.setString(9, membervo.getAddr1());
			pstmt.setString(10, membervo.getAddr2());
			pstmt.setInt(11, membervo.getIdx());
			System.out.println("updateMember 3/4 success");
			
			result = pstmt.executeUpdate();
			System.out.println("updateMember 4/4 success");
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	} // end of updateMember(MemberVO)

	
//	#회원 삭제 메소드(status값 변경)
	@Override
	public int deleteMember(int idx) throws SQLException{
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql1 = " select count(*) as CNT "
					+ " from jsp_member"
					+ " where status=1 and idx=? ";
			
			pstmt = conn.prepareStatement(sql1);
			
			pstmt.setInt(1, idx);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String sql2 = " update jsp_member set status=0 "+
						 " where idx = ? and status=1 ";
			
				pstmt = conn.prepareStatement(sql2);
	
				pstmt.setInt(1, idx);
			
				result = pstmt.executeUpdate();
			}
			else {
				result = 0;
			}
			
		} finally {
			close();
		}
		return result;
	} // end of deleteMember(int idx)
	
//	#회원 복원 메소드(status값 변경)
	@Override
	public int recoveryMember(int idx) throws SQLException{
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql1 = " select count(*) as CNT "
					+ " from jsp_member"
					+ " where status=0 and idx=? ";
			
			pstmt = conn.prepareStatement(sql1);
			
			pstmt.setInt(1, idx);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String sql2 = " update jsp_member set status=1 "+
						 " where idx = ?";
			
				pstmt = conn.prepareStatement(sql2);
	
				pstmt.setInt(1, idx);
			
				result = pstmt.executeUpdate();
			}
			else {
				result = 0;
			}
			
		} finally {
			close();
		}
		return result;
	}// end of recoveryMember(int idx)
	
//	#회원 활성화, 비활성화 변경 메소드
	@Override
	public int updateMemberToEnable(int idx) throws SQLException{
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql1 = " select count(*) as CNT "
					+ " from jsp_member"
					+ " where status=1 and idx=? ";
			
			pstmt = conn.prepareStatement(sql1);
			
			pstmt.setInt(1, idx);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String sql2 = " update jsp_member set lastlogindate=sysdate "+
						 	  " where idx = ? and status=1 ";
			
				pstmt = conn.prepareStatement(sql2);
	
				pstmt.setInt(1, idx);
			
				result = pstmt.executeUpdate();
			}
			else {
				result = 0;
			}
			
		} finally {
			close();
		}
		return result;
	} // end of updateMemberToEnable(int idx)	

//	#아이디 찾기 메소드
	@Override
	public String findUserid(String name, String mobile) throws SQLException {
		String userid = null;
		try {
			conn = ds.getConnection();
			
//			012 3456 7890,  012 345 6789 01078974565
			String hp2 = "";
			String hp3 = "";
			String mobilenumber = mobile.substring(0, 3);
			if(mobile.length()==11) {
				hp2 = aes.encrypt(mobile.substring(3, 7));
				hp3 = aes.encrypt(mobile.substring(7));
				mobilenumber += hp2+hp3;
			}
			else if(mobile.length()==10) {
				hp2 = aes.encrypt(mobile.substring(3, 6));
				hp3 = aes.encrypt(mobile.substring(6));
				mobilenumber += hp2+hp3;
			}
			
			String sql = " select userid "
					+ " from jsp_member"
//					+ " where status=1 and name=? and hp1=? and hp2=? and hp3=? ";
					+ " where status=1 and name=? and trim(hp1) || trim(hp2) || trim(hp3) = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, mobilenumber);
	
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				userid = rs.getString("userid");
			}
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}	
		
		return userid;
	}

//	#회원 인증용; 패스워드 찾기
//	 유효한 이메일이고 인증메일이 발송되면 1, 메일 발송 실패시 -1 리턴
	@Override
	public int isUserExists(String userid, String email) throws SQLException{
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select count(*) as CNT "
					+ " from jsp_member"
					+ " where status=1 and userid=? and email=? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			pstmt.setString(2, aes.encrypt(email));
	
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				n=rs.getInt("CNT");
			}
			
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}	

		return n;
	}

	
//	#새로운 비밀번호로 변경하는 메소드 (비밀번호 찾기)
	@Override
	public int changeNewPwd(String userid, String pwd) throws SQLException{
		int n = 0;
		try {
			conn = ds.getConnection();
			String sql = " update jsp_member set pwd=?, lastpwdchangedate=sysdate"+
					 	  " where userid = ? and status=1 ";
		
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, SHA256.encrypt(pwd));
			pstmt.setString(2, userid);
			n = pstmt.executeUpdate();

		} finally {
			close();
		}
		
		return n;
	}

	
//	#코인충전결제 완료 후 해당 회원의 코인 및 포인트를 update 해주는 메소드
	@Override
	public int coinAddUpdate(int idx, int coinmoney) throws SQLException{
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = " update jsp_member set coin=coin+?, point=point+?"+
					 	  " where idx = ? and status=1 ";
		
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, coinmoney);
			pstmt.setInt(2, coinmoney/100);
			pstmt.setInt(3, idx);
			result = pstmt.executeUpdate();

		} finally {
			close();
		}
		
		return result;
	}
	

	
}