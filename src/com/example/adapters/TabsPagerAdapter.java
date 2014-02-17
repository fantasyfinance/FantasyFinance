package com.example.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.fantasyfinance.FollowedStocks;
import com.example.fantasyfinance.PredictStocks;
import com.example.fantasyfinance.ProfileActivity;
import com.example.fantasyfinance.SearchStocks;


public class TabsPagerAdapter extends FragmentPagerAdapter {
	 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            return new ProfileActivity();
        case 1:
            return new FollowedStocks();
        case 2:
            return new SearchStocks();
        case 3:
        	return new PredictStocks();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        return 4;
    }
 
}