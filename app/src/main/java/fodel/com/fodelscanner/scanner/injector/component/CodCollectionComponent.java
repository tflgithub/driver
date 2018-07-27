package fodel.com.fodelscanner.scanner.injector.component;

import dagger.Component;
import fodel.com.fodelscanner.scanner.injector.PerActivity;
import fodel.com.fodelscanner.scanner.injector.module.CodCollectionModule;
import fodel.com.fodelscanner.scanner.ui.CodCollectionActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = CodCollectionModule.class)
public interface CodCollectionComponent {

    CodCollectionActivity inject(CodCollectionActivity activity);
}
