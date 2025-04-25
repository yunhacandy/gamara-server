package gamara.server.service;

import gamara.server.common.Response;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    private static final String BUCKET = System.getenv("AWS_S3_BUCKET");
    private final S3Operations s3Operations;

    @Transactional
    public Response<?> uploadFile(MultipartFile multipartFile, String dir) throws IOException {
        String contentType = multipartFile.getContentType();
        if (!MediaType.IMAGE_PNG.toString().equals(contentType) &&
                !MediaType.IMAGE_JPEG.toString().equals(contentType)) {
            return Response.createError("Only image files can be uploaded");
        }

        String filename = dir + "/" + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        try (InputStream is = multipartFile.getInputStream()) {
            S3Resource resource = s3Operations.upload(
                    BUCKET,
                    filename,
                    is,
                    ObjectMetadata.builder()
                            .contentType(contentType)
                            .build()
            );

            String uploadedUrl = resource.getURL().toString();
            return Response.createSuccess(uploadedUrl);
        }
    }
}