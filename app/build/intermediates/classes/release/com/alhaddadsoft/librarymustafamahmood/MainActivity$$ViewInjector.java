// Generated code from Butter Knife. Do not modify!
package com.alhaddadsoft.librarymustafamahmood;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class MainActivity$$ViewInjector<T extends com.alhaddadsoft.librarymustafamahmood.MainActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558490, "field 'gridView'");
    target.gridView = finder.castView(view, 2131558490, "field 'gridView'");
    view = finder.findRequiredView(source, 2131558491, "field 'editText'");
    target.editText = finder.castView(view, 2131558491, "field 'editText'");
  }

  @Override public void reset(T target) {
    target.gridView = null;
    target.editText = null;
  }
}
