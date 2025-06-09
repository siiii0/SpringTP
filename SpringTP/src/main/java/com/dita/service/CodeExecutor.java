package com.dita.service;

import java.io.*;
import java.util.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CodeExecutor {

    // 실행할 언어에 맞는 코드 실행 메서드
    public String executeCode(String code, String language, String input) throws IOException, InterruptedException {
        switch (language.toLowerCase()) {
            case "java":
                return executeJava(code, input);
            case "cpp":
                return executeCpp(code, input);
            case "py":
                return executePython(code, input);
            default:
                return "지원하지 않는 언어입니다.";
        }
    }

    // Java 코드 실행
    private String executeJava(String code, String input) throws IOException, InterruptedException {
        // Java 소스 코드 저장
        File javaFile = new File("Solution.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile))) {
            writer.write(code);
        }

        // 컴파일
        Process compileProcess = Runtime.getRuntime().exec("javac Solution.java");
        compileProcess.waitFor();

        // 실행
        Process runProcess = Runtime.getRuntime().exec("java Solution");
        BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
        inputWriter.write(input);  // 문제 입력값 전달
        inputWriter.flush();
        inputWriter.close();

        // 출력값 받기
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
        // C++ 소스 코드 저장
        File cppFile = new File("Solution.cpp");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cppFile))) {
            writer.write(code);
        }

        // 컴파일
        Process compileProcess = Runtime.getRuntime().exec("g++ Solution.cpp -o Solution");
        compileProcess.waitFor();

        // 실행
        Process runProcess = Runtime.getRuntime().exec("./Solution");
        BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
        inputWriter.write(input);  // 문제 입력값 전달
        inputWriter.flush();
        inputWriter.close();

        // 출력값 받기
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
        // Python 코드 실행
        Process process = new ProcessBuilder("python", "-c", code).start();
        BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        inputWriter.write(input);  // 문제 입력값 전달
        inputWriter.flush();
        inputWriter.close();

        // 출력값 받기
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }
}
