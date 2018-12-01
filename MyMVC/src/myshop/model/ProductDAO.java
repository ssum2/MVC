package myshop.model;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.DataSource;

import jdbc.util.AES256;
import member.model.MemberVO;
import my.util.MyKey;

/* === 아파치톰캣이 제공하는 DBCP(DB Connection Pool)를 이용하여  
       ProductDAO 클래스를 생성한다. ===  */

public class ProductDAO implements InterProductDAO {
 
	private DataSource ds = null;
	// 객체변수 ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool) 이다.
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	AES256 aes = null;
	
	/*  === ProductDAO 생성자에서 해야할 일은 ===
	      아파치톰캣이 제공하는 DBCP(DB Connection Pool) 객체인 ds 를 얻어오는 것이다.   
	*/
	
	public ProductDAO() {
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env"); 
			ds = (DataSource)envContext.lookup("jdbc/myoracle");
			
			String key = MyKey.key;  // 암호화, 복호화 키 
			aes = new AES256(key);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("key 값은 17글자 이상이어야 합니다.");
			e.printStackTrace();
		}
	}// end of ProductDAO() 생성자-------------------------
	
	
	// === 사용한 자원을 반납하는 close() 메소드 생성하기 === //
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
	}// end of void close()-------------------


//	#jsp_product 테이블에서 pspec 컬럼의 값(HIT, NEW, BEST)별로 상품 목록을 가져오는 메소드 생성하기 *** // 
	@Override
	public List<ProductVO> selectByPspec(String pspec) 
		throws SQLException {
		
		List<ProductVO> productList = null;
		
		try {
			 conn = ds.getConnection();

			 String sql = "select pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, pspec, pcontent, point\n"+
					 "     , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate\n"+
					 "from jsp_product\n"+
					 "where pspec = ? \n"+
					 "order by pnum desc";
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, pspec);
			 
			 rs = pstmt.executeQuery();
			 
			 int cnt = 0;
			 while(rs.next()) {
				 cnt++;
				 
				 if(cnt==1) {
					 productList = new ArrayList<ProductVO>();
				 }
				 
				 int pnum = rs.getInt("pnum");
				 String pname = rs.getString("pname");
				 String pcategory_fk = rs.getString("pcategory_fk");
				 String pcompany = rs.getString("pcompany");
				 String pimage1 = rs.getString("pimage1");
				 String pimage2 = rs.getString("pimage2");
				 int pqty = rs.getInt("pqty");
				 int price = rs.getInt("price");
				 int saleprice = rs.getInt("saleprice");
				 String v_pspec = rs.getString("pspec");
				 String pcontent = rs.getString("pcontent");
				 int point = rs.getInt("point");
				 String pinputdate = rs.getString("pinputdate");
				 
				 ProductVO productvo = new ProductVO(pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, v_pspec, pcontent, point, pinputdate); 
				 
				 productList.add(productvo);
				 
			 }// end of while-----------------------
			 
		} finally {
			close();
		}
		 
		return productList;
	}// end of List<ProductVO> selectByPspec(String pspec)-------------------

//	#pnum으로 제품 1개의 정보를 가져오는 메소드
	@Override
	public ProductVO getProductOneByPnum(int pnum) throws SQLException {
		ProductVO pvo = null;
		try {
			 conn = ds.getConnection();

			 String sql = "select pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, pspec, pcontent, point\n"+
					 "     , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate\n"+
					 "from jsp_product\n"+
					 "where pnum = ? \n"+
					 "order by pnum desc";
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setInt(1, pnum);
			 
			 rs = pstmt.executeQuery();
			 
			 boolean bool = rs.next();
			 
			 if(bool) {
				 pnum = rs.getInt("pnum");
				 String pname = rs.getString("pname");
				 String pcategory_fk = rs.getString("pcategory_fk");
				 String pcompany = rs.getString("pcompany");
				 String pimage1 = rs.getString("pimage1");
				 String pimage2 = rs.getString("pimage2");
				 int pqty = rs.getInt("pqty");
				 int price = rs.getInt("price");
				 int saleprice = rs.getInt("saleprice");
				 String v_pspec = rs.getString("pspec");
				 String pcontent = rs.getString("pcontent");
				 int point = rs.getInt("point");
				 String pinputdate = rs.getString("pinputdate");
	
				 pvo = new ProductVO(pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, v_pspec, pcontent, point, pinputdate); 
			 }
			 
		} finally {
			close();
		}
		return pvo;
	}

//	#카테고리 정보를 가져오는 메소드
	@Override
	public List<HashMap<String, String>> selectCategory() throws SQLException  {
		List<HashMap<String, String>> categoryList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select cnum, code, cname\n"+
					"from jsp_category"
					+ " order by cnum asc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1) {
					categoryList = new ArrayList<HashMap<String, String>>();
				}
				
				int cnum = rs.getInt("cnum");
				String str_cnum = String.valueOf(cnum);
				String code = rs.getString("code");
				String cname = rs.getString("cname");
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("cnum", str_cnum);
				map.put("code", code);
				map.put("cname", cname);
				
				categoryList.add(map);
			}

		} finally {
			close();
		}
		return categoryList;
	}
	
//	#스펙 정보를 가져오는 메소드
	@Override
	public List<HashMap<String, String>> selectSpec() throws SQLException  {
		List<HashMap<String, String>> specList = null;
		try {
			conn = ds.getConnection();
			
			String sql = "select snum, sname\n"+
						"from jsp_spec";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1) {
					specList = new ArrayList<HashMap<String, String>>();
				}
				
				int snum = rs.getInt("snum");
				String str_snum = String.valueOf(snum);
				String sname = rs.getString("sname");
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("snum", str_snum);
				map.put("sname", sname);
				
				specList.add(map);
			}

		} finally {
			close();
		}
		return specList;
	}

//	#제품번호(시퀀스) 채번 하는 메소드
	@Override
	public int getPnumOfProduct() throws SQLException {
		int pnum = 0;
		
		try {
			conn = ds.getConnection();
			String sql = " select seq_jsp_product_pnum.nextval as seq"
						+ " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			
			pnum = rs.getInt("seq");
			
		} finally {
			close();
		}
		return pnum;
	}


//	#제품등록 insert 메소드
	@Override
	public int productInsert(ProductVO pvo) throws SQLException {
		int n = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = "insert into jsp_product(pnum, pname, pcategory_fk, pcompany, \n"+
					"                        pimage1, pimage2, pqty, price, saleprice,\n"+
					"                        pspec, pcontent, point)\n"+
					"values(?, ?, ?, ?,\n"+
					"       ?, ?,\n"+
					"       ?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, pvo.getPnum());
			pstmt.setString(2, pvo.getPname());
			pstmt.setString(3, pvo.getPcategory_fk());
			pstmt.setString(4, pvo.getPcompany());
			pstmt.setString(5, pvo.getPimage1());
			pstmt.setString(6, pvo.getPimage2());
			pstmt.setInt(7, pvo.getPqty());
			pstmt.setInt(8, pvo.getPrice());
			pstmt.setInt(9, pvo.getSaleprice());
			pstmt.setString(10, pvo.getPspec());
			pstmt.setString(11, pvo.getPcontent());
			pstmt.setInt(12, pvo.getPoint());
			
			n = pstmt.executeUpdate();

		} finally {
			close();
		}
		return n;
	}

//	#제품 이미지정보를 jsp_product_imagefile 테이블에 insert하는 메소드
	@Override
	public int product_imagefile_Insert(int pnum, String attachFilename) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			
			String sql = "insert into jsp_product_imagefile(imgfileno, fk_pnum, imgfilename)\n"+
						"values(seq_imgfileno.nextval, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, pnum);
			pstmt.setString(2, attachFilename);

			result = pstmt.executeUpdate();

		} finally {
			close();
		}
		return result;
	}

//	#추가 첨부 이미지파일 이름 가져오는 메소드
	@Override
	public List<HashMap<String, String>> selectAttachImage(int pnum) throws SQLException {
		List<HashMap<String, String>> attachImageList = null;
		try {
			conn = ds.getConnection();
			
			String sql = " select imgfileno, fk_pnum, imgfilename "+
						" from jsp_product_imagefile "
						+ " where fk_pnum=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pnum);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1) {
					attachImageList = new ArrayList<HashMap<String, String>>();
				}
				
				int imgfileno = rs.getInt("imgfileno");
				String str_imgfileno = String.valueOf(imgfileno);
				int fk_pnum = rs.getInt("fk_pnum");
				String str_fk_pnum = String.valueOf(fk_pnum);
				String imgfilename = rs.getString("imgfilename");
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("imgfileno", str_imgfileno);
				map.put("fk_pnum", str_fk_pnum);
				map.put("imgfilename", imgfilename);
				
				attachImageList.add(map);
			}

		} finally {
			close();
		}

		return attachImageList;
	}

//	#카테고리리스트 가져오는 메소드(로그인폼 밑  출력용)
	@Override
	public List<CategoryVO> getCategoryList() throws SQLException {
		List<CategoryVO> categoryList = null;
		try {
			conn = ds.getConnection();
			
			String sql = " select cnum, code, cname\n"+
					" from jsp_category "
					+ " order by cnum asc ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1) {
					categoryList = new ArrayList<CategoryVO>();
				}
				
				int cnum = rs.getInt("cnum");
				String code = rs.getString("code");
				String cname = rs.getString("cname");
				
				CategoryVO cvo = new CategoryVO(cnum, code, cname);
				
				categoryList.add(cvo);
			}

		} finally {
			close();
		}
		return categoryList;
	}

//	#카테고리 코드로 물품 리스트를 select하는 메소드
	@Override
	public List<ProductVO> selectByCode(String code) throws SQLException {
		List<ProductVO> productList = null;
		
		try {
			 conn = ds.getConnection();
			 
			 String sql = "select pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, pspec, pcontent, point\n"+
					 "     , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate\n"+
					 "from jsp_product\n"+
					 "where pcategory_fk = ? \n"+
					 "order by pnum desc";
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, code);
			 
			 rs = pstmt.executeQuery();
			 
			 int cnt = 0;
			 while(rs.next()) {
				 cnt++;
				 
				 if(cnt==1) {
					 productList = new ArrayList<ProductVO>();
				 }
				 
				 int pnum = rs.getInt("pnum");
				 String pname = rs.getString("pname");
				 String pcategory_fk = rs.getString("pcategory_fk");
				 String pcompany = rs.getString("pcompany");
				 String pimage1 = rs.getString("pimage1");
				 String pimage2 = rs.getString("pimage2");
				 int pqty = rs.getInt("pqty");
				 int price = rs.getInt("price");
				 int saleprice = rs.getInt("saleprice");
				 String v_pspec = rs.getString("pspec");
				 String pcontent = rs.getString("pcontent");
				 int point = rs.getInt("point");
				 String pinputdate = rs.getString("pinputdate");
				 
				 ProductVO productvo = new ProductVO(pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, v_pspec, pcontent, point, pinputdate); 
				 
				 productList.add(productvo);
				 
			 }// end of while-----------------------
			 
		} finally {
			close();
		}
		 
		return productList;
	}
	

//	#카테고리코드를 받아 카테고리명을 select하는 메소드
	@Override
	public String getCnameByCode(String code) throws SQLException {
		String cname = null;
		try {
			conn = ds.getConnection();
			
			String sql = " select cname\n"+
					" from jsp_category "
					+ " where code=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cname = rs.getString("cname");
			}

		} finally {
			close();
		}
		
		return cname;
	}
	
	
//	---- 강사님이 하신 버전
// **** jsp_jsp_product 테이블에서 카테고리코드(code)별로 제품정보를 읽어오는 메소드 생성하기 *** // 
	public List<HashMap<String, String>> getProductsByCategory(String code) 
		throws SQLException {
		
		List<HashMap<String, String>> productList = null;
		
		try {
			 conn = ds.getConnection();

			 String sql = "select A.cname, pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, pspec, pcontent, point \n"+
					 "     , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate\n"+
					 "from jsp_category A left join jsp_product B \n"+
					 "on A.code = B.pcategory_fk \n"+
					 "where A.code = ? \n"+
					 "order by pnum desc";
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, code);
			 
			 rs = pstmt.executeQuery();
			 
			 int cnt = 0;
			 while(rs.next()) {
				 cnt++;
				 
				 if(cnt==1) {
					 productList = new ArrayList<HashMap<String, String>>();
				 }
				 
				 String cname = rs.getString("cname");
				 int pnum = rs.getInt("pnum");
				 String pname = rs.getString("pname");
				 String pcategory_fk = rs.getString("pcategory_fk");
				 String pcompany = rs.getString("pcompany");
				 String pimage1 = rs.getString("pimage1");
				 String pimage2 = rs.getString("pimage2");
				 int pqty = rs.getInt("pqty");
				 int price = rs.getInt("price");
				 int saleprice = rs.getInt("saleprice");
				 String pspec = rs.getString("pspec");
				 String pcontent = rs.getString("pcontent");
				 int point = rs.getInt("point");
				 String pinputdate = rs.getString("pinputdate");
				 
				 HashMap<String, String> productMap = new HashMap<String, String>(); 
				 
				 productMap.put("CNAME", cname);
				 productMap.put("PNUM", String.valueOf(pnum));
				 productMap.put("PNAME", pname);
				 productMap.put("PCATEGORY_FK", pcategory_fk);
				 productMap.put("PCOMPANY", pcompany);
				 productMap.put("PIMAGE1", pimage1);
				 productMap.put("PIMAGE2", pimage2);
				 productMap.put("PQTY", String.valueOf(pqty));
				 productMap.put("PRICE", String.valueOf(price));
				 productMap.put("SALEPRICE", String.valueOf(saleprice));
				 productMap.put("PSPEC", pspec);
				 productMap.put("PCONTENT", pcontent);
				 productMap.put("POINT", String.valueOf(point));
				 productMap.put("PINPUTDATE", pinputdate);
				 
				 productList.add(productMap);
				 
			 }// end of while
			 
		} finally {
			close();
		}
		
		return productList;
		
	}// end of List<HashMap<String, String>> getProductsByCategory(String code)

/**
 * pnum에 해당하는 제품이 jsp_cart테이블에 없는 경우 insert, 있는 경우 oqty를 update
 * @param: userid; 로그인한 유저 아이디, pnum; 선택한 제품번호, oqty; 수량
 * @return insert 또는 update가 성공했을 경우 1, 실패한 경우 0 리턴 
 */
	@Override
	public int addCart(String userid, int pnum, int oqty) throws SQLException {
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
/*			#pnum으로 jsp_cart테이블에 해당 제품이 있는지 확인 후 없는 경우 insert, 있는 경우 oqty컬럼 update
			-------------------------------------------------
				cartno	fk_userid	fk_pnum	oqty	status	
			-------------------------------------------------
				  1		leess		7		  2		  1		<<<
				  2		hongkd		7		  5		  1
				  3		leess		6		  3		  1
				  4		leess		7		  10	  1		<<< 중복!
*/			
			String sql = " select cartno "
						+ " from jsp_cart "
						+ " where status = 1 and fk_userid = ? and fk_pnum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setInt(2, pnum);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int cartno = rs.getInt("cartno");
				sql = " update jsp_cart set oqty= oqty+? "
						+ "where cartno = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, oqty);
				pstmt.setInt(2, cartno);
				
				result = pstmt.executeUpdate();
			}
			else {
				sql = " insert into jsp_cart(cartno, fk_userid, fk_pnum, oqty, status) "
						+ " values(seq_jsp_cart_cartno.nextval, ?, ?, ?, default)";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setInt(2, pnum);
				pstmt.setInt(3, oqty);
				
				result = pstmt.executeUpdate();
			}
					
		} finally {
			close();
		}
		return result;
	}

/**
 * 페이징 처리 전 장바구니 목록
 * @param userid; 로그인한 유저 아이디
 * @return List<CartVO>; CartVO형태의 List타입
 */
	@Override
	public List<CartVO> getCartList(String userid) throws SQLException {
		List<CartVO> cartList = null;
		
		try {
			 conn = ds.getConnection();

			/*String sql = 
					"select rno, A.cartno, A.fk_userid, A.fk_pnum, A.oqty, A.status," + 
					"B.pnum, B.pname, B.pcategory_fk, " + 
					"B.pimage1, B.pimage2, B.pqty, B. price, B.saleprice, " + 
					"B.pspec, B.point "
					+ "from"
					+ "("
					+ "select rownum as rno, A.cartno, A.fk_userid, A.fk_pnum, A.oqty, A.status,\n"+
					"           B.pnum, B.pname, B.pcategory_fk, \n"+
					"           B.pimage1, B.pimage2, B.pqty, B. price, B.saleprice,\n"+
					"           B.pspec, B.point\n"+
					"from jsp_cart A inner join jsp_product B\n"+
					"on A.fk_pnum = B.pnum\n"+
					"where A.status =1 and A.fk_userid = ? \n"+
					"order by A.cartno asc"
					+ ") V ";*/
			 
			 String sql = "select A.cartno, A.fk_userid, A.fk_pnum, A.oqty, A.status,\n"+
					 "           B.pnum, B.pname, B.pcategory_fk, B.pcompany, \n"+
					 "           B.pimage1, B.pimage2, B.pqty, B. price, B.saleprice,\n"+
					 "           B.pspec, B.point\n"+
					 "from jsp_cart A inner join jsp_product B\n"+
					 "on A.fk_pnum = B.pnum\n"+
					 "where A.status =1 and A.fk_userid = ? \n"+
					 "order by A.cartno desc"; 
			 
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, userid);
			 
			 rs = pstmt.executeQuery();
			 
			 int cnt = 0;
			 while(rs.next()) {
				 cnt++;
				 
				 if(cnt==1) {
					 cartList = new ArrayList<CartVO>();
				 }
				 
				 int cartno = rs.getInt("cartno");
				 String fk_userid = rs.getString("fk_userid");
				 int fk_pnum = rs.getInt("fk_pnum");
				 int oqty = rs.getInt("oqty");
				 int status = rs.getInt("status");
				 
				 int pnum = rs.getInt("pnum");
				 String pname = rs.getString("pname");
				 String pcategory_fk = rs.getString("pcategory_fk");
				 String pimage1 = rs.getString("pimage1");
				 String pimage2 = rs.getString("pimage2");
				 int pqty = rs.getInt("pqty");
				 int price = rs.getInt("price");
				 int saleprice = rs.getInt("saleprice");
				 String pspec = rs.getString("pspec");
				 int point = rs.getInt("point");
				 
				 ProductVO item = new ProductVO();
				 item.setPnum(pnum);
				 item.setPname(pname);
				 item.setPcategory_fk(pcategory_fk);
				 item.setPimage1(pimage1);
				 item.setPimage2(pimage2);
				 item.setPqty(pqty);
				 item.setPrice(price);
				 item.setSaleprice(saleprice);
				 item.setPspec(pspec);
				 item.setPoint(point);			 
				 item.setTotalPriceTotalPoint(oqty);	// 아주 중요! 수량에 따른 가격 총액과 포인트 총액
				 
				 CartVO cartvo = new CartVO(cartno, fk_userid, fk_pnum, oqty, status, item);

				 cartList.add(cartvo);				 
			 }// end of while	 
		} finally {
			close();
		}
		return cartList;
	}

	
/**
 * 장바구니 수량 수정 메소드
 * @param cartno; 장바구니 일련번호
 * @param oqty; 해당 물품 수량의 수정값
 * @return result; update 성공 시 1, 실패 0
 */
	@Override
	public int updateDeleteCart(int cartno, int oqty) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			String sql = "";
			
			if(oqty==0) {	// 장바구니 비우기(status==0) --> 원래는 delete로 처리!
				sql = " update jsp_cart set status=0 "
						+ " where cartno = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, cartno);
				
				result = pstmt.executeUpdate();
			}
			else {	// 장바구니 수량 변경
				sql = " update jsp_cart set oqty=? "
						+ "where cartno = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, oqty);
				pstmt.setInt(2, cartno);
				
				result = pstmt.executeUpdate();
			}
					
		} finally {
			close();
		}
		return result;
	}

/**
 * 주문코드의 시퀀스를 채번하는 메소드
 * @return sequence
 * @throws SQLException
 */
	@Override
	public int getSeq_jsp_order() throws SQLException {
		int seq = 0;
		try {
			conn = ds.getConnection();
			
			String sql = " select seq_jsp_order.nextval as seq "
						+ " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			seq = rs.getInt("seq");
		} finally {
			close();
		}
		return seq;
	}

/**
1. 주문개요 테이블(jsp_order)에 insert
2. 주문상세 테이블(jsp_order_detail)에 insert
3. 구매하는 사용자의 coin컬럼을 가격만큼 마이너스, point컬럼 값은 구매한 포인트만큼 플러스 update
4. 주문한 제품(jsp_product)의 재고(pqty)는 주문량(oqty)만큼 감소(update)
5. 주문완료 후 장바구니에 해당 물품 삭제(본래 delete이지만 status==0으로 update)
6. return result
 * @param odrcode, userid, sumtotalprice, sumtotalpoint; jsp_order
 * @param pnumArr, oqtyArr, salepriceArr ; jsp_order_detail
 * @param cartnoArr ; jsp_cart
 * @return result; 성공했으면 1, 실패했으면 0 --> 1인 경우 email 발송
 * @throws SQLException
 */
	@Override
	public int addOrder(String odrcode, String userid, int sumtotalprice, int sumtotalpoint, String[] pnumArr,
			String[] oqtyArr, String[] salepriceArr, String[] cartnoArr) throws SQLException {

		int n1=0, n2=0, n3=0, n4=0, n5=0;
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false); // 수동 커밋으로 전환 (트랜젝션)
//			1. 주문개요 테이블(jsp_order)에 insert
			String sql = " insert into jsp_order(odrcode, fk_userid, odrtotalPrice, odrtotalPoint, odrdate) "
						+ " values(?, ?, ?, ?, default) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odrcode);
			pstmt.setString(2, userid);
			pstmt.setInt(3, sumtotalprice);
			pstmt.setInt(4, sumtotalpoint);
			
			n1 = pstmt.executeUpdate();
			if(n1!=1) {
				conn.rollback();
				conn.setAutoCommit(true);
				return 0;
			}
			
			if(n1==1) {
//			2. 주문상세 테이블(jsp_order_detail)에 insert	
				for(int i=0; i<pnumArr.length; i++) {
					sql = " insert into jsp_order_detail(odrseqnum, fk_odrcode, fk_pnum, oqty, odrprice, deliverStatus) "
							+ " values(seq_jsp_order_detail.nextval, ?, to_number(?), to_number(?), to_number(?), default) ";
	
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, odrcode);
					pstmt.setString(2, pnumArr[i]);
					pstmt.setString(3, oqtyArr[i]);	// 배열 사이즈가 같기 때문에 그대로 넣어줌
					pstmt.setString(4, salepriceArr[i]);
					
					n2 = pstmt.executeUpdate();
					
					if(n2 != 1) {
						conn.rollback();
						conn.setAutoCommit(true);
						return 0;
					}
				}
			}
			
//			3. 구매하는 사용자의 coin컬럼을 가격만큼 마이너스, point컬럼 값은 구매한 포인트만큼 플러스 update
			if(n2==1) {
				sql=" update jsp_member set coin = coin-?, point = point+? "
						+ " where userid = ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, sumtotalprice);
				pstmt.setInt(2, sumtotalpoint);
				pstmt.setString(3, userid);
				
				n3 = pstmt.executeUpdate();
				if(n3!=1) {
					conn.rollback();
					conn.setAutoCommit(true);
					return 0;
				}
			}
			
//			4. 주문한 제품(jsp_product)의 재고(pqty)는 주문량(oqty)만큼 감소(update)
			if(n3==1) {
				for(int i=0; i<pnumArr.length; i++) {
					sql=" update jsp_product set pqty = pqty-to_number(?) "
							+ " where pnum = to_number(?) ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, oqtyArr[i]);
					pstmt.setString(2, pnumArr[i]);
					
					n4=pstmt.executeUpdate();
					if(n4!=1) {
						conn.rollback();
						conn.setAutoCommit(true);
						return 0;
					}
				}
			}
			
			
//			5. 주문완료 후 장바구니에 해당 물품 삭제(본래 delete이지만 status==0으로 update)
			if(cartnoArr != null && n4==1) {
				for(int i=0; i<cartnoArr.length; i++) {
					sql=" update jsp_cart set status=0 where cartno=to_number(?) ";
//					cf) 만약 delete로 처리할 때; backup 테이블에 삭제할 데이터를 백업해두고 본 테이블에서 삭제
//					sql1= " insert into jsp_cart_backup values( select * from jsp_cart where cartno=to_number(?) )"
//					sql2=" delete table jsp_cart where cartno=to_number(?)";

					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, cartnoArr[i]);

					n5=pstmt.executeUpdate();
					if(n5!=1) {
						conn.rollback();
						conn.setAutoCommit(true);
						return 0;
					}
				}
			}
			
//			6. commit and return result
//			1) 바로주문하기인 경우 commit
			if(cartnoArr==null && n1*n2*n3*n4==1) {
				conn.commit();
				conn.setAutoCommit(true);
				return 1;
			}
//			2) 장바구니에서 주문하기를 한 경우 commit
			else if(cartnoArr!=null && n1*n2*n3*n4*n5==1) {
				conn.commit();
				conn.setAutoCommit(true);
				return 1;
			}
			else {
				conn.rollback();
				conn.setAutoCommit(true);
				return 0;
			}
		} finally {
			close();
		}
	}

/**
 * 주문완료된 물품 정보를 select하는 메소드; 메일발송용
 * @param pnumes 주문완료된 물품의 pnum값들('a','b','c'...)
 * @return List<ProductVO>
 * @throws SQLException
 */
	@Override
	public List<ProductVO> getOrderProductList(String pnumes) throws SQLException {
		List<ProductVO> orderProductList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, pspec, pcontent, point\n"+
					 "     , to_char(pinputdate, 'yyyy-mm-dd') as pinputdate\n"+
					 "from jsp_product\n"+
					 "where pnum in("+pnumes+") \n";
			 // in()은 ?로 값을 넣을 수 없기 때문에 변수를 직접 입력
			 pstmt = conn.prepareStatement(sql);
			 rs = pstmt.executeQuery();
			 
			 int cnt = 0;
			 while(rs.next()) {
				 cnt++;
				 if(cnt==1) {
					 orderProductList = new ArrayList<ProductVO>();
				 }
				 int pnum = rs.getInt("pnum");
				 String pname = rs.getString("pname");
				 String pcategory_fk = rs.getString("pcategory_fk");
				 String pcompany = rs.getString("pcompany");
				 String pimage1 = rs.getString("pimage1");
				 String pimage2 = rs.getString("pimage2");
				 int pqty = rs.getInt("pqty");
				 int price = rs.getInt("price");
				 int saleprice = rs.getInt("saleprice");
				 String v_pspec = rs.getString("pspec");
				 String pcontent = rs.getString("pcontent");
				 int point = rs.getInt("point");
				 String pinputdate = rs.getString("pinputdate");
				 
				 ProductVO productvo = new ProductVO(pnum, pname, pcategory_fk, pcompany, pimage1, pimage2, pqty, price, saleprice, v_pspec, pcontent, point, pinputdate); 
				 
				 orderProductList.add(productvo);
				 
			 }// end of while-----------------------
			 
		} finally {
			close();
		}		
		return orderProductList;
	}

/**
 * 주문목록을 가져오는 메소드
 * @param userid; 로그인한 유저 아이디
 * @return orderList
 * @throws SQLException
 */
	@Override
	public List<HashMap<String, String>> getOrderList(String userid) throws SQLException {
		List<HashMap<String, String>> orderList =null;
		
		try {
			conn = ds.getConnection();
			String sql = "select A.odrcode, A.fk_userid, to_char(A.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate\n"+
					"        , B.odrseqnum, B.fk_pnum, B.oqty, B.odrprice\n"+
					"        , case b.deliverstatus when 1 then '주문완료' when 2 then '배송시작' when 3 then '배송완료' end as deliverstatus\n"+
					"        , c.pname, c.pimage1, c.price, c.saleprice, c.point\n"+
					"from jsp_order A join jsp_order_detail B\n"+
					"on A.odrcode = B.fk_odrcode\n"+
					"join jsp_product C\n"+
					"on B.fk_pnum = C.pnum\n"+
					"where 1=1 ";
			if(!"admin".equals(userid)) {
				sql += " and A.fk_userid = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userid);
			}
			else {
				pstmt = conn.prepareStatement(sql);
			}
			
			rs = pstmt.executeQuery();
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					orderList = new ArrayList<HashMap<String, String>>();
				}
				String odrcode = rs.getString("odrcode");
				String fk_userid = rs.getString("fk_userid");
				String odrdate = rs.getString("odrdate");
				String odrseqnum = rs.getString("odrseqnum");
				String fk_pnum = rs.getString("fk_pnum");
				String oqty = rs.getString("oqty");
				String odrprice = rs.getString("odrprice");
				String deliverstatus = rs.getString("deliverstatus");
				String pname = rs.getString("pname");
				String pimage1 = rs.getString("pimage1");
				String price = rs.getString("price");
				String saleprice = rs.getString("saleprice");
				String point = rs.getString("point");
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("odrcode", odrcode);
				map.put("userid", fk_userid);
				map.put("odrdate", odrdate);
				map.put("odrseqnum", odrseqnum);
				map.put("fk_pnum", fk_pnum);
				map.put("oqty", oqty);
				map.put("odrprice", odrprice);
				map.put("deliverstatus", deliverstatus);
				map.put("pname", pname);
				map.put("pimage1", pimage1);
				map.put("price", price);
				map.put("saleprice", saleprice);
				map.put("point", point);
				
				orderList.add(map);
				 
			}		
		} finally {
			close();
		}
		return orderList;
	}

/**
 * odrcode로 특정회원정보 가져오기
 * @param odrcode; 주문코드
 * @return MemberVO
 */
	@Override
	public MemberVO getOneUserByOdrcode(String odrcode) throws SQLException {
		MemberVO membervo = null;
		try {
			conn = ds.getConnection();
			String sql = " select IDX, USERID, NAME, PWD, EMAIL, HP1, HP2, HP3, "+
						 " POST1, POST2, ADDR1, ADDR2, GENDER, BIRTHDAY, "+
					     " COIN, POINT, to_char(REGISTERDAY, 'yyyy-mm-dd') as REGISTERDAY, STATUS,"
					     + "to_char(lastlogindate, 'yyyy-mm-dd') as lastlogindate , to_char(lastPwdChangeDate, 'yyyy-mm-dd') as lastPwdChangeDate "+
					     " from jsp_member "+
					     " where userid = ( select fk_userid\n"+
					     "                        from jsp_order\n"+
					     "                        where odrcode = ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, odrcode);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int idx2 = rs.getInt("IDX");
				String userid = rs.getString("USERID");
				String name = rs.getString("NAME");
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

//	#[관리자기능] 전체 주문목록에서 주문코드와 제품번호를 받아서 배송시작으로 변경하는 메소드
	@Override
	public int updateDeliverStart(String odrcodePnum, int length) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String sql = " update jsp_order_detail set deliverStatus=2 where (fk_odrcode||'/'||fk_pnum) in("+odrcodePnum+") ";
			
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate(); // 여러개를 업데이트 하기 때문에 배열길이만큼 update
			
			if(result==length) {
				conn.commit();
				conn.setAutoCommit(true);
				
				return 1;
			}
			else {
				conn.rollback();
				return 0;
			}
		} finally {
			close();
		}	
	}


	@Override
	public int updateDeliverEnd(String odrcodePnum, int length) throws SQLException {
		int result = 0;
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			
			String sql = " update jsp_order_detail set deliverStatus=3 where (fk_odrcode||'/'||fk_pnum) in("+odrcodePnum+") ";
			
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate(); // 여러개를 업데이트 하기 때문에 배열길이만큼 update
			
			if(result==length) {
				conn.commit();
				conn.setAutoCommit(true);
				
				return 1;
			}
			else {
				conn.rollback();
				return 0;
			}
		} finally {
			close();
		}	
	}
	
	
	@Override
	public List<HashMap<String, String>> getDeliverInfo(String odrcodePnum) throws SQLException {
		 List<HashMap<String, String>> deliverInfoList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = "select A.odrcode, to_char(A.odrdate, 'yyyy-mm-dd hh24:mi:ss') as odrdate\n"+
					"        , B.fk_pnum, B.oqty, B.odrprice, B.deliverstatus\n"+
					"        , c.pname, c.pimage1,  c.pimage2, c.saleprice, c.point\n"+
					"        , D.userid, D.email\n"+
					"from jsp_member D \n"+
					"    inner join jsp_order A \n"+
					"    on A.fk_userid = D.userid\n"+
					"    inner join jsp_order_detail B\n"+
					"    on A.odrcode = B.fk_odrcode\n"+
					"    inner join jsp_product C\n"+
					"    on B.fk_pnum = C.pnum\n"+
					"where (fk_odrcode||'/'||fk_pnum) in("+odrcodePnum+")"
					+ " order by D.userid ";
			
			 // in()은 ?로 값을 넣을 수 없기 때문에 변수를 직접 입력
			 pstmt = conn.prepareStatement(sql);
			 rs = pstmt.executeQuery();
			 
			 int cnt = 0;
			 while(rs.next()) {
				 cnt++;
				 if(cnt==1) {
					 deliverInfoList = new ArrayList<HashMap<String, String>>();
				 }
				 String odrcode = rs.getString("odrcode");
				 String odrdate = rs.getString("odrdate");
				 String fk_pnum = rs.getString("fk_pnum");
				 String oqty = rs.getString("oqty");
				 String odrprice = rs.getString("odrprice");
				 String deliverstatus = rs.getString("deliverstatus");
				 String pname = rs.getString("pname");
				 String pimage1 = rs.getString("pimage1");
				 String pimage2 = rs.getString("pimage2");
				 String saleprice = rs.getString("saleprice");
				 String point = rs.getString("point");
				 String userid = rs.getString("userid");
				 String email = aes.decrypt(rs.getString("email"));
				 
				 HashMap<String, String> map = new HashMap<String, String>();
				 
				 map.put("odrcode", odrcode);
				 map.put("odrdate", odrdate);
				 map.put("fk_pnum", fk_pnum);
				 map.put("oqty", oqty);
				 map.put("odrprice", odrprice);
				 map.put("deliverstatus", deliverstatus);
				 map.put("pname", pname);
				 map.put("pimage1", pimage1);
				 map.put("pimage2", pimage2);
				 map.put("saleprice", saleprice);
				 map.put("point", point);
				 map.put("userid", userid);
				 map.put("email", email);
				 
 
				 deliverInfoList.add(map);
				 
			 }// end of while-----------------------
			 
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		} finally {
			close();
		}		
		return deliverInfoList;
	}

	
//	#구글맵 api를 이용한 매장찾기 기능
	@Override
	public List<StoremapVO> getStoreMap() throws SQLException {
		List<StoremapVO> storemapList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select storeno, storeName, latitude, longitude, zindex, tel, addr, transport "
					   + " from jsp_storemap "
					   + " order by storeno ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1) storemapList = new ArrayList<StoremapVO>();
				
				int storeno = rs.getInt("storeno");
				String storeName = rs.getString("storeName");
				double latitude = rs.getDouble("latitude");
				double longitude = rs.getDouble("longitude");
				int zindex = rs.getInt("zindex");
				String tel = rs.getString("tel");
				String addr = rs.getString("addr");
				String transport = rs.getString("transport");
								
				StoremapVO mapvo = new StoremapVO(storeno, storeName, latitude, longitude, zindex, tel, addr, transport);
				
				storemapList.add(mapvo);
				
			}// end of while---------------------
		} finally {
			close();
		}
		return storemapList;
	}


	@Override
	public StoremapVO getStoreDetail(String storeno) throws SQLException {
		StoremapVO storemapvo = null;
		List<String> images = null;
		try {
			conn = ds.getConnection();
			
			String sql = " select storeno, storeName, tel, addr, transport, img " + 
					" from jsp_storemap a join jsp_storedetailImg " + 
					" on storeno = fk_storeno " + 
					" where storeno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, storeno);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				if(cnt==1) {
					storemapvo = new StoremapVO();
					images = new ArrayList<String>();
					int v_storeno = rs.getInt("storeno");
					String storeName = rs.getString("storeName");
					String tel = rs.getString("tel");
					String addr = rs.getString("addr");
					String transport = rs.getString("transport");
					
					storemapvo.setStoreno(v_storeno);
					storemapvo.setStoreName(storeName);
					storemapvo.setTel(tel);
					storemapvo.setAddr(addr);
					storemapvo.setTransport(transport);
				}
				
				
				images.add(rs.getString("img"));
			}
			storemapvo.setImages(images);
			
		} finally {
			close();
		}
		return storemapvo;
	}

	
//	#매장 상세정보 보기; 강사님이 하신 것
	@Override
	public List<HashMap<String, String>> getStoreDetailList(String storeno) throws SQLException {
		List<HashMap<String, String>> storeDetailList =null;
		try{
			conn = ds.getConnection();
			// DBCP객체 ds를 통해 context.xml에서 이미 설정된 Connection 객체를 빌려오는 것이다.
			
			String sql = " select storeno, storeName, tel, addr, transport "
					   + " from jsp_storemap  "
					   + " where storeno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, storeno);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				storeDetailList = new ArrayList<HashMap<String, String>>();
				
				storeno = rs.getString("storeno");
				String storeName = rs.getString("storeName");
				String tel = rs.getString("tel");
				String addr = rs.getString("addr");
				String transport = rs.getString("transport");
								
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("storeno", storeno);
				map.put("storeName", storeName);
				map.put("tel", tel);
				map.put("addr", addr);
				map.put("transport", transport);
				
				storeDetailList.add(map);
				
			}// end of if---------------------
			
			sql =  " select img " 
                + " from jsp_storedetailImg "
                + " where fk_storeno = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, storeno);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
					
				String img = rs.getString("img");
												
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("img", img);
								
				storeDetailList.add(map);
				
			}// end of while---------------------
			
		 } finally{
			close();
		 }
		
		return storeDetailList;
	}
	
	

} // end of class
