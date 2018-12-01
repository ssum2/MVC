package member.model;

import java.sql.SQLException;
import java.util.List;

public interface InterMemberDAO {

//	#로그인 처리(로그인 성공; MemberVO객체 return, 실패; null return)
	MemberVO loginOKmemberInfo(String userid, String pwd) throws SQLException;

//	#회원가입 메소드
	int registerMember(MemberVO membervo) throws SQLException;

//	#아이디중복검사 메소드
	boolean idDuplicateCheck(String userid) throws SQLException;

//	#검색 및 날짜 구간이 있는 버전의 회원 조회 메소드 가이드라인
	int getTotalCount(String searchType, String searchWord, int period) throws SQLException;

//	#페이징 처리가 완료된 상태에서 검색 및 날짜구간기능까지 포함하여 memberList에 전체회원을 넣어주는 메소드
	List<MemberVO> getAllMember(int sizePerPage, int currentShowPageNo, String searchType, String searchWord,
			int period) throws SQLException;

//	#회원 정보 수정 메소드
	int updateMember(MemberVO membervo) throws SQLException;

//	#회원 삭제 메소드
	int deleteMember(int idx) throws SQLException;

//	#일반회원으로 로그인 시 보이는 총 회원 명수
	int getTotalCountMember(String searchType, String searchWord, int period) throws SQLException;
	
//	# 회원 계정 활성화 메소드
	int updateMemberToEnable(int idx) throws SQLException;

//	#회원 복원 메소드
	int recoveryMember(int idx) throws SQLException;

//	#아이디 찾기 메소드
	String findUserid(String name, String mobile) throws SQLException;

//	#회원 인증 메일 발송; 패스워드 찾기
	int isUserExists(String userid, String email) throws SQLException;

//	#새로운 비밀번호 변경 메소드
	int changeNewPwd(String userid, String pwd) throws SQLException;

//	#해당 회원의 코인 및 포인트를 업데이트 하는 메소드
	int coinAddUpdate(int idx, int coinmoney) throws SQLException;

}
