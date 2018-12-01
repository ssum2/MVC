package myshop.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class ProductRegisterEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		#admin으로 로그인 했을 경우에만 접근 가능하도록 제약
		MemberVO loginuser = super.getLoginUser(req);
		System.out.println("ProductRegisterEndAction 1/7 success");
		if(loginuser == null) return;
		
		String userid = loginuser.getUserid();
		if(!"admin".equals(userid)) {
			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		else {
//			1. 첨부파일이 저장될 경로 설정
			HttpSession session = req.getSession();
			ServletContext svlCtx = session.getServletContext(); // getServletContext; 프로그램 전체 == ServletContext]
			String imagesDir =  svlCtx.getRealPath("/images");	// .java, .xml은 '/' == contextname까지(/MyMVC/)
			// getRealPath("/폴더명"); 해당 폴더의 절대 경로; C:\myjsp\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\MyMVC\images
			// 실제 작동되는 파일은 .metadata에 복사된 파일들
			System.out.println(">> imagesDir: "+imagesDir);
			System.out.println("ProductRegisterEndAction 2/7 success");
			
//			2. 파일을 받아옴--> cos.jar lib 사용;
//				- MultipartRequest; 자바 내장 객체 request의 일을 승계받아 일 처리를 하고 동시에 파일을 받아서 업로드, 다운로드를 처리
			MultipartRequest mtreq = null;	
			try {
			/*
			MultipartRequest의 객체가 생성됨과 동시에 파일 업로드가 이루어 진다.
						 
			MultipartRequest(HttpServletRequest request,
				       	     String saveDirectory, -- 파일이 저장될 경로
					     	 int maxPostSize,      -- 업로드할 파일 1개의 최대 크기(byte); cf) 10MB = 10*1024*1024 BYTE
					     	 String encoding,
					     	 FileRenamePolicy policy) -- 중복된 파일명이 올라갈 경우 파일명다음에 자동으로 숫자가 붙어서 올라간다. ~~(1),(2)...
					   
			파일을 저장할 디렉토리를 지정할 수 있으며, 업로드제한 용량을 설정할 수 있다.(바이트단위). 
			이때 업로드 제한 용량을 넘어서 업로드를 시도하면 IOException 발생된다. 
			또한 국제화 지원을 위한 인코딩 방식을 지정할 수 있으며, 중복 파일 처리 인터페이스를사용할 수 있다.
					   		
			 이때 업로드 파일 크기의 최대크기를 초과하는 경우, IOException이 발생된다.
			 그러므로 Exception 처리를 해주어야 한다.                
			 */
				mtreq = new MultipartRequest(req, imagesDir, 10*1024*1024, "UTF-8", new DefaultFileRenamePolicy());
				System.out.println("ProductRegisterEndAction 3/7 success");	
			} catch (IOException e) {	// 업로드 용량이 10MB가 넘어갔을 경우
				req.setAttribute("msg", "파일 첨부 용량은 10MB이하여야 합니다.");
				req.setAttribute("loc", "productRegister.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				return;
			
			}
			
//		3. 파일 업로드 후 상품정보(상품명, 정가, 제품설명...)를  DB jsp_product 테이블에 insert 
			ProductDAO pdao = new ProductDAO();

//			1) 새로운 제품 등록시 HTML form 에서 입력한 값들을 얻어오기 
		    String pname = mtreq.getParameter("pname");
		    String pcategory_fk = mtreq.getParameter("pcategory_fk");
		    String pcompany = mtreq.getParameter("pcompany");
							
		    String pimage1 = mtreq.getFilesystemName("pimage1");
		    String pimage2 = mtreq.getFilesystemName("pimage2");
//			업로드된 시스템 파일 이름; getFilesystemName("form에서의 첨부파일 name명"); -->	업로드 된 파일이 없는 경우에는 null을 반환
		    
			/*
			   <<참고>> 
			   ※ MultipartRequest 메소드

		        ---------------------------------------------------------------------------
				  반환타입                         설명
				---------------------------------------------------------------------------
				 Enumeration       getFileNames()
				
						                     업로드 된 파일들에 대한 이름을 Enumeration객체에 String형태로 담아 반환한다. 
						                     이때의 파일 이름이란 클라이언트 사용자에 의해서 선택된 파일의 이름이 아니라, 
						                     개발자가 form의 file타임에 name속성으로 설정한 이름을 말한다. 
						                     만약 업로드 된 파일이 없는 경우엔 비어있는 Enumeration객체를 반환한다.
				----------------------------------------------------------------------------
				 String            getContentType(String name)
				
						                     업로드 된 파일의 컨텐트 타입을 얻어올 수 있다. 
						                     이 정보는 브라우저로부터 제공받는 정보이다. 
						                     이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
				----------------------------------------------------------------------------
				 File              getFile(String name)
				
						                     업로드 된 파일의 File객체를 얻는다. 
						                     우리는 이 객체로부터 파일사이즈 등의 정보를 얻어낼 수 있다. 
						                     이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
				----------------------------------------------------------------------------
				 String            getFilesystemName(String name)
				
						                     시스템의 파일 이름을 반환한다. 
						                     이때 업로드 된 파일이 없는 경우에는 null을 반환한다.
				----------------------------------------------------------------------------
				 String            getOriginalFimeName(String name)
				
						                     중복 파일 처리 인터페이스에 의해 변환되기 이전의 파일 이름을 반환한다. 
						                     이때업로드 된 파일이 없는 경우에는 null을 반환한다.
				----------------------------------------------------------------------------
				 String            getParameter(String name)
				
						                     지정한 파라미터의 값을 반환한다. 
						                     이때 전송된 값이 없을 경우에는 null을 반환한다.
				----------------------------------------------------------------------------
				 Enumeration       getParameternames()
				
						                     폼을 통해 전송된 파라미터들의 이름을 Enumeration객체에 String 형태로 담아 반환한다. 
						                     전송된 파라미터가 없을 경우엔 비어있는 Enumeration객체를 반환한다
				----------------------------------------------------------------------------
				 String[]          getparameterValues(String name)
				
						                     동일한 파라미터 이름으로 전송된 값들을 String배열로 반환한다. 
						                     이때 전송된파라미터가 없을 경우엔 null을 반환하게 된다. 
						                     동일한 파라미터가 단 하나만 존재하는 경우에는 하나의 요소를 지닌 배열을 반환하게 된다.
				----------------------------------------------------------------------------
			*/			
			String pqty = mtreq.getParameter("pqty");
			String price = mtreq.getParameter("price"); 
			String saleprice = mtreq.getParameter("saleprice");
			String pspec = mtreq.getParameter("pspec");
			String pcontent = mtreq.getParameter("pcontent").replaceAll("\r\n", "<br/>");
			String point = mtreq.getParameter("point");
			System.out.println("ProductRegisterEndAction 4/7 success");
			
//			2) pnum까지 채번하고, 위에서 가져온 파라미터 값을 productVO객체에 set
			ProductVO pvo = new ProductVO();
			int pnum = pdao.getPnumOfProduct();	// 시퀀스 값을 채번해옴
			
			pvo.setPnum(pnum);
			pvo.setPname(pname);
			pvo.setPcategory_fk(pcategory_fk);
			pvo.setPcompany(pcompany);
			pvo.setPimage1(pimage1);
			pvo.setPimage2(pimage2);
			pvo.setPqty(Integer.parseInt(pqty));
			pvo.setPrice(Integer.parseInt(price));
			pvo.setSaleprice(Integer.parseInt(saleprice));
			pvo.setPspec(pspec);
			pvo.setPcontent(pcontent);
			pvo.setPoint(Integer.parseInt(point));
			
//			3) 제품객체정보를 jsp_product 테이블에 insert
			int n = pdao.productInsert(pvo);
			int m = 0;
			System.out.println("ProductRegisterEndAction 5/7 success");
			
//			4) 제품정보에 추가 이미지파일 정보를 insert
			String str_attachCount = mtreq.getParameter("attachCount"); // hidden input태그 안에 있는 값(파일 개수)
			if(!"".equals(str_attachCount)) {
				int attachCount = Integer.parseInt(str_attachCount);
				System.out.println("ProductRegisterEndAction 6/7 success");
				for(int i=0; i<attachCount; i++) {
					String attachFilename = mtreq.getFilesystemName("attach"+i);
					m = pdao.product_imagefile_Insert(pnum, attachFilename);
					if(m==0) break;
				}// end of for
				
			} // end of if
			
			String msg = "";
			String loc = "";
			if(n*m==1) {
				msg = "제품 등록 성공!";
				loc = req.getContextPath()+"/mallHome.do";
			}
			else {
				msg = "제품 등록 실패!";
				loc = req.getContextPath()+"/mallHome.do";
			}
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			System.out.println("ProductRegisterEndAction 7/7 success");
		}
		
	}

}
