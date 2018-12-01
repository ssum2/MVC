package myshop.model;

import java.sql.*;
import java.util.*;

import member.model.MemberVO;

public interface InterProductDAO {

//	#jsp_product 테이블에서 pspec 컬럼의 값(HIT, NEW, BEST)별로 상품 목록을 가져오는 추상 메소드
	List<ProductVO> selectByPspec(String pspec) throws SQLException;
	
//	#jsp_product테이블에서 pnum(제품번호)에 해당하는 제품 1개를 select해오는 추상메소드
	ProductVO getProductOneByPnum(int pnum) throws SQLException;

//	#물품 카테고리정보 가져오기(HashMap)
	List<HashMap<String, String>> selectCategory() throws SQLException;

//	#물품 스펙 정보 가져오기
	List<HashMap<String, String>> selectSpec() throws SQLException;

//	#제품 번호 채번
	int getPnumOfProduct() throws SQLException;

//	#[관리자기능]제품등록(insert)
	int productInsert(ProductVO pvo) throws SQLException;

//	#제품 이미지정보를 jsp_product_imagefile 테이블에 insert하는 추상메소드
	int product_imagefile_Insert(int pnum, String attachFilename) throws SQLException;

//	#추가 첨부이미지파일명 가져오는 추상 메소드
	List<HashMap<String, String>> selectAttachImage(int pnum) throws SQLException;

//	#물품 카테고리 목록 가져오기(VO)
	List<CategoryVO> getCategoryList() throws SQLException;

//	#jsp_product 테이블에서 카테고리코드 값 별로 상품 목록을 가져오는 추상 메소드
	List<ProductVO> selectByCode(String code) throws SQLException;

//	#카테고리코드를 받아 카테고리명을 select하는 메소드
	String getCnameByCode(String code) throws SQLException;

//	#fk_userid, pnum, oqty를 받아서 장바구니 테이블에 insert하는 추상 메소드
	int addCart(String userid, int pnum, int oqty) throws SQLException;

//	#장바구니 목록; 페이징 처리 전
	List<CartVO> getCartList(String userid) throws SQLException;

//	#장바구니 수량 수정하기; oqty==0; delete / oqty>0; update
	int updateDeleteCart(int cartno, int oqty) throws SQLException;

//	#주문코드 시퀀스를 채번하는 메소드
	int getSeq_jsp_order() throws SQLException;

//	#주문하기 메소드
	int addOrder(String odrcode, String userid, int sumtotalprice, int sumtotalpoint, String[] pnumArr,
			String[] oqtyArr, String[] salepriceArr, String[] cartnoArr) throws SQLException;

//	#주문완료된 물품의 정보를 가져오는 메소드
	List<ProductVO> getOrderProductList(String pnumes) throws SQLException;

//	#[관리자,일반회원]주문 목록을 가져오는 메소드
	List<HashMap<String, String>> getOrderList(String userid) throws SQLException;

//	#[관리자기능] 주문코드로 특정회원정보 가져오는 메소드
	MemberVO getOneUserByOdrcode(String odrcode) throws SQLException;

//	#[관리자기능] 전체 주문목록에서 주문코드와 제품번호를 받아서 배송시작으로 변경하는 메소드
	int updateDeliverStart(String odrcodePnum, int length) throws SQLException;

//	#[관리자기능] 전체 주문목록에서 주문코드와 제품번호를 받아서 배송완료로 변경하는 메소드
	int updateDeliverEnd(String odrcodePnum, int length) throws SQLException;

//	#혼자 하던 것; 배송정보 받아오기(유저정보+배송물품정보)
	List<HashMap<String, String>> getDeliverInfo(String odrcodePnum) throws SQLException;

//	#구글맵 api를 이용한 매장찾기 기능
	List<StoremapVO> getStoreMap() throws SQLException;

//	#구글맵 마커 상세보기 기능; 내가 한 것
	StoremapVO getStoreDetail(String storeno) throws SQLException;

//	#구글맵 마커 상세보기 기능; 강사님이 하신 것
	List<HashMap<String, String>> getStoreDetailList(String storeno) throws SQLException;
	
}
