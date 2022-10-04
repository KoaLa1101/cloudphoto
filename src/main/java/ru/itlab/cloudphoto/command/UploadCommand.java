package ru.itlab.cloudphoto.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import ru.itlab.cloudphoto.service.AlbumService;
import ru.itlab.cloudphoto.service.PhotoService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@CommandLine.Command(name = "upload", description = "cloudphoto upload command")
@RequiredArgsConstructor
@Slf4j
public class UploadCommand implements Runnable {

    private final PhotoService photoService;
    private final AlbumService albumService;
    private final Tika tika = new Tika(); //todo Autowire
    @Getter
    @Setter
    @CommandLine.Option(names = "--album", description = "album name", required = true)
    private String albumName;
    @Getter
    @Setter
    @CommandLine.Option(names = "--path", description = "photo name")
    private String photosPath;

    @Override
    public void run() {

        photosPath = photosPath == null ? System.getProperty("user.dir") : photosPath;
        List<File> fileList = Arrays.stream(Objects.requireNonNull(getAlbumDirectory(photosPath).listFiles())).toList();
        albumService.saveAlbum(albumName);
        photoService.savePhotoList(fileList, albumName);
    }

    public File getAlbumDirectory(String path) {
        File albumDirectory = new File(path);
        validateAlbumPath(albumDirectory);
        return albumDirectory;
    }

    private void validateAlbumPath(File photosDirectorty) {
        if (!photosDirectorty.isDirectory()) {
            throw new IllegalStateException("Photos directory is not a directory");
        }
        if (Objects.isNull(photosDirectorty.listFiles())) {
            throw new IllegalStateException("its not directory with files");
        }
        if (Objects.requireNonNull(photosDirectorty.listFiles()).length == 0) {
            throw new IllegalStateException("You have empty directory");
        }
        Arrays.stream(Objects.requireNonNull(photosDirectorty.listFiles())).forEach(photoFile -> {
            if (!isImageMimeType(photoFile)) {
                throw new IllegalStateException("Some files are not images in directory");
            }
        });

    }

    public boolean isImageMimeType(File src) {
        try (FileInputStream fis = new FileInputStream(src)) {
            String mime = tika.detect(fis, src.getName());
            return mime.contains("/")
                    && mime.split("/")[0].equalsIgnoreCase("image");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
