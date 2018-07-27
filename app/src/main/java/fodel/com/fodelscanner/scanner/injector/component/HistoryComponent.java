package fodel.com.fodelscanner.scanner.injector.component;

import dagger.Component;
import fodel.com.fodelscanner.scanner.injector.PerActivity;
import fodel.com.fodelscanner.scanner.injector.module.HistoryModule;
import fodel.com.fodelscanner.scanner.ui.HistoryActivity;

/**
 * Created by fula on 2017/7/24.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = HistoryModule.class)
public interface HistoryComponent {

    HistoryActivity inject(HistoryActivity activity);
}
