//package com.example.wms.infrastructure.service;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Service
//public class FileUploadService {
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    private final AmazonS3 amazonS3;
//
//    // InputStream, 파일 이름, 파일 크기를 받음
//    public String upload(InputStream inputStream, String originalFilename, long fileSize) throws IOException {
//        // S3에 저장할 파일 이름 생성
//        String s3FileName = UUID.randomUUID() + "-" + originalFilename;
//
//        // 메타데이터에 파일 크기를 설정
//        ObjectMetadata objMeta = new ObjectMetadata();
//        objMeta.setContentLength(fileSize);
//
//        // S3에 파일 업로드
//        amazonS3.putObject(bucket, s3FileName, inputStream, objMeta);
//
//        // 업로드된 파일의 URL 반환
//        return amazonS3.getUrl(bucket, s3FileName).toString();
//    }
//}
