package ru.itlab.cloudphoto;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Lazy;
import picocli.CommandLine;
import ru.itlab.cloudphoto.command.CommandResolver;
import ru.itlab.cloudphoto.helper.ConfigHelper;
import ru.itlab.cloudphoto.service.AlbumService;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class CloudphotoApplication implements CommandLineRunner {
    @Lazy
    @Autowired
    private AlbumService albumService;

    @Autowired
    private ConfigHelper configUtil;

    @Autowired
    private CommandLine.IFactory factory;
    @Autowired
    private CommandResolver commandResolver;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(Arrays.toString(args));
        int exitCode = new CommandLine(commandResolver,factory).execute(args);
        // System.out.println(exitCode);
    }
    public static void main(String[] args) {
        SpringApplication.run(CloudphotoApplication.class, args);
    }

}