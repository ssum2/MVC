package memo.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface InterMemoDAO {
	
//	#메모 작성 메소드 (insert) 가이드라인
	int memoInsert(MemoVO memovo) throws SQLException;
	
//	#전체 메모 조회 메소드(HashMap) 가이드라인
	List<HashMap<String, String>> getAllMemo() throws SQLException;
	// List<HashMap<key 데이터타입, 출력물 데이터타입>>

	int getTotalCount(String searchType, String searchWord, int period) throws SQLException;

	List<HashMap<String, String>> getAllMemo(int sizePerPage, int currentShowPageNo, String searchType, String searchWord, int period) throws SQLException;
	
	int getTotalCount(String userid, String searchType, String searchWord, int period) throws SQLException;

	List<MemoVO> getAllMemo(String userid, int sizePerPage, int currentShowPageNo, String searchType, String searchWord, int period) throws SQLException;
	
//	#나의 메모 비공개 처리하기
	void memoBlind1(String[] delCheckArr) throws SQLException;
//	void memoBlind(String[] delCheckArr) throws SQLException;
	
//	#나의 메모 공개처리 하기
	void memoOpen(String[] delCheckArr) throws SQLException;

//	#나의 메모 공개/비공개처리  한번에 하기
	void memoOpenBlind(String[] delCheckArr, String status) throws SQLException;
	
//	#나의 메모 삭제하기
	void memoDelete(String[] delCheckArr) throws SQLException;
	
}
