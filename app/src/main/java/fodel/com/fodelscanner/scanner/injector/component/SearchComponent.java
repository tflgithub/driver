package fodel.com.fodelscanner.scanner.injector.component;

import dagger.Component;
import fodel.com.fodelscanner.scanner.injector.PerActivity;
import fodel.com.fodelscanner.scanner.injector.module.SearchModule;
import fodel.com.fodelscanner.scanner.ui.SearchActivity;

/**
 * Created by fula on 2017/7/24.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = SearchModule.class)
public interface SearchComponent {

    SearchActivity inject(SearchActivity activity);
}
