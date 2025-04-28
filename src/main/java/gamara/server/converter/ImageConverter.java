package gamara.server.converter;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import gamara.server.common.exception.AppException;
import gamara.server.common.exception.ErrorCode;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class ImageConverter {

    private static final List<String> IMAGE_FILE_EXTENSIONS = List.of("png", "jpg", "jpeg", "heif");

    public static String extractFileExtension(MultipartFile file) throws AppException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new AppException(ErrorCode.FILE_EXTENSION_FAULT);
        }

        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    public static boolean isImageFileExtension(String fileExtension) {
        return IMAGE_FILE_EXTENSIONS.contains(fileExtension.toLowerCase());
    }

    public static File convert(MultipartFile file) throws AppException {
        String fileExtension = extractFileExtension(file);
        File convertFile = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + "."
                + fileExtension);    //TODO: java.io.tmpdir가 맞는지 user.dir가 맞는지 확인

        try {
            FileOutputStream fos = new FileOutputStream(convertFile);
            fos.write(file.getBytes());
            fos.close();
            return convertFile;
        } catch (IOException e) {
            throw new AppException(ErrorCode.IMAGE_CONVERT_FAIL);
        }
    }

    public static File convertToWebp(File originalFile) throws AppException {
        try {
            String parentPath = originalFile.getParent();
            String fileNameWithoutExtension = originalFile.getName().replaceFirst("[.][^.]+$", "");

            File webpFile = ImmutableImage.loader()
                    .fromFile(originalFile)
                    .output(WebpWriter.DEFAULT, new File(parentPath, fileNameWithoutExtension + ".webp"));

            if (originalFile.exists()) {
                boolean deleted = originalFile.delete();
                if (!deleted) {
                    throw new AppException(ErrorCode.FILE_DELETE_FAIL);
                }
            }

            return webpFile;
        } catch (IOException e) {
            throw new AppException(ErrorCode.WEBP_CONVERT_FAIL);
        }
    }
}
