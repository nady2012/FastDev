package com.mamba.mambasdk.video;

import com.danikula.videocache.file.FileNameGenerator;

/**
 * Created by shijunfeng on 2018/5/25.
 */
public class MVideFNGenerator implements FileNameGenerator {
    private IGenerateListener generateListener;

    public MVideFNGenerator(IGenerateListener generateListener) {
        if (generateListener == null) {
            throw new IllegalArgumentException("must implement IGenerateListener!!");
        }
        this.generateListener = generateListener;
    }

    @Override
    public String generate(String url) {
        return generateListener.onFileNameSet(url);
    }

    public interface IGenerateListener {
        String onFileNameSet(String url);
    }
}
