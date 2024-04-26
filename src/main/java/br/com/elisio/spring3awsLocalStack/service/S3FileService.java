package br.com.elisio.spring3awsLocalStack.service;


import br.com.elisio.spring3awsLocalStack.Exception.FileListenerException;
import com.amazonaws.services.s3.AmazonS3;
import io.awspring.cloud.core.io.s3.SimpleStorageProtocolResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class S3FileService {

    @Value("s3-helloworld")
    private String directory;

    @Autowired
    private ResourceLoader resourceLoader;

    private ResourcePatternResolver resourcePatternResolver;


    @Autowired
    public void configureResourceLoader(AmazonS3 amazonS3, DefaultResourceLoader resourceLoader) {
        SimpleStorageProtocolResolver simpleStorageProtocolResolver = new SimpleStorageProtocolResolver(amazonS3);
        // As we are calling it by hand, we must initialize it properly.
        simpleStorageProtocolResolver.afterPropertiesSet();
        resourceLoader.addProtocolResolver(simpleStorageProtocolResolver);
    }

    public boolean isFileExists(String file) {
        try {
            Resource resource = this.resourceLoader
                    .getResource(String.format("s3://%s/%s", directory, file));
            return resource.contentLength() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<Resource> searchFile(String name, boolean exact) {
        Resource[] resources = null;
        try {
            if (exact)
                resources = this.resourcePatternResolver.getResources(String.format("s3://%s/%s", directory, name));
            else
                resources = this.resourcePatternResolver.getResources(String.format("s3://%s/%s*.*", directory, name));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return Arrays.asList(resources).stream().sorted(Comparator.comparing(Resource::getFilename)).collect(Collectors.toList());
    }

    public void saveFile(InputStream from, String to) throws FileListenerException {
        Resource resource = this.resourceLoader.getResource(String.format("s3://%s/%s", directory, to));
        WritableResource writableResource = (WritableResource) resource;

        try (OutputStream outputStream = writableResource.getOutputStream()) {
            from.transferTo(outputStream);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new FileListenerException(ex.getMessage(), ex);
        }
    }

    public String contentFile(String file) {
        try {
            Resource resource = resourceLoader.getResource(String.format("s3://%s/%s", directory, file));
            return new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.ISO_8859_1))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception ex) {
            return null;
        }
    }
}
