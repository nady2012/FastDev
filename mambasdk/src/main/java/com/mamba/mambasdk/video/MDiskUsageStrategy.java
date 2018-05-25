package com.mamba.mambasdk.video;

import com.danikula.videocache.file.LruDiskUsage;

import java.io.File;

/**
 * Created by shijunfeng on 2018/5/25.
 */
public class MDiskUsageStrategy extends LruDiskUsage{

    @Override
    protected boolean accept(File file, long totalSize, int totalCount) {
        return totalCount <= MVideoCacheConfig.DEFAULT_MAX_CACHE_FILE_COUNT;
    }
}
