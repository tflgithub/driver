package fodel.com.fodelscanner.scanner.injector.component;

import dagger.Component;
import fodel.com.fodelscanner.scanner.injector.PerActivity;
import fodel.com.fodelscanner.scanner.injector.module.HomeModule;
import fodel.com.fodelscanner.scanner.ui.HomeActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = HomeModule.class)
public interface HomeComponent {

    HomeActivity inject(HomeActivity activity);
}
