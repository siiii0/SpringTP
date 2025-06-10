package com.dita.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeRequest {
    private String code;        // 사용자 코드
    private String language;    // 언어
    private String input;       // 문제 입력값
    private int qid;            // 문제 ID
    private String userId;      // 사용자 ID (로그인 기능 필요 시)
    private String userType;    // 사용자 유형 (로그인 기능 필요 시)

}
