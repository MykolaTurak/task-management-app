package mate.academy.demo.service;

import com.dropbox.core.DbxException;
import java.io.IOException;
import java.io.InputStream;

public interface DropboxService {
    String uploadFile(InputStream inputStream, String fileName) throws DbxException, IOException;

    InputStream downloadFile(String fileId);
}
