package com.jraska.github.client.rx;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import rx.Observable;
import timber.log.Timber;

import javax.inject.Inject;

public class ObservableLoader {
  private final FragmentManager fragmentManager;

  @Inject
  public ObservableLoader(FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }

  public <R, A extends FragmentActivity> void load(Observable<R> observable,
                                                   SubscriberDelegateProvider<A, R> subscriberDelegateProvider) {
    ObservableLoadFragment existingFragment = (ObservableLoadFragment) fragmentManager.findFragmentByTag(ObservableLoadFragment.TAG);
    if (existingFragment != null && existingFragment.isValid()) {
      Timber.d("Activity %s is already loading its data", existingFragment.getActivity());
      existingFragment.requestDeliver();
      return;
    }

    ObservableLoadFragment fragmentProxy = ObservableLoadFragment.newInstance(observable, subscriberDelegateProvider);
    fragmentManager.beginTransaction()
        .add(fragmentProxy, ObservableLoadFragment.TAG)
        .commit();
  }
}
