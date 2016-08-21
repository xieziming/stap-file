package com.xieziming.stap.file.model;

import com.xieziming.stap.file.util.StapFileDbUtil;
import org.springframework.stereotype.Component;

/**
 * Created by Suny on 7/19/16.
 */
@Component
public class StapFileDao {
    private static final String STAP_FILE_TABLE = "Stap_File";

    public byte[] findByPath(String path) {
        String sql = "SELECT content FROM "+STAP_FILE_TABLE+" WHERE Path=?";
        return StapFileDbUtil.getJdbcTemplate().queryForObject(sql, new Object[]{path}, byte[].class);
    }

    public void deleteByPath(String path){
        String sql = "DELETE FROM "+STAP_FILE_TABLE+" WHERE Path=?";
        StapFileDbUtil.getJdbcTemplate().update(sql, new Object[]{path});
    }

    public boolean exists(String path) {
        String sql = "SELECT count(*) FROM "+STAP_FILE_TABLE+" WHERE Path=?";
        return StapFileDbUtil.getJdbcTemplate().queryForObject(sql, new Object[]{path}, Integer.class) > 0;
    }

    public void put(String path, byte[] content){
        if(exists(path)){
            update(path, content);
        }else{
            add(path, content);
        }
    }

    public void post(String path, byte[] content, String overwrite){
        if(Boolean.valueOf(overwrite)) {
            put(path, content);
        }else {
            add(path, content);
        }
    }

    public void add(String path, byte[] content) {
        String sql = "INSERT INTO "+STAP_FILE_TABLE+" SET Path=?, Content=?";
        StapFileDbUtil.getJdbcTemplate().update(sql, new Object[]{path, content});
    }

    public void update(String path, byte[] content) {
        String sql = "UPDATE "+STAP_FILE_TABLE+" SET Path=?, Content=? WHERE Path=?";
        StapFileDbUtil.getJdbcTemplate().update(sql, new Object[]{path, content, path});
    }
}
