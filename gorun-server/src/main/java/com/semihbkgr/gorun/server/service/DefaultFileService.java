package com.semihbkgr.gorun.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class DefaultFileService implements FileService {

    private final Path rootPath;

    public DefaultFileService(@Value("${file.root-path:files}") String rootFolderPath) {
        this.rootPath = Path.of(rootFolderPath);
    }

    @PostConstruct
    void createRootDirIfNotExists() throws IOException {
        if (!Files.exists(rootPath)) {
            log.info("Root dir does not exists, Path : {}", rootPath);
            Files.createDirectories(rootPath);
            log.info("Root dir has been created successfully, Path : {}", rootPath);
        } else
            log.info("Root dir already exists, Path : {}", rootPath);
    }

    @PreDestroy
    void clearAllFilesAndDeleteRootDir() throws IOException {
        Files.delete(rootPath);
    }

    @Override
    public String createFile(String fileName, String content) {
        try {
            Path filePath = rootPath.resolve(fileName);
            Files.createFile(filePath);
            Files.write(filePath, content.getBytes());
            return filePath.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FileCreateException");
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Path filePath = rootPath.resolve(fileName);
            Files.delete(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FileDeleteException");
        }
    }

    @Override
    public Path getRootDirPath() {
        return this.rootPath;
    }


}
