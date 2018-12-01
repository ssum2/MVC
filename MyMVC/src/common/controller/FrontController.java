package common.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class FrontController
 */
@WebServlet(
		description = "사용자가 웹에서 *.do를 호출했을 때 먼저 응답한다", 
		urlPatterns = { "*.do" }, 
		initParams = {
//		servlet을 구동한 다음에 initParams로 단 한번만 구동되기 때문에 properties에서 새로운 .do를 매핑 했을 때 인식 X
//		>> properties를 수정했을 때 WAS 및 서블릿을 반드시 재구동 해야함
				
				@WebInitParam(name = "propertyConfig", value = "C:/Git/MyMVC/WebContent/WEB-INF/Command.properties", description = "*.do관련 class Mapping Info")
		})
public class FrontController extends HttpServlet {
//	>>FrontController; 배치서술자의 역할 -> 모든 *.do를 다 받아와서 받아온 .do가 어떤 기능을 하는지 알려줌

	private static final long serialVersionUID = 1L;

//	#init(config)에서 생성한 object들을 넣어줄 HashMap<key타입, 객체타입> 생성
	HashMap<String, Object> cmdMap = new HashMap<String, Object>();
	
	
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// 	.do를 호출했을 때 FrontController에서 가장 먼저 호출되는 메소드
		//	>> 초기화 파라미터 블럭으로 읽어온 Mapping 정보에 서술된 .do와 연관있는 클래스들을 실제 객체화 해줌 -> 한번만 실행
		System.out.println(">> 확인용: servlet FrontController의 init 메소드 실행");
		
		String props = config.getInitParameter("propertyConfig"); // initParams에 있는 value의 경로를 지정된 name으로 불러옴
		System.out.println(">> 확인용: 초기화 파라미터 데이터값; " + props);
		
		Properties pr = new Properties(); // FileInputStream으로 properties파일을 읽어와서 객체화
		
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(props);
			pr.load(fis);
			/*
			 	Properties객체.load(initParmas에서 불러온 value값)
			 	> properties파일을 읽어와서 Properties클래스의 객체에 데이터를 올려줌
			 	> 읽어온 파일의 내용에서 '='를 기준으로 왼쪽은 key로 인식, 오른쪽은 value로 인식	
			*/
			/*String className = pr.getProperty("/test1.do"); // Properties객체.getProperty("key") -> return value
			System.out.println(">> 확인용: properties에 있는 key값으로 value 불러오기;"+className);
			// className=test.controller.Test1Controller
			
			Class<?> cls = Class.forName(className); // Class타입의 객체설계도
			// >> Class<?>; 불특정 객체타입 1개
			
			Object obj = cls.newInstance(); // className에 있는 value값을 토대로 객체화
			
			cmdMap.put("/test1.do", obj);*/
			
//			#properties에 있는 key를 불러오기
			Enumeration<Object> en = pr.keys();
// 			>> properties파일에 선언되어있는 mapping 정보에서 '='을 중심으로 왼쪽에 있는 모든 key값만 가져옴
			
			while(en.hasMoreElements()) {
//				>> Enumeration객체에 있는 Element가 있는 경우 true, 없으면 false
				
				String key_urlcmd = (String)en.nextElement(); // 실제 담겨있는 key를 받아옴
				String className = pr.getProperty(key_urlcmd);
				
				if(className != null) {
					className = className.trim();
					Class<?> cls = Class.forName(className);
					Object obj = cls.newInstance();
					
					cmdMap.put(key_urlcmd, obj);
					
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("String타입으로 명시된 클래스가 없습니다.");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println(props+"파일이 없습니다.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestProcess(request, response);
	}

	protected void requestProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		#init메소드에서 만들어 HashMap에 넣어둔 객체를 해당 객체의 클래스타입으로 캐스팅하여 가져옴
/*		Test1Controller action = (Test1Controller)cmdMap.get("/test1.do");
		action.testPrint();*/
//		>> Interface로 부모클래스를 만든 다음, 부모클래스로 캐스팅해주고 자식클래스에서 override한 메소드를 불러옴
		
//		#웹브라우저에 나오는 주소를 이용하여 Hashmap의 key값 구하기(/~.do)
		String url = request.getRequestURL().toString();
		// >> http://localhost:9090/MyMVC/test2.do
		
		String uri = request.getRequestURI();
		// >> /MyMVC/test2.do 
		
		String ctxPath = request.getContextPath(); 
		// >> /MyMVC
		
		String mapKey = uri.substring(ctxPath.length());
		// >> /test2.do ==> key값!
		
//		#위에서 구한 key를 이용하여 Hashmap에서 객체를 가져옴
//		 >> 다형성을 이용하여 부모클래스로 지정해둔 추상클래스를 사용해 캐스팅 --> 저장되어있는 모든 객체에 공통적용
		AbstractController action = (AbstractController)cmdMap.get(mapKey);
		
		if(action == null) {
			System.out.println(mapKey+" URL 패턴에 매핑된 객체가 없습니다.");
			return;
		}
		else {
			try {
				action.execute(request, response); // >> 추상클래스에서 정해둔 메소드를 상속받은 자식클래스에서 재정의한 것으로 사용
				
				
				String viewPage = action.getViewPage();
				boolean bool = action.isRedirect();
				
				if(bool) {
					// >> true일 때 sendRedirect방식으로 page를 이동
					// redirect; 데이터(history, request, response 등)를 넘기지 않고 페이지만 이동됨
					response.sendRedirect(viewPage);
				}
				else {
					// >> false일 때 forward방식으로 page이동
					// forward; 페이지 이동시 데이터까지 넘어감
					RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
					dispatcher.forward(request, response);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		
		
	}
}
