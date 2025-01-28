package com.application.sharedlibrary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ResourceLoaderService {
  private final ResourceLoader resourceLoader;

  @Autowired
  public ResourceLoaderService(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public String readFileFromResources(String fileName) throws Exception {
    Resource resource = resourceLoader.getResource("classpath:" + fileName);
    return Files.readString(Path.of(resource.getURI()));
  }
}
