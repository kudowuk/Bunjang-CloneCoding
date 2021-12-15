package com.example.demo.config;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// 크로스 도메인 접속 허용 설정
@CrossOrigin("*")


// RestController = rest api 요청, Controller = view 연결
@RestController
public class ModuleApiController {

    // GET 방식 : map
    // 경로 지정 : http://localhost:8000/resource/asset/?name=PT20210715181929_black_pw_icon.png]
    // 로직 : 서버 로컬 PC에 저장된 이미지 확인 -> 저장된 이미지 파일이 존재하는 경우 -> 브라우저에서 이미지 표시
    @GetMapping("/resource/asset")
    public ResponseEntity<Resource> getImage(@RequestParam Map<String, String> param) {
        System.out.println("\n");
        System.out.println("=======================================");
        System.out.println("[ModuleApiController] : [getImage] : [start]");
        System.out.println("[request name] : " + String.valueOf(param.get("name")));
        System.out.println("=======================================");
        System.out.println("\n");

        // 시스템 os 정보 확인 변수 선언
        String os = System.getProperty("os.name").toLowerCase();

        // 사진이 저장된 폴더 경로 변수 선언
        String imageRoot = "";

        // os 정보 확인 및 사진이 저장된 서버 로컬 경로 지정 실시
        // 로컬 : Home/Resource/assets 폴더는 이미지 파일을 공통적으로 저장 관리
        if (os.contains("win")) {
            imageRoot = "c:/resource/asset/"; // 윈도우 경로(디스크)
        } else if (os.contains("linux")) {
            imageRoot = "/home/resource/asset/"; // 리눅스 경로
        }

        // 서버 로컬 경로 + 파일 명 저장 실시
        imageRoot = imageRoot + String.valueOf(param.get("name"));
        System.out.println("\n");
        System.out.println("=======================================");
        System.out.println("[ModuleApiController] : [getImage] : [imageRoot]");
        System.out.println("[경로] : " + imageRoot);
        System.out.println("=======================================");
        System.out.println("\n");

        // Resorce를 사용해서 로컬 서버에 저장된 이미지 경로 및 파일 명을 지정
        Resource resource = new FileSystemResource(imageRoot);

        // 로컬 서버에 저장된 이미지 파일이 없을 경우
        if(!resource.exists()){
            System.out.println("FILE : NOT_FOUND");
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND); // 리턴 결과 반환 404
        }

        // 로컬 서버에 저장된 이미지가 있는 경우 로직 처리
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(imageRoot);
            // 인풋으로 들어온 파일명 .png / .jpg 에 맞게 헤더 타입 설정
            header.add("Content-Type", Files.probeContentType(filePath));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        // 이미지 리턴 실시 [브라우저에서 get 주소 확인 가능]
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }

    
}
