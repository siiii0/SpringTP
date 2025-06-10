package com.dita.controller;

import com.dita.domain.Question;
import com.dita.domain.CodeRequest;
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
@RequestMapping("/submit_code")
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
    public Map<String, Object> submitCode(@RequestBody CodeRequest coderequest) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("code : " + coderequest.getCode());
        System.out.println(coderequest.getLanguage());
        System.out.println(coderequest.getInput());
        System.out.println(coderequest.getQid());
        System.out.println(coderequest.getUserId());
        System.out.println(coderequest.getUserType());
        
        // 문제 정보 조회
        Question question = questionRepository.findById(coderequest.getQid()).orElse(null);
        if (question == null) {
            response.put("error", "문제를 찾을 수 없습니다.");
            return response;
        }

        // 해당 문제의 모든 정답 예시 조회 (여러 개의 정답 예시를 가져오는 방식)
        Optional<Q_Language> qLanguages = qLangRepository.findByqIdAndQLanguage(question, coderequest.getLanguage());
        if (qLanguages.isEmpty()) {
            response.put("error", "정답 예시를 찾을 수 없습니다.");
            return response;
        }

        // 복합키를 사용하여 사용자 조회
        User_id_type userIdType = new User_id_type(coderequest.getUserId(), coderequest.getUserType());
        Optional<User> userOpt = userRepository.findById(userIdType);

        if (!userOpt.isPresent()) {
            response.put("error", "사용자를 찾을 수 없습니다.");
            return response;
        }

        User user = userOpt.get();

        // 코드 실행
        try {
            response.clear();
            String userCode = coderequest.getCode();
            if(coderequest.getCode().isEmpty())
            	userCode = " ";
            
            String result = executeUserCode(userCode, coderequest.getLanguage(), coderequest.getInput());
            String answer = executeAnswerCode(qLanguages.get().getQ_answer(), coderequest.getLanguage(), coderequest.getInput());
            
            System.out.println(qLanguages.get().getQ_answer());
            System.out.println(coderequest.getCode());
            System.out.println(result);
            System.out.println(answer);
            // 정답 예시와 비교
            boolean isCorrect = false;
            if(answer.equals(result))
            	isCorrect = true;
            

            // 제출 기록 저장 (Submissions 테이블)
            Submissions submission = new Submissions();
            submission.setUser(user);  // 복합키를 사용한 User 객체 설정
            submission.setQId(question);               // 문제 정보
            submission.setS_language(coderequest.getLanguage()); // 언어 정보
            submission.setS_code(coderequest.getCode()); // 사용자가 제출한 코드
            submission.setS_isCorrect(isCorrect ? "Y" : "N");  // 정답 여부 (Y: 정답, N: 오답)
            submission.setS_runTime(1000);              // 실행 시간 (예시: 1000ms)
            submission.setSubmitted_at(LocalDateTime.now()); // 제출 일시
            submissionsRepository.save(submission); // 데이터베이스에 저장

            response.put("isCorrect", isCorrect);
            response.put("result", result);

        } catch (Exception e) {
            response.put("error", "코드 실행 중 오류가 발생했습니다.");
            System.err.println(e);
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
     // 현재 작업 디렉토리 확인
        System.out.println("현재 작업 디렉토리: " + new File(".").getAbsolutePath());
        
        // Java 코드 파일 작성
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile))) {
            writer.write(code);
        }

        // 컴파일
        Process compileProcess = Runtime.getRuntime().exec("javac Solution.java");
        int compileExitCode = compileProcess.waitFor();

        if (compileExitCode != 0) {
            // 컴파일 오류 발생 시
            return "컴파일 오류가 발생했습니다.";
        }

        // 실행
        Process runProcess = Runtime.getRuntime().exec("java Solution");
        
        // 입력값 전달
        try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
            inputWriter.write(input);  // 문제 입력값 전달
            inputWriter.flush();
        }

        // 출력 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // 오류 출력 읽기 (컴파일 또는 실행 오류)
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        if (errorOutput.length() > 0) {
            return "실행 오류: " + errorOutput.toString();
        }
        
        if(javaFile.exists())
        	javaFile.delete();

        return output.toString();  // 정상적으로 실행된 출력 반환
    }


 // C++ 코드 실행
    private String executeCpp(String code, String input) throws IOException, InterruptedException {
        File cppFile = new File("Solution.cpp");

        // 현재 작업 디렉토리 확인
        System.out.println("현재 작업 디렉토리: " + new File(".").getAbsolutePath());

        // C++ 코드 파일 작성
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cppFile))) {
            writer.write(code);
        }

        // 컴파일
        Process compileProcess = Runtime.getRuntime().exec("g++ Solution.cpp -o Solution");
        int compileExitCode = compileProcess.waitFor();

        if (compileExitCode != 0) {
            // 컴파일 오류 발생 시
            return "컴파일 오류가 발생했습니다.";
        }

        // 실행
        Process runProcess = Runtime.getRuntime().exec("./Solution");

        // 입력값 전달
        try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
            inputWriter.write(input);  // 문제 입력값 전달
            inputWriter.flush();
        }

        // 출력 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // 오류 출력 읽기 (컴파일 또는 실행 오류)
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        if (errorOutput.length() > 0) {
            return "실행 오류: " + errorOutput.toString();
        }

        if (cppFile.exists()) cppFile.delete();

        return output.toString();  // 정상적으로 실행된 출력 반환
    }


 // Python 코드 실행
    private String executePython(String code, String input) throws IOException, InterruptedException {
        // 임시 Python 스크립트 파일 작성
        File pythonFile = new File("Solution.py");

        // 현재 작업 디렉토리 확인
        System.out.println("현재 작업 디렉토리: " + new File(".").getAbsolutePath());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pythonFile))) {
            writer.write(code);
        }

        // Python 코드 실행
        Process process = new ProcessBuilder("python", pythonFile.getAbsolutePath()).start();

        // 입력값 전달
        try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            inputWriter.write(input);  // 문제 입력값 전달
            inputWriter.flush();
        }

        // 출력 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // 오류 출력 읽기 (실행 오류)
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        if (errorOutput.length() > 0) {
            return "실행 오류: " + errorOutput.toString();
        }

        if (pythonFile.exists()) pythonFile.delete();

        return output.toString();  // 정상적으로 실행된 출력 반환
    }
    


    
    
    
    // 정답 코드를 실행하는 메서드
    private String executeAnswerCode(String code, String language, String input) throws IOException, InterruptedException {
        switch (language.toLowerCase()) {
            case "java":
                return executeAJava(code, input);
            case "cpp":
                return executeACpp(code, input);
            case "py":
                return executeAPython(code, input);
            default:
                throw new UnsupportedOperationException("지원하지 않는 언어입니다.");
        }
    }

    // Java 코드 실행
    private String executeAJava(String code, String input) throws IOException, InterruptedException {
        File javaAFile = new File("Main.java");
     // 현재 작업 디렉토리 확인
        System.out.println("현재 작업 디렉토리: " + new File(".").getAbsolutePath());
        
        // Java 코드 파일 작성
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(javaAFile))) {
            writer.write(code);
        }

        // 컴파일
        Process compileProcess = Runtime.getRuntime().exec("javac Main.java");
        int compileExitCode = compileProcess.waitFor();

        if (compileExitCode != 0) {
            // 컴파일 오류 발생 시
            return "컴파일 오류가 발생했습니다.";
        }

        // 실행
        Process runProcess = Runtime.getRuntime().exec("java Main");
        
        // 입력값 전달
        try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
            inputWriter.write(input);  // 문제 입력값 전달
            inputWriter.flush();
        }

        // 출력 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // 오류 출력 읽기 (컴파일 또는 실행 오류)
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        if (errorOutput.length() > 0) {
            return "실행 오류: " + errorOutput.toString();
        }

        if(javaAFile.exists())
        	javaAFile.delete();
        return output.toString();  // 정상적으로 실행된 출력 반환
    }


 // C++ 코드 실행
    private String executeACpp(String code, String input) throws IOException, InterruptedException {
        File cppFile = new File("Main.cpp");

        // 현재 작업 디렉토리 확인
        System.out.println("현재 작업 디렉토리: " + new File(".").getAbsolutePath());

        // C++ 코드 파일 작성
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cppFile))) {
            writer.write(code);
        }

        // 컴파일
        Process compileProcess = Runtime.getRuntime().exec("g++ Main.cpp -o Main");
        int compileExitCode = compileProcess.waitFor();

        if (compileExitCode != 0) {
            // 컴파일 오류 발생 시
            return "컴파일 오류가 발생했습니다.";
        }

        // 실행
        Process runProcess = Runtime.getRuntime().exec("./Main");

        // 입력값 전달
        try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()))) {
            inputWriter.write(input);  // 문제 입력값 전달
            inputWriter.flush();
        }

        // 출력 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // 오류 출력 읽기 (컴파일 또는 실행 오류)
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        if (errorOutput.length() > 0) {
            return "실행 오류: " + errorOutput.toString();
        }

        if (cppFile.exists()) cppFile.delete();

        return output.toString();  // 정상적으로 실행된 출력 반환
    }


 // Python 코드 실행
    private String executeAPython(String code, String input) throws IOException, InterruptedException {
        // 임시 Python 스크립트 파일 작성
        File pythonFile = new File("Main.py");

        // 현재 작업 디렉토리 확인
        System.out.println("현재 작업 디렉토리: " + new File(".").getAbsolutePath());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pythonFile))) {
            writer.write(code);
        }

        // Python 코드 실행
        Process process = new ProcessBuilder("python", pythonFile.getAbsolutePath()).start();

        // 입력값 전달
        try (BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            inputWriter.write(input);  // 문제 입력값 전달
            inputWriter.flush();
        }

        // 출력 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // 오류 출력 읽기 (실행 오류)
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        if (errorOutput.length() > 0) {
            return "실행 오류: " + errorOutput.toString();
        }

        if (pythonFile.exists()) pythonFile.delete();

        return output.toString();  // 정상적으로 실행된 출력 반환
    }
    


}
