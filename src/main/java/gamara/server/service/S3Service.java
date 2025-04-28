package gamara.server.service;

import gamara.server.common.Response;
import gamara.server.common.exception.ErrorCode;
import gamara.server.common.exception.ImageException;
import gamara.server.converter.ImageConverter;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    private static final String BUCKET = System.getenv("AWS_S3_BUCKET");
    private final S3Operations s3Operations;

    public String uploadFile(MultipartFile multipartFile, String key) throws ImageException {
        String fileExtension = ImageConverter.extractFileExtension(multipartFile);
        if (!ImageConverter.isImageFileExtension(fileExtension)) {
            throw new ImageException(ErrorCode.INVALID_IMAGE_FILE_FORMAT);
        }

        File originalFile = ImageConverter.convert(multipartFile);
        File webpFile = ImageConverter.convertToWebp(originalFile);
        String filename = key + "/" + UUID.randomUUID() + ".webp";

        try (InputStream is = new FileInputStream(webpFile)) {
            ObjectMetadata metadata = ObjectMetadata.builder()
                    .contentType("image/webp")
                    .build();

            S3Resource resource = s3Operations.upload(BUCKET, filename, is, metadata);

            return resource.getURL().toString();
        } catch (IOException e) {
            throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }

    @Transactional
    public Response<?> deleteFile(String key) throws ImageException { //TODO: delete 사용할거면 iam 수정해서 s3 삭제 권한 허용해야 함
        try {
            s3Operations.deleteObject(BUCKET, key);
            return Response.createSuccessWithNoData();
        } catch (Exception e) {
            throw new ImageException(ErrorCode.IMAGE_FILE_DELETE_FAIL);
        }
    }
}