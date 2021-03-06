package com.gmugu.happytour.presenter;

import com.gmugu.happyhour.message.ScenicModel;

/**
 * Created by mugu on 16-4-19 下午5:58.
 */
public interface IRealTimePresenter {
    void onCreateBnPressed();

    void onCreateSure();

    void onDeleteBnPressed();

    void onJoinBnPressed();

    void onOutBnPressed();

    void onTeamItemClick(int position);

    void onStartBnPressed();

    void onStopBnPressed();

    void onCreateView();

    void onLocToScenicBnPressed();

    void onLocToSelfBnPressed();

    void onScenicPointClick(ScenicModel scenicModel);
}
