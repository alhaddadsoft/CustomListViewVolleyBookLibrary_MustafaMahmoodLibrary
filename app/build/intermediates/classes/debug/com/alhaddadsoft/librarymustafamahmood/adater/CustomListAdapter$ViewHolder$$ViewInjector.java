// Generated code from Butter Knife. Do not modify!
package com.alhaddadsoft.librarymustafamahmood.adater;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class CustomListAdapter$ViewHolder$$ViewInjector<T extends com.alhaddadsoft.librarymustafamahmood.adater.CustomListAdapter.ViewHolder> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558452, "field 'title'");
    target.title = finder.castView(view, 2131558452, "field 'title'");
    view = finder.findRequiredView(source, 2131558553, "field 'genre'");
    target.genre = finder.castView(view, 2131558553, "field 'genre'");
    view = finder.findRequiredView(source, 2131558556, "field 'pdfurl'");
    target.pdfurl = finder.castView(view, 2131558556, "field 'pdfurl'");
    view = finder.findRequiredView(source, 2131558555, "field 'saveas'");
    target.saveas = finder.castView(view, 2131558555, "field 'saveas'");
    view = finder.findRequiredView(source, 2131558552, "field 'rating'");
    target.rating = finder.castView(view, 2131558552, "field 'rating'");
    view = finder.findRequiredView(source, 2131558554, "field 'releaseyear'");
    target.releaseyear = finder.castView(view, 2131558554, "field 'releaseyear'");
    view = finder.findRequiredView(source, 2131558551, "field 'thumbNail'");
    target.thumbNail = finder.castView(view, 2131558551, "field 'thumbNail'");
  }

  @Override public void reset(T target) {
    target.title = null;
    target.genre = null;
    target.pdfurl = null;
    target.saveas = null;
    target.rating = null;
    target.releaseyear = null;
    target.thumbNail = null;
  }
}
