package com.gmugu.happyhour.message.result;

import com.gmugu.happyhour.message.ScenicModel;

/**
 * Created by mugu on 16-4-23 下午7:29.
 */
public class GuideCmdResult extends BaseResult {
    private ScenicModel scenicModel;

    public GuideCmdResult() {
    }

    public GuideCmdResult(int code, String message, ScenicModel scenicModel) {
        super(code, message);
        this.scenicModel = scenicModel;
    }

    public ScenicModel getScenicModel() {
        return scenicModel;
    }

    public void setScenicModel(ScenicModel scenicModel) {
        this.scenicModel = scenicModel;
    }
}
