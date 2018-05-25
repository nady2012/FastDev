package com.mamba.mambasdk.video;

import com.danikula.videocache.file.DiskUsage;
import com.danikula.videocache.file.FileNameGenerator;
import com.mamba.mambasdk.util.FileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shijunfeng on 2018/5/25.
 */
public class MVideoCacheConfig {

    public static final File DEFAULT_CACHE_FILE = FileUtil.createDir(FileUtil.getSDPath() + "mamba_video");
    public static final long DEFAULT_MAX_CACHE_SIZE = 1024 * 1024 * 1024;
    public static final DiskUsage DEFAULT_DISKUSAGE = new MDiskUsageStrategy();
    public static final int DEFAULT_MAX_CACHE_FILE_COUNT = 20;
    public static final MVideFNGenerator DEFAULT_FN_GENERATOR = new MVideFNGenerator(new MVideFNGenerator.IGenerateListener() {
        @Override
        public String onFileNameSet(String url) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateStr = dateFormat.format(new Date(System.currentTimeMillis()));
            return dateStr + ".mp4";
        }
    });
    public static final MVideoCacheConfig DEFAULT_CONFIG = new MVideoCacheConfig(DEFAULT_CACHE_FILE, DEFAULT_FN_GENERATOR, DEFAULT_DISKUSAGE, DEFAULT_MAX_CACHE_SIZE,DEFAULT_MAX_CACHE_FILE_COUNT);
    public File cacheRoot;
    public FileNameGenerator fileNameGenerator;
    public DiskUsage diskUsage;
    public long maxCacheSize;
    public int maxCacheFileCount;

    public MVideoCacheConfig(File cacheRoot, FileNameGenerator fileNameGenerator, DiskUsage diskUsage, long maxCacheSize, int maxCacheFileCount) {
        this.cacheRoot = cacheRoot;
        this.fileNameGenerator = fileNameGenerator;
        this.diskUsage = diskUsage;
        this.maxCacheSize = maxCacheSize;
        this.maxCacheFileCount = maxCacheFileCount;
    }

}
