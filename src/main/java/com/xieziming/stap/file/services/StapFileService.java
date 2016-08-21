package com.xieziming.stap.file.services;

import com.xieziming.stap.file.model.StapFileDao;
import com.xieziming.stap.file.model.StapFileGetRequest;
import com.xieziming.stap.file.model.StapFilePostRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * Created by Suny on 5/16/16.
 */
@Controller
@RequestMapping("file")
public class StapFileService {
    private static Logger logger = LoggerFactory.getLogger(StapFileService.class);
    private final String UTF8 = ";charset=UTF-8";

    @Autowired
    private StapFileDao stapFileDao;

    @RequestMapping(value = "upload", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE+UTF8)
    @ResponseStatus(HttpStatus.OK)
    public void postFile(@RequestBody final StapFilePostRequest stapFilePostRequest) {
        stapFileDao.post(stapFilePostRequest.getPath(), stapFilePostRequest.getContent(), stapFilePostRequest.getOverwrite());
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE+UTF8)
    @ResponseStatus(HttpStatus.OK)
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(!ServletFileUpload.isMultipartContent(request)) throw new Exception("Bad file request!");

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024*1024); //1MB

        ServletFileUpload upload = new ServletFileUpload(factory);
        FileItemIterator fileItemIterator = upload.getItemIterator(request);
        String path = null;
        String overwrite = "false";
        byte[] fileContent = null;
        while (fileItemIterator.hasNext()) {
            FileItemStream item = fileItemIterator.next();
            String name = item.getFieldName();
            InputStream inputStream = item.openStream();
            if (item.isFormField()) {
                String value = Streams.asString(inputStream);
                if("path".equalsIgnoreCase(name)) path = value;
                if("overwrite".equalsIgnoreCase(name)) overwrite = value;
                logger.debug("Form field " + name + " with value " + name + " detected.");
            } else {
                logger.debug("File field " + name + " with file name " + item.getName() + " detected.");
                fileContent= IOUtils.toByteArray(inputStream);
            }
        }
        if(path != null && fileContent != null){
                stapFileDao.post(path, fileContent, overwrite);
        }else {
            throw new Exception("path or content is null!");
        }

    }

    @RequestMapping(value = "{path}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] getFileContent(@PathVariable("path") String path) {
        return stapFileDao.findByPath(path);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE+UTF8)
    @ResponseBody
    public byte[] getFileContent(@RequestBody final StapFileGetRequest stapFileGetRequest) {
        return stapFileDao.findByPath(stapFileGetRequest.getPath());
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE+UTF8)
    @ResponseStatus(HttpStatus.OK)
    public void deleteFile(@RequestBody final StapFileGetRequest stapFileGetRequest) {
        stapFileDao.deleteByPath(stapFileGetRequest.getPath());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Exception> handleAllException(Exception exception) {
        logger.error("Stap File Service Exception", exception);
        if(exception instanceof EmptyResultDataAccessException){
            return new ResponseEntity<Exception>(new Exception("Record not found"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Exception>(exception, HttpStatus.BAD_REQUEST);
    }
}

