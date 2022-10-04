package ru.itlab.cloudphoto.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import ru.itlab.cloudphoto.service.PhotoService;

import java.nio.file.Path;

@Component
@CommandLine.Command(name = "download", description = "cloudphoto download command")
@RequiredArgsConstructor
@Slf4j
public class DownloadCommand implements Runnable {

    private final PhotoService photoService;
    @Getter
    @Setter
    @CommandLine.Option(names = "--album", description = "album name", required = true)
    private String albumName;
    @Getter
    @Setter
    @CommandLine.Option(names = "--path", description = "photo path")
    private String photosPath;

    @Override
    public void run() {
        photosPath = photosPath == null ? System.getProperty("user.dir") : photosPath;
        photoService.downloadPhotoListByAlbumName(albumName, Path.of(photosPath).normalize().toAbsolutePath().toFile());
    }
}
