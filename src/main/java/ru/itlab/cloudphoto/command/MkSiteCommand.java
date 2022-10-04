package ru.itlab.cloudphoto.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import ru.itlab.cloudphoto.service.SiteService;

@Component
@CommandLine.Command(name = "mksite", description = "create site with all images")
@RequiredArgsConstructor
public class MkSiteCommand implements Runnable {

    private final SiteService siteService;

    @Override
    public void run() {
        System.out.println(siteService.getAlbumWebsiteUrl());
    }
}
