package com.dita.controller;

import com.dita.domain.Question;
import com.dita.domain.Q_Language;
import com.dita.domain.Submissions;
import com.dita.domain.User;
import com.dita.domain.User_id_type;
import com.dita.repository.QuestionRepository;
import com.dita.repository.QLangRepository;  // 수정된 repository 이름
import com.dita.repository.SubmissionsRepository;
import com.dita.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/submit-code")
public class CodeSubmitController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QLangRepository qLangRepository;  // 수정된 repository 이름

    @Autowired
    private SubmissionsRepository submissionsRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Map<String, Object> submitCode(@RequestParam String code, 
                                          @RequestParam String language, 
                                          @RequestParam String input, 
                                          @RequestParam int qid, 
                                          @RequestParam String userId,
                                          @RequestParam String userType) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println(code);
        System.out.println(language);
        System.out.println(input);
        System.out.println(qid);
        System.out.println(userId);
        System.out.println(userType);
        
        // 문제 정보 조회
        Question question = questionRepository.findById(qid).orElse(null);
        if (question == null) {
            response.put("error", "문제를 찾을 수 없습니다.");
            return response;
        }

        // 해당 문제의 모든 정답 예시 조회 (여러 개의 정답 예시를 가져오는 방식)
        List<Q_Language> qLanguages = qLangRepository.findByqId(question);  // 수정된 부분
        if (qLanguages.isEmpty()) {
            response.put("error", "정답 예시를 찾을 수 없습니다.");
            return response;
        }

        // 복합키를 사용하여 사용자 조회
        User_id_type userIdType = new User_id_type(userId, userType);
        Optional<User> userOpt = userRepository.findById(userIdType);

        if (!userOpt.isPresent()) {
            response.put("error", "사용자를 찾을 수 없습니다.");
            return response;
        }

        User user = userOpt.get();

        // 코드 실행
        try {
            String result = executeUserCode(code, language, input);

            // 정답 예시와 비교
            boolean isCorrect = false;
            for (Q_Language qLanguage : qLanguages) {
                if (result.trim().equals(qLanguage.getQ_answer().trim())) {
                    isCorrect = true;
                    break;
                }
            }

            // 제출 기록 저장 (Submissions 테이블)
            Submissions submission = new Submissions();
            submission.setUser(user);  // 복합키를 사용한 User 객체 설정
            submission.setQid(question);               // 문제 정보
            submission.setS_language(language); // 언어 정보
            submission.setS_code(code); // 사용자가 제출한 코드
            submission.setS_isCorrect(isCorrect ? "Y" : "N");  // 정답 여부 (Y: 정답, N: 오답)
            submission.setS_runTime(1000);              // 실행 시간 (예시: 1000ms)
            submission.setSubmitted_at(LocalDateTime.now()); // 제출 일시
            submissionsRepository.save(submission); // 데이터베이스에 저장

            response.put("isCorrect", isCorrect);
            response.put("result", result);

        } catch (Exception e) {
            response.put("error", "코드 실행 중 오류가 발생했습니다.");
        }

        return response;
    }

    // 사용자 코드를 실행하는 메서드
    private String executeUserCode(String code, String language, String input) throws IOException, InterruptedException {
        switch (language.toLowerCase()) {
            case "java":
                return executeJava(code, input);
            case "cpp":
                return executeCpp(code, input);
            case "py":
                return executePython(code, input);
            default:
                throw new UnsupportedOperationException("지원하지 않는 언어입니다.");
        }
    }

    // Java 코드 실행
    private String executeJava(String code, String input) throws IOException, InterruptedException {
        File javaFile = new File("Solution.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile))) {
            writer.write(code);
        }

        Process compileProcess = Runtime.getRuntime().exec("javac Solution.java");
        compileProcess.waitFor();

        Process runProcess = Runtime.getRuntime().exec("java Solution");
        BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
        inputWriter.write(input);  // 문제 입력값 전달
        inputWriter.flush();
        inputWriter.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }

    // C++ 코드 실행
    private String executeCpp(String code, String input) throws IOException, InterruptedException {
        File cppFile = new File("Solution.cpp");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cppFile))) {
            writer.write(code);
        }

        Process compileProcess = Runtime.getRuntime().exec("g++ Solution.cpp -o Solution");
        compileProcess.waitFor();

        Process runProcess = Runtime.getRuntime().exec("./Solution");
        BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
        inputWriter.write(input);  // 문제 입력값 전달
        inputWriter.flush();
        inputWriter.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }

    // Python 코드 실행
    private String executePython(String code, String input) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("python", "-c", code).start();
        BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        inputWriter.write(input);  // 문제 입력값 전달
        inputWriter.flush();
        inputWriter.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }
}
