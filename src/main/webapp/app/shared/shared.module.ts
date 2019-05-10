import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SafhSharedLibsModule, SafhSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [SafhSharedLibsModule, SafhSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [SafhSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhSharedModule {
  static forRoot() {
    return {
      ngModule: SafhSharedModule
    };
  }
}
