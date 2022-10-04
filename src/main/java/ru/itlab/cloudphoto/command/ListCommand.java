package ru.itlab.cloudphoto.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ru.itlab.cloudphoto.domain.model.Album;
import ru.itlab.cloudphoto.service.AlbumService;
import ru.itlab.cloudphoto.service.PhotoService;

import java.util.List;
import java.util.Optional;

@Component
@Command(name = "list", description = "show all in cloud")
@RequiredArgsConstructor
public class ListCommand implements Runnable {

    private final AlbumService albumService;
    private final PhotoService photoService;
    @Getter
    @Setter
    @Option(names = "--album", description = "album name")
    private String album;

    @Override
    public void run() {
        Optional.ofNullable(album)
                .ifPresentOrElse(
                        albumName -> {
                            List<String> photoKeys = photoService.getPhotoKeyListByAlbumName(albumService.getOrThrow(albumName).getName());
                            if (photoKeys.isEmpty()) {
                                throw new IllegalStateException("Have no photos in album");
                            }
                            photoKeys.forEach(System.out::println);
                        },
                        () -> {
                            List<Album> albumList = albumService.getAllAlbumsDto().getAlbumList();
                            if (albumList.isEmpty()) {
                                throw new IllegalStateException("Have no any albums");
                            }
                            albumList.forEach(System.out::println);
                        });

    }
}
