package gamara.server.service;

import gamara.server.common.Response;
import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
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

    @Transactional
    public Response<?> uploadFile(MultipartFile multipartFile, String key) throws IOException {
        String fileExtension = ImageConverter.extractFileExtension(multipartFile);
        if (!ImageConverter.isImageFileExtension(fileExtension)) {
            throw new AppException(ErrorCode.INVALID_IMAGE_FILE_FORMAT);
        }

        File originalFile = ImageConverter.convert(multipartFile);
        File webpFile = ImageConverter.convertToWebp(originalFile);
        String filename = key + "/" + UUID.randomUUID() + ".webp";

        try (InputStream is = new FileInputStream(webpFile)) {
            ObjectMetadata metadata = ObjectMetadata.builder()
                    .contentType("image/webp")
                    .build();

            S3Resource resource = s3Operations.upload(BUCKET, filename, is, metadata);

            String uploadedUrl = resource.getURL().toString();
            return Response.createSuccess(uploadedUrl);
        }
    }

    @Transactional
    public Response<?> deleteFile(String key) { //TODO: delete 사용할거면 iam 수정해서 s3 삭제 권한 허용해야 함
        try {
            s3Operations.deleteObject(BUCKET, key);
            return Response.createSuccessWithNoData();
        } catch (Exception e) {
            throw new AppException(ErrorCode.IMAGE_FILE_DELETE_FAIL);
        }
    }
}