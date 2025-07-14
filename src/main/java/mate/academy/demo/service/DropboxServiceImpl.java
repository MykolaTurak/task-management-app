package mate.academy.demo.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DropboxServiceImpl implements DropboxService {
    @Value("${dropbox.token}")
    private String accessToken;
    private DbxClientV2 client;

    @PostConstruct
    public void init() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        this.client = new DbxClientV2(config, accessToken);
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName)
            throws DbxException, IOException {
        String dropboxPath = fileName.startsWith("/") ? fileName : "/" + fileName;
        FileMetadata metadata = client.files().upload(dropboxPath).uploadAndFinish(inputStream);
        return metadata.getId();
    }

    @Override
    public InputStream downloadFile(String fileId) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String pathLower = client.files().getMetadataBuilder(fileId).start().getPathLower();
            client.files().download(pathLower).download(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Can't download file with id: " + fileId, e);
        }
    }
}
