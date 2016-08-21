package com.xieziming.stap.file.model;

/**
 * Created by Suny on 8/14/16.
 */
public class StapFilePostRequest {
    private String path;
    private String overwrite;
    private byte[] content;

    public String getPath() {
        return path;
    }

    public String getOverwrite() {
        return overwrite;
    }

    public byte[] getContent() {
        return content;
    }
}
