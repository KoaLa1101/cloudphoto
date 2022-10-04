package ru.itlab.cloudphoto.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import ru.itlab.cloudphoto.service.AlbumService;
import ru.itlab.cloudphoto.service.PhotoService;

import java.util.Objects;

@Component
@CommandLine.Command(name = "delete", description = "cloudphoto delete command")
@RequiredArgsConstructor
@Slf4j
public class DeleteCommand implements Runnable{

    @Getter
    @Setter
    @CommandLine.Option(names = "--album", description = "album name", required = true)
    private String albumName;
    @Getter
    @Setter
    @CommandLine.Option(names = "--path", description = "photo path")
    private String photoPath;

    private final AlbumService albumService;
    private final PhotoService photoService;
    @Override
    public void run() {
        if(Objects.isNull(photoPath)){
            albumService.deleteAlbum(albumName);
        }
        else {
            photoService.deletePhotoInAlbum(albumName,photoPath);
        }
    }
}
