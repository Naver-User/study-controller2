package org.zerock.myapp.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zerock.myapp.domain.PersonDTO;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


//@Log4j2
@Slf4j

@NoArgsConstructor

@RequestMapping("/controller/")

// MVC 패턴대로, View까지 수행시키는 컨트롤러
// 우리가 사용하는 View (즉, Template Engine)는 JSP이다!!!

@Controller
public class ReturnTypesController {	// POJO
	
	// * MVC 패턴에서 Controller는 비지니스 로직의 결과인 Model뿐만
	//   아니라, "호출할 View 의 이름"까지 반환해야 할 의무가 있다!!!

	
	//--------------------------------------------------------//
	// 1. void return type (매개변수도 없는 경우)
	//--------------------------------------------------------//
	// (1) 핸들러의 리턴타입이 바로 "호출할 View의 이름"을 결정한다!!
	// (2) 핸들러의 리턴타입이 void가 되면, 호출URI가 바로 뷰의 이름이 된다!!!
	// (3) 호출URI가 뷰의 이름이 되고, 이 이름 앞/뒤로 접두사/접미사가 붙는다!!!
	// (4) JSP에서 기본 접두사 : /WEB-INF/views/
	// (5) JSP에서 기본 접미사 : .jsp
	
	@GetMapping("/void")
	void returnVoid() {
		log.trace("returnVoid() invoked.");
		
		// 결국 위의 (1) ~ (5)에 따라, 최종 "호출될 뷰의 경로"는 아래와 같습니다.
		// 접두사 + 호출URI + 접미사 = /WEB-INF/views/호출URI.jsp
		//                           = /WEB-INF/views/controller/void.jsp
	} // returnVoid


	//--------------------------------------------------------//
	// 2. String return type (핸들러 선언의 정석)
	//--------------------------------------------------------//
	// String 타입의 리턴타입이라? 핸들러가 뷰의 이름을 결정해서 반환하겠다!
	// 라는 의미가 됩니다.
	@GetMapping("/string")
	String returnString() {
		log.trace("returnString() invoked.");
		
		// 핸들러가 최종적으로 호출할 뷰의 이름을 반환한다!-> 이게 정석이다!
		// 최종 호출될 뷰의 경로명:
		//  = 접두사 + "Yoseph" + 접미사
		//  = /WEB-INF/views/ + Yoseph + .jsp
		//  = /WEB-INF/views/Yoseph.jsp
		return "Yoseph";	// 여러분의 이름을 뷰의 이름으로 반환하세요!
	} // returnString


	//--------------------------------------------------------//
	// 3. String return type (핸들러 선언의 정석)
	//    with Spring command object (모든 전송파라미터를 자동수집한 DTO객체)
	//--------------------------------------------------------//
	// String 타입의 리턴타입이라? 핸들러가 뷰의 이름을 결정해서 반환하겠다!
	// 라는 의미가 됩니다.
	@GetMapping(
		path="/stringWithCommandObject", 
		params= { "name", "age" })
	String returnStringWithCommandObject(PersonDTO dto) {
		log.trace("returnStringWithCommandObject() invoked.");
		
		// 핸들러가 최종적으로 호출할 뷰의 이름을 반환한다!-> 이게 정석이다!
		// 최종 호출될 뷰의 경로명:
		//  = 접두사 + "Yoseph" + 접미사
		//  = /WEB-INF/views/ + Yoseph + .jsp
		//  = /WEB-INF/views/Yoseph.jsp
		return "Yoseph";	// 여러분의 이름을 뷰의 이름으로 반환하세요!
	} // returnStringWithCommandObject


	//--------------------------------------------------------//
	// 4. String return type (핸들러 선언의 정석)
	//    with "redirect:" (리다이렉션을 수행하는 문자열 반환)
	//--------------------------------------------------------//
	// 이 경우에는, 리턴타입인 문자열이 최종 호출될 뷰의이름을 반환하는게
	// 아니라!!!, 바로 "리다이렉션 수행 문자열"을 반환하는 것입니다.
	// 그러면, Spring Boot 는 "redirect:"이라는 접두사를 보고,
	// 뷰를 호출하는게 아니라, 리다이렉션 처리를 수행하게 됩니다.
	@GetMapping("/returnStringForRedirection")
	String returnStringForRedirection() {
		log.trace("returnStringForRedirection() invoked.");
		
		// 리다이렉션의 특징:
		// (1) 무조건 GET 방식으로 새로운 요청을 발생시킵니다.
		// (2) 새로운 Target URI로 요청이 발생할 때, 임시상자인
		//     RedirectAttributes (줄여서, rttrs)를 이용하여,
		//     전송파라미터를 만들어 줄 수가 있습니다.
		// (3) 웹브라우저의 URL주소창의 주소가, 리다이렉션 URL로 변경됨
//		return "redirect:/redirect";	// 1-1, XX : Base URI 없어서...
		
		// 상세URI에서, '/'를 빼야, 이 컨트롤러의 Base URI + 아래의
		// 상세URI가 되어, 잘 찾게 됩니다.
		return "redirect:redirect";		// 1-2, OK : 가장 좋은방법
		
		// 중요: 리다이렉션 수행시, 지정된 URI에는 Base URI도 포함되어야 함
//		return "redirect:/controller/redirect";	// 2, OK
		
		// 브라우저 주소창에 마치 사람이 완전한 새로운 주소를 넣어서
		// 요청을 보내는 것처럼, URL로지정하였습니다.
		// 후에 만약, 주소나 포트번호가 바뀌면, 아래의 하드코딩된
		// 주소부분을 모두 찾아내어 수정해야 하기 때문에,
		// 당연히 아래와 같이 지정해서는 안됩니다!!! (***)
//		return "redirect:http://localhost:8080/controller/redirect"; // 3
	} // returnStringForRedirection


	//--------------------------------------------------------//
	// 5. String return type (핸들러 선언의 정석)
	//    with "forward:" (Request Forwarding을 수행하는 문자열 반환)
	//--------------------------------------------------------//
	// 이 경우에는, 리턴타입인 문자열이 최종 호출될 뷰의이름을 반환하는게
	// 아니라!!!, 바로 "Request Forwarding 수행 문자열"을 반환하는 것입니다.
	// 그러면, Spring Boot 는 "forward:"이라는 접두사를 보고,
	// 뷰를 호출하는게 아니라, Request Forwarding 처리를 수행하게 됩니다.
	@PostMapping("/returnStringForForward")
	String returnStringForForward() {
		log.trace("returnStringForForward() invoked.");
		
		// Request Forwarding 의 특징:
		// (1) 대부분 POST 방식에서 하나의 요청에 대한 처리를
		//     여러 핸들러가 차례대로 나누어 처리할 때 사용
		// (2) 그 다음 수행될 핸들러에게 전송할 데이터가 있다면,
		//     이때에는 보통 Request Scope 공유속성으로 전달합니다.
		//     결국 이경우에도 Model 타입의 addAttribute(key, value)
		//     메소드를 이용해서 데이터를 전달해주셔도 같습니다.
		// (3) 웹브라우저의 URL주소창의 주소가 전혀 바뀌지 않음
		return "forward:forward";
	} // returnStringForForward


	//--------------------------------------------------------//
	// 6. 문자열로 뷰의 이름을 반환하지 않고, 자바 객체를 리턴
	//    하는 경우도 가능합니다. 즉, 반환된 자바객체를 XML이나
	//    JSON으로 변환하여 반환하는 경우로, @RestController의
	//    기능을 특정 핸들러에서 그대로 똑같이 수행할 수 있습니다
	//--------------------------------------------------------//
	// 새로운 스프링의 어노테이션으로, @ResponseBody 란 것을 사용하면
	// 마치, 아래와 2가지와 같습니다:
	//
	//  (1) @RestController == @Controller + @ResponseBody
	//  (2) @RestController == @Controller + ResponseEntity<E>
	//
	// 참고로 위 (2)에서 새로이 나온 스프링 타입인 "ResponseEntity<E>"
	// 는 응답메시지의 응답코드, 헤더, 바디 모두 조작이 가능합니다!
	
	@GetMapping("/returnJavaObject")
//	(1) @RestController == @Controller + @ResponseBody
	@ResponseBody
	PersonDTO returnJavaObject() { // XML 또는 JSON으로 변환되어 반환
		log.trace("returnJavaObject() invoked.");
		
		PersonDTO dto = new PersonDTO();
		dto.setName("Yoseph");
		dto.setAge(23);
		
		return dto;
	} // returnJavaObject
	
	
	@GetMapping("/returnResponseEntity")
//	(2) @RestController == @Controller + ResponseEntity<E>
	ResponseEntity<String> returnResponseEntity() {
		log.trace("returnResponseEntity() invoked.");
		
		String json = "{ 'name': 'Yoseph', 'age': 23 }";
		
		HttpHeaders headers = new HttpHeaders();
		
//		headers.add("새로운헤더명", "값");	// 새로운 헤더의 추가
//		headers.remove("삭제할헤더명");	// 기존 헤더의 삭제
		
		headers.add("Content-Type", "application/json; charset=utf8");
		
//		return new ResponseEntity<>(바디컨텐츠, 헤더, 응답코드);
		return new ResponseEntity<>(json, headers, HttpStatus.OK);
	} // returnResponseEntity
	
	
	@GetMapping("/redirect")
	String redirect() {
		log.trace("redirect() invoked.");
		
		// 반환할 최종 뷰의 이름
		return "redirect";
	} // redirect
	
	
	@PostMapping("/forward")
	String forward() {
		log.trace("forward() invoked.");
		
		// 반환할 최종 뷰의 이름
		return "forward";
	} // forward
	

} // end class
