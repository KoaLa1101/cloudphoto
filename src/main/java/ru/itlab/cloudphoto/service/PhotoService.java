package ru.itlab.cloudphoto.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.itlab.cloudphoto.domain.model.Photo;
import ru.itlab.cloudphoto.helper.ConfigHelper;
import ru.itlab.cloudphoto.util.ObjectMapperUtil;

import java.io.File;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class PhotoService {
    private final ObjectMapperUtil objectMapperUtil;
    private final AmazonS3 yandexS3;
    private final ObjectMapper objectMapper;
    private final ConfigHelper configHelper;
    private final TransferManager transferManager;
    private Function<Photo, String> getKeyFunc = photo -> photo.getAlbumName() + "/" + photo.getName();

  /*  public Photo savePhotoList(Photo photo){
        yandexS3.putObject(configHelper.getParamFromIniAWSSection("bucketName"),getKeyFunc.apply(photo),objectMapperUtil.writeValueAsString(photo));
        return photo;
    }*/

    public void savePhotoList(List<File> fileList, String albumName) {
        fileList.forEach(file -> {
            try {
                transferManager.upload(configHelper.getParamFromIniAWSSection("bucketName"), albumName + "/" + file.getName(), file)
                        .waitForCompletion();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });
    }


    public List<String> getPhotoKeyListByAlbumName(String albumName) {
        return yandexS3.listObjects(configHelper.getParamFromIniAWSSection("bucketName"), albumName).getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey).toList();
    }

    public void downloadPhotoListByAlbumName(String albumName, File destinationDir) {
        MultipleFileDownload download =
                transferManager.downloadDirectory(configHelper
                        .getParamFromIniAWSSection("buketName"), albumName + "/", destinationDir);
        try {
            download.waitForCompletion();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public void deleteAllPhotosInAlbum(String albumName) {
        yandexS3.deleteObjects(new DeleteObjectsRequest(configHelper.getParamFromIniAWSSection("bucketName"))
                .withKeys(getPhotoKeyListByAlbumName(albumName).toArray(new String[1])));
    }
}
