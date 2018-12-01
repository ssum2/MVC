package memo.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.DataSource;

import jdbc.util.AES256;
import member.model.MemberVO;
import my.util.MyKey;

public class MemoDAO implements InterMemoDAO {
	
//	#아파치톰캣이 제공하는 DBCP 객체변수 ds 생성 
	private DataSource ds = null; // import javax.sql.DataSource
	
//	#Connection, preparedStatement, ResultSet객체 생성
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
//	#암호화
	AES256 aes = null;
	
//	#MemoDAO 생성자; DBCP객체 ds 얻어오기
	public MemoDAO() {
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

//	#사용한 자원을 반납하는 close() 메소드
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

//	#VO객체를 받아 테이블에 Insert하는 메소드
	@Override
	public int memoInsert(MemoVO memovo) throws SQLException {
		int result = 0;
		
		try {
		
	//		1) DB Connection Pool(ds)에서 Connection을 가져옴
	//			>> tomcat context.xml에 설정된 Connection객체를 빌려옴
			conn = ds.getConnection();
			
			String sql =" insert into jsp_memo(idx, fk_userid, name, msg, writedate, cip, status) "
					+ " values(jsp_memo_idx.nextval, ?, ?, ?, default, ?, default)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memovo.getFk_userid());
			pstmt.setString(2, memovo.getName());
			pstmt.setString(3, memovo.getMsg());
			pstmt.setString(4, memovo.getCip());
			
			result = pstmt.executeUpdate();
			
		} finally { // 자원반납
			close();
		}
		return result;
	}

	@Override
	public List<HashMap<String, String>> getAllMemo() throws SQLException {
		List<HashMap<String, String>> memoList = null;
		try {
			conn = ds.getConnection();
			
			String sql = "select idx, fk_userid, name, msg\n"+
					"        , to_char(writedate, 'yyyy-mm-dd hh24:mi:ss') as writedate\n"+
					"        , cip\n"+
					"from jsp_memo\n"+
					"where status = 1\n"+
					"order by idx desc";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				
				cnt++;
				
				// memoList 객체 생성
				if(cnt==1) {
					memoList = new ArrayList<HashMap<String, String>>();
				}
				
				// ResultSet에서 데이터 가져오기
				String idx = rs.getString("idx");
				String fk_userid = rs.getString("fk_userid");
				String name = rs.getString("name");
				String msg = rs.getString("msg");
				String writedate = rs.getString("writedate");
				String cip = rs.getString("cip");
				
				// HashMap생성 및 값 넣어주기
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("idx", idx);
				map.put("fk_userid", fk_userid);
				map.put("name", name);
				map.put("msg", msg);
				map.put("writedate", writedate);
				map.put("cip", cip);
				
				memoList.add(map);
			}
		} finally {
			close();
		}
		return memoList;
	}
//	#검색 및 날짜구간이 있는 총 메모 수 
	@Override
	public int getTotalCount(String searchType, String searchWord, int period) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			String sql = " select count(*) as CNT "+
						 " from jsp_memo "+
						 " where status=1 ";
	
			/*if(period == -1) { // period가 전체(-1)일 떄 sql구문 그대로 실행
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
			}*/
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
		
		} finally {
			close();	
		}
		return count;
	}
	
	@Override
	public List<HashMap<String, String>> getAllMemo(int sizePerPage, int currentShowPageNo, String searchType, String searchWord,
			int period) throws SQLException{
		List<HashMap<String, String>> memoList = null;
		try {
			conn = ds.getConnection();
			
			String sql = "select rno, idx, fk_userid, name, msg, writedate, cip\n"+
						"from\n"+
						"    (\n"+
						"    select rownum as rno, idx, fk_userid, name, msg, writedate, cip\n"+
						"    from\n"+
						"        (\n"+
						"        select idx, fk_userid, name, msg, to_char(writedate, 'yyyy-mm-dd hh24:mi:ss') as writedate, cip\n"+
						"        from jsp_memo\n"+
						"        where status=1 \n"+
						"        order by idx desc\n"+
						"        ) V\n"+
						"    )T\n"+
						"where T.rno between ? and ?";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, (currentShowPageNo*sizePerPage)-(sizePerPage -1));
			pstmt.setInt(2, (currentShowPageNo*sizePerPage));
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				// memoList 객체 생성
				if(cnt==1) {
					memoList = new ArrayList<HashMap<String, String>>();
				}
				
				// ResultSet에서 데이터 가져오기
				String idx = rs.getString("idx");
				String fk_userid = rs.getString("fk_userid");
				String name = rs.getString("name");
				String msg = rs.getString("msg");
				String writedate = rs.getString("writedate");
				String cip = rs.getString("cip");
				
				// HashMap생성 및 값 넣어주기
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("idx", idx);
				map.put("fk_userid", fk_userid);
				map.put("name", name);
				map.put("msg", msg);
				map.put("writedate", writedate);
				map.put("cip", cip);
				
				memoList.add(map);
			}
		} finally {
			close();
		}
		return memoList;
	}
	
//	#나의 메모 조회
//	1) 나의 전체 메모 갯수 조회
	@Override
	public int getTotalCount(String userid, String searchType, String searchWord, int period) throws SQLException {
		int count = 0;
		try {
			conn = ds.getConnection();
			String sql = " select count(*) as CNT "+
						 " from jsp_memo "+
						 " where fk_userid=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			rs.next();
			
			count = rs.getInt("CNT");
		
		} finally {
			close();	
		}
		return count;
	}

//	2) 나의 전체 메모 조회
	@Override
	public List<MemoVO> getAllMemo(String userid, int sizePerPage, int currentShowPageNo, String searchType, String searchWord,
			int period) throws SQLException{
		List<MemoVO> memoList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select rno, idx, fk_userid, name, msg, writedate, cip, status\n"+
						"from\n"+
						"    (\n"+
						"    select rownum as rno, idx, fk_userid, name, msg, writedate, cip, status\n"+
						"    from\n"+
						"        (\n"+
						"        select idx, fk_userid, name, msg, to_char(writedate, 'yyyy-mm-dd hh24:mi:ss') as writedate, cip, status\n"+
						"        from jsp_memo\n"+
						"        where fk_userid = ? "+
						"        order by idx desc\n"+
						"        ) V\n"+
						"    )T\n"+
						"where T.rno between ? and ?";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userid);
			pstmt.setInt(2, (currentShowPageNo*sizePerPage)-(sizePerPage -1));
			pstmt.setInt(3, (currentShowPageNo*sizePerPage));
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				// memoList 객체 생성
				if(cnt==1) {
					memoList = new ArrayList<MemoVO>();
				}
				
				// ResultSet에서 데이터 가져오기
				String str_idx = rs.getString("idx");
				int idx = Integer.parseInt(str_idx);
				String fk_userid = rs.getString("fk_userid");
				String name = rs.getString("name");
				String msg = rs.getString("msg");
				String writedate = rs.getString("writedate");
				String cip = rs.getString("cip");
				int status = rs.getInt("status");
				
				// memovo에 값 셋팅
				MemoVO memovo = new MemoVO();
				memovo.setIdx(idx);
				memovo.setFk_userid(fk_userid);
				memovo.setName(name);
				memovo.setMsg(msg);
				memovo.setWritedate(writedate);
				memovo.setCip(cip);
				memovo.setStatus(status);
				
				// memoList에 memovo 객체 삽입
				memoList.add(memovo);
			}
		} finally {
			close();
		}
		return memoList;
	}

//	#나의 메모 조회에서 비공개처리하기
	@Override
	public void memoBlind1(String[] delCheckArr) throws SQLException{
		try {
			conn = ds.getConnection();
			
			for(String idx:delCheckArr) {
				String sql =" update jsp_memo set status=0 "
							+ " where status=1 and idx = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, idx);
			}
			pstmt.executeUpdate();
			
		} finally {
			close();
		}
	}
	
//	#DB처리 한번에 하는 메모 공개처리 메소드
	@Override
	public void memoOpen(String[] delCheckArr) throws SQLException {
      try {
         conn = ds.getConnection();
         
         String sql = " update jsp_memo set status = 1 " + 
                  	" where idx in(";
         
         for(int i=0; i<delCheckArr.length; i++) {
            String comma = (i < delCheckArr.length-1)? ",": ") ";
            sql += '\''+delCheckArr[i]+'\''+comma;
         }
         
         System.out.println("sql " +sql);
         pstmt = conn.prepareStatement(sql);

         pstmt.executeUpdate();
         
      } finally {
         close();
      } 
   }
	
//	#DB처리 한번에 하는 메모 공개/비공개처리 메소드
	@Override
	public void memoOpenBlind(String[] delCheckArr, String status) throws SQLException {
      try {
         conn = ds.getConnection();
         
         String sql = " update jsp_memo set status = ? " + 
                  	" where idx in(";
         
         for(int i=0; i<delCheckArr.length; i++) {
            String comma = (i < delCheckArr.length-1)? ",": ") ";
            sql += '\''+delCheckArr[i]+'\''+comma;
         }
         
         System.out.println("sql " +sql);
         
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, status);
         pstmt.executeUpdate();
         
      } finally {
         close();
      } 
   }

	@Override
	public void memoDelete(String[] delCheckArr) throws SQLException {
		try {
	         conn = ds.getConnection();
	         
	         conn.setAutoCommit(false);
	         int a = 0;
	         int b = 0;
	         
	         String sql1 = "insert into jsp_memo_delete(idx, userid, name, msg, writedate, cip, status)"
	         				+ " select idx, fk_userid, name, msg, writedate, cip, status "
	         				+ " from jsp_memo "
	         				+ " where idx in(";
	         for(int i=0; i<delCheckArr.length; i++) {
		            String comma = (i < delCheckArr.length-1)? ",": ") ";
		            sql1 += '\''+delCheckArr[i]+'\''+comma;
		         }
	         System.out.println("sql1 " +sql1);
	         pstmt = conn.prepareStatement(sql1);

	         a = pstmt.executeUpdate();
	         
 
	        String sql2 = " delete from jsp_memo " + 
	                  		" where idx in(";
	         
	         for(int i=0; i<delCheckArr.length; i++) {
	            String comma = (i < delCheckArr.length-1)? ",": ") ";
	            sql2 += '\''+delCheckArr[i]+'\''+comma;
	         }
	         
	         System.out.println("sql2 " +sql2);
	         pstmt = conn.prepareStatement(sql2);

	         b = pstmt.executeUpdate();
	         
	         if(a+b==2) {
	        	 conn.commit();
	        	 conn.setAutoCommit(true);
	         }
	         else {
	        	 conn.rollback();
	         }
	         
	      } finally {
	         close();
	      } 
		
	}
} // end of class

