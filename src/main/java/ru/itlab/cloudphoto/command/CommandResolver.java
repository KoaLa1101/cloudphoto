package ru.itlab.cloudphoto.command;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Spec;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;

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
   @Spec CommandSpec commandSpec; //fixme delete


}
