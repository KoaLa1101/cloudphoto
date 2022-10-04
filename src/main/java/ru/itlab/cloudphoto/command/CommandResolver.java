package ru.itlab.cloudphoto.command;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

/**
 * Binding all commands and invoke them in CloudphotoApplication class
 */
@Component
@Command(name = "cloudphoto",
        subcommands = {InitCommand.class,
                ListCommand.class,
                UploadCommand.class,
                DeleteCommand.class,
                DownloadCommand.class,
                MkSiteCommand.class
        },description = "base command resolver")
public class CommandResolver {

}
