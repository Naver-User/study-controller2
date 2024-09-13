package org.zerock.myapp.domain;

import lombok.Data;


@Data
public class PersonDTO {	// DTO: 즉, 전송파라미터 수집용 객체
	// (1) 각 필드의 이름은 전송파라미터의 이름과 같게 한다!
	// (2) 각 필드의 타입은, 자동형변환(promotion)이 가능한 한도
	//     내에서 자유롭게 지정하시면 됩니다.
	private String name;
	private Integer age;

	
	
} // end class
