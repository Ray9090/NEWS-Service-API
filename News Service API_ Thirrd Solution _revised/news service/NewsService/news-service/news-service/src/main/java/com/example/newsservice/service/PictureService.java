package com.example.newsservice.service;


import com.example.newsservice.dto.PictureDTO;
import com.example.newsservice.exception.ErrorWhileUploadException;
import com.example.newsservice.exception.NotFoundException;
import com.example.newsservice.model.News;
import com.example.newsservice.model.Picture;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.PictureRepository;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class PictureService {
    @Value("${path.baseDirectory}")
    private String baseDirectory;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Transactional
    public void upload(MultipartFile file,String type,Long newsId) {
        try{
            Optional<News> news = newsRepository.findById(newsId);
            if(news.isEmpty()){
                throw new NotFoundException(News.class,newsId);
            }

            if (file != null && type.equals("img")) {
                uploadImg(file,news);
            }
        }catch (Exception e){
            throw new ErrorWhileUploadException(e.getMessage());
        }
    }

    private void uploadImg(MultipartFile img, Optional<News> news) throws IOException {
        String extension=img.getOriginalFilename().substring(img.getOriginalFilename().lastIndexOf('.')+1);
        System.out.println("Original Image Byte Size - " + img.getBytes().length);

        if(extension.equals("jpeg") || extension.equals("png") || extension.equals("jpg")){
            String fileName = news.get().getText()+"."+extension;
            fileName=fileName.replaceAll(":","");
            fileName=fileName.replaceAll("-","");
            fileName=fileName.replaceAll(" ","");

            Picture picture = new Picture();

            picture.setName(fileName);
            picture.setType(extension);
            picture.setFile(compressBytes(img.getBytes()));


            Picture picture1 = pictureRepository.save(picture);

            news.get().setPictureId(picture1.getId());
            newsRepository.save( news.get() );

        }else {
            throw new ErrorWhileUploadException("img extension not supported");
        }
    }

    /**
     * Compress Picture Bytes
     * @param data
     * @return
     */
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }


    public PictureDTO retrieveImage(Long imageId) {
        Optional<Picture> retrievedImage = pictureRepository.findById(imageId);

        if (retrievedImage.isPresent()) {
            PictureDTO pictureDTO = new PictureDTO();
            pictureDTO.setId(retrievedImage.get().getId());
            pictureDTO.setName(retrievedImage.get().getName());
            pictureDTO.setType(retrievedImage.get().getType());
            pictureDTO.setFile(decompressBytes(retrievedImage.get().getFile()));

            return pictureDTO;
        }else {
            throw new NotFoundException("Picture");
        }

    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

    public String deletePicture(Long imageId){
        Optional<Picture> retrievedImage = pictureRepository.findById(imageId);
        if (retrievedImage.isPresent()) {
            pictureRepository.delete(retrievedImage.get());

            return "Picture deleted successfully";
        }else {
            return "No Picture for this news";
        }
    }
}
