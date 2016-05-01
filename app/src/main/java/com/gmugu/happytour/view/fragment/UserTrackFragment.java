package com.gmugu.happytour.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.gmugu.happyhour.message.TrackModel;
import com.gmugu.happytour.R;
import com.gmugu.happytour.presenter.IUserTrackPresenter;
import com.gmugu.happytour.view.IUserTrackView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserTrackFragment extends BaseFragment implements IUserTrackView, AdapterView.OnItemClickListener {

    private OnUserTrackFragmentInteractionListener mListener;
    private View mView;
    private IUserTrackPresenter presenter;

    private ListView sideList;
    private MapViewFragment mapViewFragment;

    public UserTrackFragment() {
    }

    public static UserTrackFragment newInstance() {
        UserTrackFragment fragment = new UserTrackFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_user_track, container, false);
        sideList = (ListView) findViewById(R.id.fragment_user_track_lv);
        sideList.setOnItemClickListener(this);
        mapViewFragment = MapViewFragment.newInstence();
        getFragmentManager().beginTransaction().replace(R.id.fragment_user_track_fl, mapViewFragment);
        presenter.afterCreatView();
        return mView;
    }

    private View findViewById(int id) {
        return mView.findViewById(id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnUserTrackFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUserTrackFragmentInteractionListener");
        }
        presenter = mListener.getUserTrackPresenter();
        if (presenter == null) {
            throw new NullPointerException("userTrackPresenter must be not null");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updateTeammateList(String[] data) {
        try {
            sideList.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void askWhickTrack(List<TrackModel> trackModels) {

        List<Map<String, Object>> data = new ArrayList<>();
        String[] from = new String[]{};
        int[] to = new int[]{};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), data,R.layout.track_list_item, from,to);
        new AlertDialog.Builder(getActivity()).setTitle("选择轨迹").setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onSideItemClick(position);
    }

    public interface OnUserTrackFragmentInteractionListener {
        IUserTrackPresenter getUserTrackPresenter();
    }

}
