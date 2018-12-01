package ajax.controller.chap4;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;

public class FirstPersonJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HashMap<String, String> personMap = new HashMap<String, String>();
		personMap.put("name", "이순신");
		personMap.put("age", "27");
		personMap.put("phone", "010-3456-7568");
		personMap.put("email", "leess@gmail.com");
		personMap.put("addr", "서울시 중구 남대문로 24");
		
		// HashMap에 있는 값을 JSON 데이터포맷으로 변경
		JSONObject jsonPerson = new JSONObject();
		
		jsonPerson.put("name", personMap.get("name"));
		jsonPerson.put("age", personMap.get("age"));
		jsonPerson.put("phone", personMap.get("phone"));
		jsonPerson.put("email", personMap.get("email"));
		jsonPerson.put("addr", personMap.get("addr"));
		
		
		// JSON 객체 타입을 String 타입으로 변환
		String str_jsonPerson = jsonPerson.toString();
		
//		System.out.println("str_jsonPerson: "+str_jsonPerson);
//		>>str_jsonPerson: {"phone":"010-3456-7568","name":"이순신","addr":"서울시 중구 남대문로 24","age":"27","email":"leess@gmail.com"}
		
		req.setAttribute("str_jsonPerson", str_jsonPerson);
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap4/1personJSON.jsp");
	}

}
