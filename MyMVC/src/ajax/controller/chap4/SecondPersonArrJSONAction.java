package ajax.controller.chap4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;

public class SecondPersonArrJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		HashMap<String, String> personMap1 = new HashMap<String, String>();
		personMap1.put("name", "이순신");
		personMap1.put("age", "27");
		personMap1.put("phone", "010-3456-7568");
		personMap1.put("email", "leess@gmail.com");
		personMap1.put("addr", "서울시 중구 남대문로 24");
		
		HashMap<String, String> personMap2 = new HashMap<String, String>();
		personMap2.put("name", "홍길동");
		personMap2.put("age", "37");
		personMap2.put("phone", "010-3456-4343");
		personMap2.put("email", "hongkd@gmail.com");
		personMap2.put("addr", "서울시 강남구 압구정로 25");
		
		HashMap<String, String> personMap3 = new HashMap<String, String>();
		personMap3.put("name", "엄정화");
		personMap3.put("age", "27");
		personMap3.put("phone", "010-3446-4343");
		personMap3.put("email", "eom@gmail.com");
		personMap3.put("addr", "서울시 강서구 공항로 26");
		
		List<HashMap<String, String>> personList = new ArrayList<HashMap<String, String>>();
		
		personList.add(personMap1);
		personList.add(personMap2);
		personList.add(personMap3);
		
		JSONArray jsonArr = new JSONArray();
		// JSONObject는 HashMap과 흡사, JSONArray는 List와 흡사함
		
		for(HashMap<String, String> map : personList) {
			// HashMap에 있는 값을 JSON 데이터포맷으로 변경
			JSONObject jsonPerson = new JSONObject();
			jsonPerson.put("name", map.get("name"));
			jsonPerson.put("age", map.get("age"));
			jsonPerson.put("phone", map.get("phone"));
			jsonPerson.put("email", map.get("email"));
			jsonPerson.put("addr", map.get("addr"));
			
			jsonArr.add(jsonPerson);
		}
		
		// JSONArray객체를 String 타입으로 변환
		String str_jsonArr = jsonArr.toString();
		
		System.out.println("str_jsonArr: "+str_jsonArr);
/*		str_jsonArr: [{"phone":"010-3456-7568","name":"이순신","addr":"서울시 중구 남대문로 24","age":"27","email":"leess@gmail.com"},
		{"phone":"010-3456-4343","name":"홍길동","addr":"서울시 강남구 압구정로 25","age":"37","email":"hongkd@gmail.com"},
		{"phone":"010-3446-4343","name":"엄정화","addr":"서울시 강서구 공항로 26","age":"27","email":"eom@gmail.com"}]
*/

		req.setAttribute("str_jsonArr", str_jsonArr);
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap4/2personArrJSON.jsp");
	}

}
